from django.contrib.auth.models import User
from store.models import *
from rest_framework import serializers

"""

Serializers

"""

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['username']

class ProfileSerializer(serializers.ModelSerializer):
    class Meta:
        model = Profile
        fields = ['api_key', 'name', 'email']  

class ItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields = ['SKU', 'name', 'price']

class WarehouseItemSerializer(serializers.ModelSerializer):
    class Meta:
        model = WarehouseItem
        fields = ['item', 'stock']

class ProductSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields = ['SKU', 'name', 'price', 'quantity']

class BankCardSerializer(serializers.ModelSerializer):
    class Meta:
        model = BankCard
        fields = ['cardNumber', 'cardHolder', 'cardMonthExpire', 'cardYearExpire', 'cardCVV', 'cardCompany']

class ShoppingCartSerializer(serializers.ModelSerializer):
    products = ProductSerializer(many=True)
    class Meta:
        model = ShoppingCart
        fields = ['ID', 'totalCost', 'date', 'products']

"""

Deseralizers

""" 

class UserDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return User(
            username = kwargs['username']
        )

class ProfileDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return Profile(
            ID = kwargs['ID'],
            name = kwargs['name'],
            api_key = kwargs['api_key']
        )

class ItemDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return Item(
            SKU = kwargs['SKU'],
            name = kwargs['name'],
            price = kwargs['price']
        )

class ProductDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return Product(
            item = kwargs['item'],
            quantity = kwargs['quantity'],
        )

class WarehouseItemDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return WarehouseItem(
            item = kwargs['item'],
            stock = kwargs['stock'],
        )

class ShoppingCartDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return ShoppingCart(
            ID = kwargs['ID'],
            totalCost = kwargs['totalCost'],
            active = kwargs['active'],
            products = kwargs['products'],
            customer = kwargs['customer'],
            shoppingHistory = kwargs['shoppingHistory']
        )

class BankCardDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return BankCard(
            cardNumber = kwargs['cardNumber'],
            cardHolder = kwargs['cardHolder'],
            cardMonthExpire = kwargs['cardMonthExpire'],
            cardYearExpire = kwargs['cardYearExpire'],
            cardCVV = kwargs['cardCVV'],
            cardCompany = kwargs['cardCompany'],
            profile = kwargs['user']
        )
