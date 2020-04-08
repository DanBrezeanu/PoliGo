from django.contrib import admin
from store.models import *


admin.site.register([ShoppingHistory, ShoppingCart, Product, Profile, BankCard, Item, WarehouseItem])
