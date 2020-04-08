from api.serializers import *
from api.http_codes import Error422, OK200
from api import views
from store.models import *
import json

import store

# pylint: disable=no-member

def check_stock(params):
    """
    Queries the database for the given GET request parameters.

    :param request: request from view function
    :type request: HttpRequest

    :returns: JSON containing the requested WarehouseItems or Error 422 if a requested product does not exist
    :rtype: str
    """

    # No other parameters except API-KEY were provided -> return all products
    if len(params) == 1:
        return json.dumps({'products': [WarehouseItemSerializer(query).data for query in WarehouseItem.objects.all()]})
    
    queried_products = set()
    try:
        # Find products with the requested SKUs
        if 'SKU' in params:
            queried_products.update([item for SKU in params['SKU'] for item in WarehouseItem.objects.filter(item__SKU = SKU)])

        # Find products with the requested names
        if 'name' in params:
            queried_products.update([item for name in params['name'] for item in WarehouseItem.objects.filter(item__name = name)])

        # Find products with the requested categories
        if 'category' in params:
            queried_products.update([item for category in params['category'] for item in WarehouseItem.objects.filter(item__category = category)])
        
        # Find products with the requested stocks
        if 'stock' in params:
            queried_products.update([item for stock in params['stock'] for item in WarehouseItem.objects.filter(stock = stock)])
    except WarehouseItem.DoesNotExist:
        return Error422('Wrong data')


    return json.dumps({'products': [ProductSerializer(query).data for query in queried_products]})


def add_stock(params):
    req_products = params['products']

    for prod in req_products:
        try:
            item = Item.objects.get(SKU=prod['SKU'])
        except Item.DoesNotExist:
            return Error422('No item with provided SKU, item number {}'.format(req_products.index(prod)))

        try:
            warehouse_item = WarehouseItem.objects.get(item=item)
        except WarehouseItem.DoesNotExist:
            return Error422('No warehouse item for provided item, item number {}'.format(req_products.index(prod)))

        try:
            warehouse_item.stock += int(prod['stock'])
        except KeyError:
            return Error422('No stock provided, item number {}'.format(req_products.index(prod)))

        warehouse_item.save()
        
    return OK200(None)

def remove_stock(params):
    req_products = params['products']

    for prod in req_products:
        try:
            item = Item.objects.get(SKU=prod['SKU'])
        except Item.DoesNotExist:
            return Error422('No item with provided SKU, item number {}'.format(req_products.index(prod)))

        try:
            warehouse_item = WarehouseItem.objects.get(item=item)
        except WarehouseItem.DoesNotExist:
            return Error422('No warehouse item for provided item, item number {}'.format(req_products.index(prod)))

        if warehouse_item.stock < int(prod['stock']):
            return Error422('Available stock smaller than requested removal, item number {}'.format(req_products.index(prod)))

        try:
            warehouse_item.stock -= int(prod['stock'])
        except KeyError:
            return Error422('No stock provided, item number {}'.format(req_products.index(prod)))

        warehouse_item.save()

    return OK200(None)

def add_product(params):
    req_products = params['products']

    for prod in req_products:
        print(prod)
        # Find item with given SKU
        try:
            item = list(Item.objects.filter(SKU=prod['SKU']))
        except KeyError:
            return Error422('No SKU provided, item number {}'.format(req_products.index(prod)))

        # If it doesn't exist create it
        if len(item) == 0:
            try:
                item = ItemDeserializer.deserialize(**prod)
                item.save()
            except:
                return Error422('Wrong item data, item number {}'.format(req_products.index(prod)))
        else:
            item = item[0]

        try:
            WarehouseItem.objects.get(item=item)
            continue   # if wh_item exists, skip it
        except WarehouseItem.DoesNotExist:
            pass
            
        # If it doesn't exist, create it
        try:
            warehouse_item = WarehouseItemDeserializer.deserialize(item=item, stock=0)
        except:
            return Error422('Wrong data, item number {}'.format(req_products.index(prod)))

        warehouse_item.save()

    return OK200(None)

def shopping_cart(params, user):
    try:
        cart = ShoppingCart.objects.get(customer=user, active=True)
    except ShoppingCart.DoesNotExist:
        return OK200(json.dumps({'products': []}))

    products = [ProductSerializer(query).data for query in cart.products.all()]

    return OK200( 
        json.dumps({'products': products, 'total_sum': cart.totalCost})
    )

def add_to_cart(params, user):

    # Necessary data must be available
    if 'SKU' not in params or 'quantity' not in params:
        return Error422('Wrong data')

    # Get the base item to add
    try:
        item = Item.objects.get(SKU=params['SKU'])
    except Item.DoesNotExist:
        return Error422('Product with given SKU does not exist')

    quantity = params['quantity']

    # Get the shopping cart, create one if none exist
    try:
        shopping_cart = ShoppingCart.objects.get(customer=user, active=True)
    except ShoppingCart.DoesNotExist:
        shopping_cart = ShoppingCart.objects.create(customer=user)

    # Find out whether the product already exists in the cart, if not, create one
    product = list(shopping_cart.products.filter(item=item))

    if len(product) == 0:
        product = Product.objects.create(item=item, quantity=quantity)
    else:
        product = product[0]
        product.quantity += quantity

    product.save()

    shopping_cart.products.add(product)
    shopping_cart.totalCost += item.price * quantity
    shopping_cart.save()

    return OK200(
        json.dumps(ProductSerializer(product).data)
    )

    