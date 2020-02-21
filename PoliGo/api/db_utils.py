from api.serializers import ProductSerializer
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
        return json.dumps([ProductSerializer(query).data for query in Product.objects.all()])
    
    queried_products = set()
    try:
        # Find products with the requested SKUs
        if 'SKU' in get_options:
            queried_products.update([Product.objects.get(SKU = sku) for sku in get_options['SKU']])

        # Find products with the requested names
        if 'name' in get_options:
            queried_products.update([Product.objects.get(name = name) for name in get_options['name']])
        
        # Find products with the requested categories
        if 'category' in get_options:
            queried_products.update([Product.objects.get(category = category) for category in get_options['category']])
        
        # Find products with the requested stocks
        if 'stock' in get_options:
            queried_products.update([Product.objects.get(stock = stock) for stock in get_options['stock']])
    except store.models.Product.DoesNotExist:
        return json.dumps('Error 422')


    return json.dumps([ProductSerializer(query).data for query in queried_products])