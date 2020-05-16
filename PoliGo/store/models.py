from django.db import models
from django.contrib.auth.models import User
from django.conf import settings

from django.db.models.signals import post_save
from django.dispatch import receiver

import random
import string
import os

from PIL import Image

def generate_api_key():
    return ''.join(random.choices(string.digits + string.ascii_letters + '-', k=15))


class Profile(models.Model):
    ID = models.AutoField(auto_created=True, primary_key=True, serialize=True, verbose_name='ID')
    user = models.OneToOneField(User, on_delete=models.CASCADE, null=False)
    api_key = models.CharField(max_length=255, default=generate_api_key, null=False)
    
    User._meta.get_field('email')._unique = True

    def __str__(self):
        return self.user.username

    @property
    def name(self):
        return self.user.username

    @property
    def email(self):
        return self.user.email

class Item(models.Model):
    SKU = models.CharField(primary_key=True, max_length=255, null=False, unique=True, verbose_name='SKU')
    name = models.CharField(max_length=1000)
    price = models.FloatField()
    image = models.ImageField(default='test.png')

    def __str__(self):
        return self.name

class Product(models.Model):
    item = models.ForeignKey(Item, null=False, on_delete=models.CASCADE)
    quantity = models.IntegerField(null=False, default=0)

    def __str__(self):
        return self.item.name

    @property
    def SKU(self):
        return self.item.SKU

    @property
    def name(self):
        return self.item.name

    @property
    def price(self):
        return self.item.price

class WarehouseItem(models.Model):
    item = models.ForeignKey(Item, null=False, on_delete=models.CASCADE)
    stock = models.IntegerField(null=False, default=0)

    def __str__(self):
        return self.item.name

class ShoppingHistory(models.Model):
    customer = models.ForeignKey(Profile, on_delete=models.CASCADE)

class ShoppingCart(models.Model):
    ID = models.AutoField(auto_created=True, primary_key=True, serialize=True, verbose_name='ID')
    totalCost = models.FloatField(default=0.0)
    active = models.BooleanField(default=True)
    date = models.DateField(auto_now=True)

    products = models.ManyToManyField(Product)
    customer = models.ForeignKey(Profile, on_delete=models.CASCADE)
    shoppingHistory = models.ForeignKey(ShoppingHistory, on_delete=models.CASCADE, blank=True, null=True)


class BankCard(models.Model):
    cardNumber = models.CharField(primary_key = True, max_length=20, null=False, verbose_name='cardNumber')
    cardHolder = models.CharField(max_length=255, null=False)
    cardMonthExpire = models.CharField(null=False, max_length=2)
    cardYearExpire = models.CharField(null=False, max_length=2)
    cardCVV = models.CharField(null=False, max_length=3)
    cardCompany = models.CharField(max_length=30, null=False)
    profile = models.ForeignKey(Profile, on_delete=models.CASCADE)





@receiver(post_save, sender=Item, dispatch_uid="rename_image")
def rename_image(sender, instance, **kwargs):
    image_name = str(instance.image)
    extension = image_name.split('.')[-1]

    image_path = os.path.join(settings.MEDIA_ROOT, image_name)

    if extension != 'png':
        Image.open(image_path).save(image_path.replace(extension, 'png'))
        image_name = image_name.replace(extension, 'png')

    new_image_name = '{}.{}'.format(instance.SKU, 'png')

    os.rename(os.path.join(settings.MEDIA_ROOT, image_name),
              os.path.join(settings.MEDIA_ROOT, new_image_name))



    Item.objects.filter(SKU=instance.SKU).update(image=new_image_name)
