from django.shortcuts import render, HttpResponse
from django.core.exceptions import PermissionDenied

from django.contrib.auth.models import User
from django.contrib.auth import authenticate

from api.serializers import BankCardDeserializer, BankCardSerializer, ProductSerializer, ShoppingCartSerializer, UserSerializer
from api.http_codes import Error403, Error422, Error503, OK200

from store.models import BankCard, Product, Profile, ShoppingCart, ShoppingHistory
from api import db_utils
import json
import store

from api.api_decorators import is_staff, get, post, is_user
from api.api_utils import key_to_user, json_from_request

@get
def Home(request):
    return HttpResponse('ok')

# @is_staff
@get
def check_stock(request):
    """
    Provides an ENDPOINT for querying the products.

    :param request: implicit request
    :type request: HttpRequest

    :returns: JSON containing the queried products or Error
    :rtype: HttpResponse
    """

    # Get the queried products
    json_req = json_from_request(request)
    products_json = db_utils.check_stock(json_req)
    return HttpResponse(OK200(products_json))



@post
# @is_staff
def add_stock(request):
    result = db_utils.add_stock(json_from_request(request))
    return HttpResponse(result)

@post
# @is_staff
def add_product(request):
    json_req = json_from_request(request)
    result = db_utils.add_product(json_req)
    return HttpResponse(result)


@post
# @is_staff
def remove_stock(request):
    json_req = json_from_request(request)
    result = db_utils.remove_stock(json_req)
    return HttpResponse(result)


@post
def login(request):
    json_req = json_from_request(request)
    user = authenticate(username=json_req['username'], password=json_req['password'])

    if user is None:
        return HttpResponse(Error403('Invalid credentials'))

    profile = Profile.objects.filter(user=user)[0]
    cards = [BankCardSerializer(card).data for card in BankCard.objects.filter(profile=profile)]

    return HttpResponse(OK200(json.dumps({
        'api_key': profile.api_key,
        'name': profile.user.username,
        'email': profile.user.email,
        'cards': cards
    })))


@post
def register(request):
    json_req = json_from_request(request)

    try:
        new_user = User.objects.create_user(
                    email=json_req['email'],
                    username=json_req['username'],
                    password=json_req['password']
                )

        new_user.save()
    except django.db.utils.IntegrityError:
        return HttpResponse(Error401('Username already exists'))


    user_profile = Profile(user=new_user)
    user_profile.save()

    return HttpResponse(OK200(json.dumps({
        'api_key': user_profile.api_key,
        'name': user_profile.user.username,
        'email': user_profile.user.email,
    })))

@post
@is_user
def add_card(request):
    json_req = json_from_request(request)
    user = key_to_user(json_req)

    card_details = json_req['card_details']

    new_card = BankCardDeserializer.deserialize(**card_details, user=user)
    new_card.save()
    
    cards = [BankCardSerializer(card).data for card in BankCard.objects.filter(profile=user)]

    return HttpResponse(OK200(json.dumps({'cards': cards})))


@get
@is_user
def shopping_cart(request):
    req_json = json_from_request(request)
    user = key_to_user(req_json)

    cart = db_utils.shopping_cart(req_json, user)
    return HttpResponse(cart)

@post
@is_user
def remove_from_cart(request):
    req_json = params = json.loads(request.body.decode('utf-8'))
    profiler = key_to_user(req_json)

    get_carts = [item for item in ShoppingCart.objects.filter(customer__user=profiler.user)]
    for shop_c in get_carts:
        if shop_c.active:
            cart = shop_c
    all_prod = cart.products.all()
    for prod in all_prod:
        if prod.SKU == req_json['SKU']:
            if prod.stock > req_json['stock']:
                rez = prod.stock - req_json['stock']
                cart.products.filter(SKU=prod.SKU).update(stock=rez)
            elif prod.stock == req_json['stock']:
                    cart.products.remove(prod)
            else:
                return HttpResponse(Error422('Wrong data'))
    return HttpResponse(json.dumps({'products': [ProductSerializer(query).data for query in cart.products.all()]}))

@post
@is_user
def place_order(request):
    user = key_to_user(json_from_request(request))

    try:
        # get active shopping cart
        shopping_cart = ShoppingCart.objects.filter(customer=user, active=True)[0]
    except:
        return HttpResponse(Error422('No active Shopping Cart'))

    shopping_cart.active = False

    shopping_history = list(ShoppingHistory.objects.filter(customer=user))

    if shopping_history != []:
        shopping_cart.shoppingHistory = shopping_history[0]
    else:
        shopping_cart.shoppingHistory = ShoppingHistory(customer=user)

    shopping_cart.shoppingHistory.save()
    shopping_cart.save()
        

    return HttpResponse(OK200(json.dumps(ShoppingCartSerializer(shopping_cart).data)))

@get
@is_user
def shopping_history(request):
    user = key_to_user(json_from_request(request))

    try:
        shopping_history = ShoppingHistory.objects.filter(customer=user)[0]
    except:
        return HttpResponse(Error422('No available Shopping History'))
        
    shopping_carts = [ShoppingCartSerializer(cart).data for cart in ShoppingCart.objects.filter(shoppingHistory=shopping_history)]
    ret_json = json.dumps({'carts': shopping_carts})

    return HttpResponse(OK200(ret_json))


@post
@is_user
def add_to_cart(request):
    json_req = json_from_request(request)
    user = key_to_user(json_req)

    result = db_utils.add_to_cart(json_req, user)

    return HttpResponse(result)
 