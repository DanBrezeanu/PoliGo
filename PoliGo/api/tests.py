from django.test import TestCase
from store.models import *
from django.contrib.auth.models import User

from rest_framework.test import APITestCase
from rest_framework import status

import pprint
import requests
import json


def json_from_req(response):
    return json.loads(response.content)

class TestRegistration(APITestCase):
    def test_register_user(self):
        my_name = "Brezeanu Dan"
        my_email = "dan.brezeanu@gmail.com"
        my_pass = "parolaPoliGo"

        data = {
                'username': my_name,
                'email': my_email,
                'password': my_pass
            }

        response = self.client.post('/api/v1/signup/', json.dumps(data), content_type='application/json')
        js = json_from_req(response)

        self.assertEqual(response.status_code, 200, "Request failed")
        self.assertEqual(js['code'], 200, "Req error")

class TestAPICustomer(APITestCase):
    def setUp(self):
        my_name = "Brezeanu Dan"
        my_email = "dan.brezeanu@gmail.com"
        my_pass = "parolaPoliGo"

        data = {
                'username': my_name,
                'email': my_email,
                'password': my_pass
            }

        response = self.client.post('/api/v1/signup/', json.dumps(data), content_type='application/json')
        js = json_from_req(response)
        self.api_key = js['api_key']


        response = self.client.post(
            '/api/v1/addstocks',
            json.dumps(
                { 
                    'api_key': self.api_key,
                    'products': [{'SKU': '59474982', 'name': 'Heets Bronze', 'price': 17.00, 'stock': 10}]
                }
            ),
            content_type='application/json'
        )


    def test_add_card(self):
        data = {
            'api_key': self.api_key,
            'card_details': {
                'cardNumber': '5232123314447733',
                'cardHolder': 'Brezeanu Dan',
                'cardMonthExpire': '12',
                'cardYearExpire': '24',
                'cardCVV': '552',
                'cardCompany': 'mastercard'
            }
        }

        response = self.client.post('/api/v1/addcard/', json.dumps(data), content_type='application/json')

        js = json_from_req(response)

        self.assertEqual(response.status_code, 200, "Request failed")
        self.assertEqual(js['code'], 200, "Req error")

   
    
    def test_add_to_card(self):
        data = {'api_key': self.api_key, 'SKU': '59474982', 'quantity': 2}

        response = self.client.post('/api/v1/addtocart/', json.dumps(data), content_type='application/json')

        self.assertEqual(response.status_code, 200, "Request failed")
        self.assertEqual(js['code'], 200, "Req error")