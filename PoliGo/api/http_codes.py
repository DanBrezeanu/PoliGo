import json
from django.shortcuts import HttpResponse


def Error403(message = ""):
    return json.dumps({'code': 403, 'message': message})

def Error422(message = ""):
    return json.dumps({'code': 422, 'message': message})

def Error503(message = ""):
    return json.dumps({'code': 503, 'message': message})

def OK200(return_json = None, message = ""):
    data = json.loads(return_json) if return_json is not None else {}
    data['code'] = 200
    data['message'] = message

    return json.dumps(data)