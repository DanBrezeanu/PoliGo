import json
from django.shortcuts import HttpResponse

# Unauthorized
def Error401(message = ""):
    return json.dumps({'code': 401, 'message': message})

# Forbidden
def Error403(message = ""):
    return json.dumps({'code': 403, 'message': message})

# Unprocessable entity
def Error422(message = ""):
    return json.dumps({'code': 422, 'message': message})

# Service unavailable
def Error503(message = ""):
    return json.dumps({'code': 503, 'message': message})

def OK200(return_json = None, message = ""):
    data = json.loads(return_json) if return_json is not None else {}
    data['code'] = 200
    data['message'] = message

    return json.dumps(data)