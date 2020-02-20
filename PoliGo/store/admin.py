from django.contrib import admin
from store.models import ShoppingCart, ShoppingHistory, Profile, Product


admin.site.register([ShoppingHistory, ShoppingCart, Product, Profile])
