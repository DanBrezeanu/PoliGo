from django.urls import path, include
from api import views


urlpatterns = [
    path('', views.Home),
    path('checkstocks/', views.check_stock),
    path('addstocks/', views.add_stock),
    path('login/', views.login),
    path('signup/', views.register),
    path('shoppinghistory/', views.shopping_history),
    path('addcard/', views.add_card),
    path('addtocart/', views.add_to_cart)
]
