from django.urls import path, include
from api import views


urlpatterns = [
    path('', views.Home),
    path('checkstocks/', views.check_stock),
    path('addstocks/', views.add_stock),
]
