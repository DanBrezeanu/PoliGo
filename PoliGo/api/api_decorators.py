from api.http_codes import *
import functools

from django.shortcuts import render, HttpResponse
from api.api_utils import key_to_user, json_from_request

def is_staff(func):
    """
    Function decorator that only allows the function execution only if user.is_staff is True
    and the API key provided exists and is valid

    :returns: The functions return value or Error 403 JSON if access is not granted
    :rtype: HttpResponse
    """

    @functools.wraps(func)
    def wrapper_decorator(*args, **kwargs):
        # Get profile
        try:
            profile = key_to_user(json_from_request(args[0]))
        except:
            return HttpResponse(Error403('Unauthorized'))

        # Acces Denied
        if profile is None or not profile.user.is_staff:
            return HttpResponse(Error403('Unauthorized'))

        return func(*args, **kwargs)

    return wrapper_decorator

def is_user(func):
    @functools.wraps(func)
    def wrapper_decorator(*args, **kwargs):
        # Get profile
        try:
           profile = key_to_user(json_from_request(args[0]))
        except:
            return HttpResponse(Error401('No such user'))

        # No user with such key
        if profile is None:
            return HttpResponse(Error401('No such user'))

        return func(*args, **kwargs)

    return wrapper_decorator


def get(func):
    @functools.wraps(func)
    def wrapper_decorator(*args, **kwargs):
        try:
            if args[0].method == 'GET':
                return func(*args, **kwargs)
            else:
                return HttpResponse(Error503('Only GET requests accepted'))
        except:
            return HttpResponse(Error503('Only GET requests accepted'))

    return wrapper_decorator


def post(func):
    @functools.wraps(func)
    def wrapper_decorator(*args, **kwargs):
        try:
            if args[0].method == 'POST':
                return func(*args, **kwargs)
            else:
                return HttpResponse(Error503('Only POST requests accepted'))
        except:
            return HttpResponse(Error503('Only POST requests accepted'))

    return wrapper_decorator