from django.shortcuts import render, HttpResponse
from django.core.exceptions import PermissionDenied

from django.contrib.auth.models import User

from api.serializers import UserSerializer, ProductSerializer

from store.models import Product, Profile
from api import db_utils
import json
import store

def Home(request):
   # print(UserSerializer(request.user).data)
    return HttpResponse('ok')

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
        api_key = request.GET['api_key']
    except: # No API-KEY provided
        return None

    # Find profile matching the API-KEY
    try:    
        return Profile.objects.get(api_key = api_key)
    except store.models.Profile.DoesNotExist:
        return None

        
def check_stock(request):
    """
    Provides and ENDPOINT for querying the products.

    :param request: implicit request
    :type request: HttpRequest

    :returns: JSON containing the queried products or Error
    :rtype: HttpResponse
    """

    # Get profile
    profile = key_to_user(request)
    
    # Acces Denied
    if profile is None or not profile.user.is_staff:
        return HttpResponse(json.dumps({'error': 403}))

    # Get the queried products
    if request.method == 'GET':
        products_json = db_utils.check_stock(request)
    else:
        return HttpResponse(json.dumps({'error': 503}))

    return HttpResponse(products_json)

def add_stock(request):
    if request.method == 'POST':
        pass

