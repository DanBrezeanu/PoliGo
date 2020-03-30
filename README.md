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
