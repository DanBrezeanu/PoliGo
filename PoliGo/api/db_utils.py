from api.serializers import ProductSerializer
from store.models import Product
import json

import store

def check_stock(request):
    get_options = dict(request.GET)


    if len(get_options) == 1:
        return json.dumps([ProductSerializer(query).data for query in Product.objects.all()])
    
    queried_products = set()

    try:
        if 'SKU' in get_options:
            queried_products.update([Product.objects.get(SKU = sku) for sku in get_options['SKU']])

        if 'name' in get_options:
            queried_products.update([Product.objects.get(name = name) for name in get_options['name']])
        
        if 'category' in get_options:
            queried_products.update([Product.objects.get(category = category) for category in get_options['category']])
        
        if 'stock' in get_options:
            queried_products.update([Product.objects.get(stock = stock) for stock in get_options['stock']])
    except store.models.Product.DoesNotExist:
        return json.dumps('Error 422')


    return json.dumps([ProductSerializer(query).data for query in queried_products])