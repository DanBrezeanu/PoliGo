from django.contrib import admin
from store.models import ShoppingCart, ShoppingHistory, Profile, Product, BankCard


admin.site.register([ShoppingHistory, ShoppingCart, Product, Profile, BankCard])
