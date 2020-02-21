from django.db import models
from django.contrib.auth.models import User

import random
import string

def generate_api_key():
    return ''.join(random.choices(string.digits + string.ascii_letters + '-', k=15))


class Profile(models.Model):
    ID = models.AutoField(auto_created=True, primary_key=True, serialize=True, verbose_name='ID')
    user = models.OneToOneField(User, on_delete=models.CASCADE)
    api_key = models.CharField(max_length=255, default=generate_api_key, null=False)

    def __str__(self):
        return self.user.username

class Product(models.Model):
    SKU = models.CharField(primary_key=True, max_length=255, null=False, unique=True, verbose_name='SKU')
    name = models.CharField(max_length=1000)
    price = models.FloatField()
    stock = models.IntegerField()

    def __str__(self):
        return self.name

class ShoppingHistory(models.Model):
    customer = models.ForeignKey(Profile, on_delete=models.CASCADE)

class ShoppingCart(models.Model):
    ID = models.AutoField(auto_created=True, primary_key=True, serialize=True, verbose_name='ID')
    totalCost = models.FloatField(default=0.0)
    active = models.BooleanField(default=True)

    products = models.ManyToManyField(Product)
    customer = models.ForeignKey(Profile, on_delete=models.CASCADE)
    shoppingHistory = models.ForeignKey(ShoppingHistory, on_delete=models.CASCADE, blank=True, null=True)



