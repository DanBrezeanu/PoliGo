from store.models import Profile
import json

def key_to_user(req_json):
    """
    Returns Profile associated to the api_key provided in request.
    :param request: request from view function
    :type request: HttpRequest
    :returns: Profile with api_key equal to the one provided or None if not found
    :rtype: Profile/None
    """
    
    # Get API-KEY from request
    try:    
        api_key = req_json['api_key']
    except: # No API-KEY provided
        return None

    # Find profile matching the API-KEY
    try:    
        return Profile.objects.get(api_key = api_key)
    except Profile.DoesNotExist:
        return None
        

def json_from_request(request):
    # try:
        print(request.body)
        return json.loads(request.body.decode('utf-8'))
    # except:
    #     return None