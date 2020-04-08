from django.urls import path, include
from api import views


urlpatterns = [
    path('', views.Home),
    path('checkstock/', views.check_stock),
    path('addstock/', views.add_stock),
    path('removestock/', views.remove_stock),
    path('addproduct/', views.add_product),
    path('login/', views.login),
    path('signup/', views.register),
    path('shoppinghistory/', views.shopping_history),
    path('shoppingcart/', views.shopping_cart),
    path('removefromcart/', views.remove_from_cart),
    path('placeorder/', views.place_order),
    path('addcard/', views.add_card),
    path('addtocart/', views.add_to_cart)
]
