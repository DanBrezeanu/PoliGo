from django.shortcuts import render, HttpResponse
from django.core.exceptions import PermissionDenied

from django.contrib.auth.models import User

from api.serializers import UserSerializer, ProductSerializer
from api.http_codes import Error403, Error422, Error503, OK200

from store.models import Product, Profile
from api import db_utils
import json
import store
import functools

def key_to_user(request):
    """
    Returns Profile associated to the api_key provided in request.

    :param request: request from view function
    :type request: HttpRequest

    :returns: Profile with api_key equal to the one provided or None if not found
    :rtype: Profile/None
    """
    
    # Get API-KEY from request
    try:    
        api_key = request.GET['api_key'] if request.method == "GET" else request.POST['api_key'] 
    except: # No API-KEY provided
        return None

    # Find profile matching the API-KEY
    try:    
        return Profile.objects.get(api_key = api_key)
    except store.models.Profile.DoesNotExist:
        return None

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

@is_staff      
def check_stock(request):
    """
    Provides and ENDPOINT for querying the products.

    :param request: implicit request
    :type request: HttpRequest

    :returns: JSON containing the queried products or Error
    :rtype: HttpResponse
    """

    # dir(request)

    # Get the queried products
    if request.method == 'GET':
        products_json = db_utils.check_stock(request)
    else:
        return HttpResponse(Error503('Only GET requests accepted'))

    return HttpResponse(OK200(products_json))

@is_staff
def add_stock(request):
    if request.method == 'POST':
        result = db_utils.add_stock(request)

        if result is None:
            return HttpResponse(Error422('Wrong data'))
        else:
            return HttpResponse(OK200(None))
    else:
        return HttpResponse(Error503('Only POST requests accepted'))


# @is_staff
def remove_stock(request):
    if request.method == 'POST':
        result = db_utils.remove_stock(request)

        if result is None:
            return HttpResponse(Error422('Wrong data'))
        else:
            return HttpResponse(OK200(None))
    else:
        return HttpResponse(Error503('Only POST requests accepted'))


# def pretul_produsului(request):
#     user = key_to_user(request)

#     if user is None:
#         nu exista utilizator cu cheia asta

#     user -> profile 
#     shopping_cart-ul -> profile-ului
#     cautati produsul cu SKU furnizat
#     adaugati produsul -> shopping cart 

# TODO:

# ENDPOINT-URI PENTRU UTILIZATORI (fara @is_staff)
# am scanat produsul -> POST REQ 
# am eliminat din cos -> POST REQ
# pretul produsului -> GET
# historicul cumparaturilor -> GET
# am terminat cumparaturile -> POST REQ
# orice altceva