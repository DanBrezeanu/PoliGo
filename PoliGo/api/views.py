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
    
    result = db_utils.login(json_req)
    return HttpResponse(result)


@post
def register(request):
    json_req = json_from_request(request)

    result = db_utils.register(json_req)
    return HttpResponse(result)
    
@post
@is_user
def add_card(request):
    json_req = json_from_request(request)
    user = key_to_user(json_req)

    result = db_utils.add_card(json_req, user)
    return HttpResponse(result)


@get
@is_user
def shopping_cart(request):
    req_json = json_from_request(request)
    user = key_to_user(req_json)

    cart = db_utils.shopping_cart(user)
    return HttpResponse(cart)

@post
@is_user
def remove_from_cart(request):
    json_req = json.loads(request.body.decode('utf-8'))
    user = key_to_user(json_req)

    result = db_utils.remove_from_cart(json_req, user)
    return HttpResponse(result)

@post
@is_user
def place_order(request):
    user = key_to_user(json_from_request(request))

    result = db_utils.place_order(user)
    return HttpResponse(result)

@get
@is_user
def shopping_history(request):
    user = key_to_user(json_from_request(request))

    result = db_utils.shopping_history(user)
    return HttpResponse(result)


@post
@is_user
def add_to_cart(request):
    json_req = json_from_request(request)
    user = key_to_user(json_req)

    result = db_utils.add_to_cart(json_req, user)

    return HttpResponse(result)
 