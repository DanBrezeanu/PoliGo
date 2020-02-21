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
    try:
        api_key = request.GET['api_key']
    except:
        return None

    try:
        return Profile.objects.get(api_key = api_key)
    except store.models.Profile.DoesNotExist:
        return None

        
def check_stock(request):
    profile = key_to_user(request)
    
    if profile is None or not profile.user.is_staff:
        return HttpResponse(json.dumps({'error': 403}))

    if request.method == 'GET':
        products_json = db_utils.check_stock(request)
    else:
        return HttpResponse({'error': 403})

    return HttpResponse(products_json)

def add_stock(request):
    if request.method == 'POST':
        pass

