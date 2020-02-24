from django.contrib.auth.models import User
from store.models import Profile, Product
from rest_framework import serializers

class UserSerializer(serializers.ModelSerializer):
    class Meta:
        model = User
        fields = ['username']

class ProductSerializer(serializers.ModelSerializer):
    class Meta:
        model = Product
        fields = ['SKU', 'name', 'price', 'stock']

class ProductDeserializer:
    @staticmethod
    def deserialize(**kwargs):
        return Product(
            SKU = kwargs['SKU'],
            name = kwargs['name'],
            price = kwargs['price'],
            stock = kwargs['stock']
        )
