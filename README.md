### Server-side
---

##### Prima data:
 ``` bash 
 cd PoliGo
 
 # instaleaza pachetul pentru virtual environment
 pip3 install venv

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
 python3 runserver 0.0.0.0:8000
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