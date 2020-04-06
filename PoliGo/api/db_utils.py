from api.serializers import ProductSerializer, ProductDeserializer
from api.http_codes import Error422
from store.models import Product
import json

import store

def check_stock(request):
    """
    Queries the database for the given GET request parameters.

    :param request: request from view function
    :type request: HttpRequest

    :returns: JSON containing the requested products or Error 422 if a requested product does not exist
    :rtype: str
    """

    get_options = dict(request.GET)

    # No other parameters except API-KEY were provided -> return all products
    if len(get_options) == 1:
        return json.dumps({'products': [ProductSerializer(query).data for query in Product.objects.all()]})
    
    queried_products = set()
    try:
        # Find products with the requested SKUs
        if 'SKU' in get_options:
            queried_products.update([item for SKU in get_options['SKU'] for item in Product.objects.filter(SKU = SKU)])

        # Find products with the requested names
        if 'name' in get_options:
            queried_products.update([item for name in get_options['name'] for item in Product.objects.filter(name = name)])
        
        # Find products with the requested categories
        if 'category' in get_options:
            queried_products.update([item for category in get_options['category'] for item in Product.objects.filter(category = category)])
        
        # Find products with the requested stocks
        if 'stock' in get_options:
            queried_products.update([item for stock in get_options['stock'] for item in Product.objects.filter(stock = stock)])
    except store.models.Product.DoesNotExist:
        return Error422('Wrong data')


    return json.dumps({'products': [ProductSerializer(query).data for query in queried_products]})


def add_stock(params):
    req_products = params['products']
    try:
        for prod in req_products:
            ProductDeserializer.deserialize(**prod).save()
    except:
        return None

    return True
