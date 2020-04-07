from django.contrib.auth.models import User
from store.models import Profile, Product, BankCard, ShoppingCart, ShoppingHistory
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
        fields = ['ID', 'name', 'api_key']  

class ProductSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields = ['SKU', 'name', 'price', 'stock']

class ShoppingCartSerializer(serializers.ModelSerializer):
    class Meta:
        model = ShoppingCart
        fields = ['ID', 'totalCost', 'active', 'products', 'customer', 'shoppingHistory']

class BankCardSerializer(serializers.ModelSerializer):
    class Meta:
        model = BankCard
        fields = ['cardNumber', 'cardHolder', 'cardMonthExpire', 'cardYearExpire', 'cardCVV', 'cardCompany']

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

class ProductDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return Product(
            SKU = kwargs['SKU'],
            name = kwargs['name'],
            price = kwargs['price'],
            stock = kwargs['stock']
        )

class ShoppingCartDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return ShoppingCart(
            ID = kwargs['ID'],
            totalCost = kwargs['totalCost'],
            active = kwargs['active'],
            products = kwargs['customer'],
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
