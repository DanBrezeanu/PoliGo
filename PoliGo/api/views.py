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
import functools

def key_to_user(req_json):
    """
    Returns Profile associated to the api_key provided in request.
    :param request: request from view function
    :type request: HttpRequest
    :returns: Profile with api_key equal to the one provided or None if not found
    :rtype: Profile/None
    """
    
    # Get API-KEY from request
    try:    
        api_key = req_json['api_key']
    except: # No API-KEY provided
        return None

    # Find profile matching the API-KEY
    try:    
        return Profile.objects.get(api_key = api_key)
    except store.models.Profile.DoesNotExist:
        return None
        

def json_from_request(request):
    return json.loads(request.body.decode('utf-8'))

def is_staff(func):
    """
    Function decorator that only allows the function execution only if user.is_staff is True
    and the API key provided exists and is valid

    :returns: The functions return value or Error 403 JSON if access is not granted
    :rtype: HttpResponse
    """

    @functools.wraps(func)
    def wrapper_decorator(*args, **kwargs):
        # Get profile
        profile = key_to_user(args[0])

        # Acces Denied
        if profile is None or not profile.user.is_staff:
            return HttpResponse(Error403('Unauthorized'))

        return func(*args, **kwargs)

    return wrapper_decorator


def Home(request):
   # print(UserSerializer(request.user).data)
    return HttpResponse('ok')

# @is_staff      
def check_stock(request):
    """
    Provides and ENDPOINT for querying the products.

    :param request: implicit request
    :type request: HttpRequest

    :returns: JSON containing the queried products or Error
    :rtype: HttpResponse
    """

    # Get the queried products
    if request.method == 'GET':
        products_json = db_utils.check_stock(request)
    else:
        return HttpResponse(Error503('Only GET requests accepted'))

    return HttpResponse(OK200(products_json))

# @is_staff
def add_stock(request):
    if request.method == 'POST':
        result = db_utils.add_stock(json_from_request(request))

        if result is None:
            return HttpResponse(Error422('Wrong data'))
        else:
            return HttpResponse(OK200(None))
    else:
        return HttpResponse(Error503('Only POST requests accepted'))


# @is_staff
def remove_stock(request):
    if request.method == 'POST':
        result = db_utils.remove_stock(json_from_request(request))

        if result is None:
            return HttpResponse(Error422('Wrong data'))
        else:
            return HttpResponse(OK200(None))
    else:
        return HttpResponse(Error503('Only POST requests accepted'))

def login(request):
    if request.method == 'POST':
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
    else:
        return HttpResponse(Error503('Onlyt POST requests accepted'))

def register(request):
    json_req = json_from_request(request)
    
    if request.method == 'POST':
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
    else:
        return HttpResponse(Error503('Only POST requests accepted'))

def add_card(request):
    json_req = json_from_request(request)
    user = key_to_user(json_req)

    card_details = json_req['card_details']

    new_card = BankCardDeserializer.deserialize(**card_details, user=user)
    new_card.save()
    
    cards = [BankCardSerializer(card).data for card in BankCard.objects.filter(profile=user)]
    
    if request.method == 'POST':
        return HttpResponse(OK200(json.dumps({'cards': cards})))
    else:
        return HttpResponse(Error503('Only POST requests accepted'))

def shopping_cart(request):
    if request.method == 'GET':
        req_json = json.loads(request.body.decode('utf-8'))
        profiler = key_to_user(req_json)
        if profiler is None:
            return HttpResponse(Error422('Wrong data'))
        get_carts = [item for item in ShoppingCart.objects.filter(customer__user=profiler.user)]
        for cart in get_carts:
            if cart.active:
                return HttpResponse(json.dumps({'products': [ProductSerializer(query).data for query in cart.products.all()]}))

def remove_from_cart(request):
    if request.method == 'POST':
        req_json = params = json.loads(request.body.decode('utf-8'))
        profiler = key_to_user(req_json)
        if profiler is None:
            return HttpResponse(Error422('Wrong data'))
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

# def pretul_produsului(request):
#     user = key_to_user(request)

#     if user is None:
#         nu exista utilizator cu cheia asta

#     user -> profile 
#     shopping_cart-ul -> profile-ului
#     cautati produsul cu SKU furnizat
#     adaugati produsul -> shopping cart 

# TODO:

def place_order(request):
    user = key_to_user(json_from_request(request))

    if user is None:
        return HttpResponse(Error422('No such user'))
    
    if request.method == 'POST':
        try:
            # get active shopping cart
            shopping_cart = ShoppingCart.objects.filter(customer=user, active=True)[0]
        except:
            return HttpResponse(Error422('No active Shopping Cart'))

        shopping_cart.active = False

        if ShoppingHistory.objects.filter(customer=user) != []:
            shopping_cart.shoppingHistory = ShoppingHistory.objects.filter(customer=user)[0]
        else:
            shopping_cart.shoppingHistory = ShoppingHistory(customer=user)

        shopping_cart.shoppingHistory.save()
        shopping_cart.save()
        
    else:
        return HttpResponse(Error503('Only POST requests accepted'))

    return HttpResponse(OK200(json.dumps(ShoppingCartSerializer(shopping_cart).data)))

def shopping_history(request):
    user = key_to_user(json_from_request(request))

    if user is None:
        return HttpResponse(Error422('No such user'))

    if request.method == 'GET':
        try:
            shopping_history = ShoppingHistory.objects.filter(customer=user)[0]
        except:
            return HttpResponse(Error422('No available Shopping History'))
        shopping_carts = [ShoppingCartSerializer(cart).data for cart in ShoppingCart.objects.filter(shoppingHistory=shopping_history)]
        ret_json = json.dumps({'carts': shopping_carts})
    else:
        return HttpResponse(Error503('Only GET requests accepted'))

    return HttpResponse(OK200(ret_json))


# ENDPOINT-URI PENTRU UTILIZATORI (fara @is_staff)
# am scanat produsul -> POST REQ 
# am eliminat din cos -> POST REQ
# pretul produsului -> GET
# historicul cumparaturilor -> GET
# am terminat cumparaturile -> POST REQ
# orice altceva