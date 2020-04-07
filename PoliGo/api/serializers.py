from django.contrib.auth.models import User
from store.models import BankCard, Product, Profile, ShoppingCart
from rest_framework import serializers

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['username']

class ProductSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields = ['SKU', 'name', 'price', 'stock']

class BankCardSerializer(serializers.ModelSerializer):
    class Meta:
        model = BankCard
        fields = ['cardNumber', 'cardHolder', 'cardMonthExpire', 'cardYearExpire', 'cardCVV', 'cardCompany']

class ShoppingCartSerializer(serializers.ModelSerializer):
    class Meta:
        model = ShoppingCart
        fields = ['ID', 'totalCost', 'date', 'products']

class ProductDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return Product(
            SKU = kwargs['SKU'],
            name = kwargs['name'],
            price = kwargs['price'],
            stock = kwargs['stock']
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
