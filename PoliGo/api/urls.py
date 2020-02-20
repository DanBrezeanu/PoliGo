from django.urls import path, include
from api import views


urlpatterns = [
    path('', views.Home),
    path('stocks/', views.stocks)
]
