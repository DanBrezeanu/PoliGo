from django.shortcuts import render, HttpResponse
from django.core.exceptions import PermissionDenied

from api.serializers import UserSerializer, ProductSerializer

from store.models import Product
import json

def Home(request):
    print(UserSerializer(request.user).data)
    return HttpResponse('ok')


def stocks(request):
    if not request.user.is_staff:
        raise PermissionDenied
    
    if request.method == 'GET':
        return HttpResponse(
                json.dumps([ProductSerializer(query).data for query in Product.objects.all()])
        )