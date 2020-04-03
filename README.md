### Server-side
---

##### Prima data:
 ``` bash 
 cd PoliGo
 
 # instaleaza pachetul pentru virtual environment
 pip3 install python3-venv

 # creeaza un virtual environment 
 python3 -m venv env

 # activeaza virtual environment-ul
 source env/bin/activate

 # instalaeaza pachetele necesare proiectului
 pip3 install -r requirements.txt

 # cauta modificarile in tabelele BD
 python3 manage.py makemigrations api store

 # aplica modificarile
 python3 manage.py migrate

 # ruleaza serverul pe localhost port 8000
 python3 manage.py runserver 0.0.0.0:8000
 ```

<span style="font-size:12px"> 
    Browser -> 127.0.0.1:8000 </br> si ar trebui sa mearga 
</span>


 ##### La fiecare modificare in **/models.py

 ``` bash
 python3 manage.py makemigrations api store
 python3 manage.py migrate
 python3 runserver 0.0.0.0:8000
 ```


 
 ###### In general, server-ul poate sa fie lasat sa mearga. Isi va da reload la fiecare ctrl+S pe un fisier dintr-un app.
 
 
 ### Testare endpoint-uri
 
 ###### Dupa ce ati scris o parte din functionalitatea endpoint-ului, e normal sa-l si testati. Dar cum?
 
 ```python
import requests
 
params = {'parametru_1': 12345,
          'parametru_2': 'un_string_frumi',
          'parametru_3': ['el_lista_1' 'el_lista_2']}
          
# GET Request
r = requests.get(url='http://127.0.0.1:8000/api/v1/<URL_ENDPOINT>', json=params)

# POST request
r = requests.post(url='http://127.0.0.1:8000/api/v1/<URL_ENDPOINT>', json=params)


print(r.text) # raspunsul de server
```

###### Si cum il preluati json-ul in Django?

```python
def your_endpoint_view(request):
    if request.method == 'POST':
        params = json.loads(request.body.decode('utf-8')))
    else request.method == 'GET':
        params = json.loads(request.body.decode('utf-8')))
        
    # Acum params e un dictionar cu ce ati trimis
    # Nu uitati sa puneti si 'api_key' printre campuri mereu
``` 
 
 
 ### Android app
 ---
 
 ##### Descarcati [Android Studio](https://developer.android.com/studio)
 ##### Open Project -> path/to/cloned/repo/LaBordel/PoliGo-Android -> OK
 ##### O sa descarce chestii, dureaza vreo 3-5 min
 > ##### In caz ca da erori legate de neacceptarea licentei
 >
 > ##### Deschideti File Explorer
 >
 > ##### Duceti-va in C:\Users\ \<USERUL VOSTRU>\AppData\Local\Android\Sdk\tools\bin
 >
 > ##### Scrieti `cmd` unde este scris path-ul din File Explorer (ala cu ...\Sdk\tools\bin) si dati Enter
 >
 > ##### `sdkmanager.bat --licenses` in command prompt si yes la toate
 
 
 ##### Pe bara de sus este dropdown-ul "Edit Configuration" -> Click
 > ##### Click pe plus-ul din stanga
 > ##### Module -> App
 > ##### Deploy -> Default APK
 > ##### Launch -> Default Activity
 > ##### OK
 
 ##### File (stanga sus) -> Project Structure -> Modules -> app -> Properties
 > ###### Compile SDK Version -> 29
 > ##### Source compatibility -> 1.8
 > ##### Target compatibility -> 1.8
 > ##### OK
 
 
 ##### Conectati telefon prin cablu la PC (dati accept-uri pe telefon)
 ##### Click pe triunghiul verde de langa numele device-uri
