### Server-side
---

##### Environment prequisites:
 ``` bash 
 cd PoliGo
 
 # install python virtual environment package
 pip3 install python3-venv

 # create a new environment
 python3 -m venv env

 # activate the environment
 source env/bin/activate

 # install the requirements
 pip3 install -r requirements.txt

 # create the database migrations
 python3 manage.py makemigrations api store

 # apply the migrations
 python3 manage.py migrate

 # run the development server
 python3 manage.py runserver 0.0.0.0:8000
 ```


 ###### In general, server-ul poate sa fie lasat sa mearga. Isi va da reload la fiecare ctrl+S pe un fisier dintr-un app.
 

 
 ### Android app prequisites
 ---
 
 ##### Download [Android Studio](https://developer.android.com/studio)
 ##### Open Project -> path/to/cloned/repo/PoliGo-Android -> OK
 ##### In case of errors regarding license agreement (Windows) 
 > ##### Open File Explorer
 >
 > ##### Go to C:\Users\ \<YOUR_USER>\AppData\Local\Android\Sdk\tools\bin
 >
 > ##### Open command prompt to this location
 >
 > ##### Run `sdkmanager.bat --licenses` in command prompt 
 
 ##### Create new configuration by clicking "Edit Configuration" in the top bar
 > ##### Click on the add sign
 > ##### Module -> App
 > ##### Deploy -> Default APK
 > ##### Launch -> Default Activity
 > ##### OK
 
 ##### File -> Project Structure -> Modules -> app -> Properties
 > ###### Compile SDK Version -> 29
 > ##### Source compatibility -> 1.8
 > ##### Target compatibility -> 1.8
 > ##### OK
 
 
 ##### Connect your phone (USB Debugging must be enabled)
 ##### Run application
