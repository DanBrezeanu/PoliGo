## PoliGo - Be the first in line

PoliGo is an Android application that lets you scan the items you want to buy from the store without having to spend your time in queues at the register.

A successful visit to the *PoliGo* shop consists of the following simple steps:
 * Login into your account (or create one if you don't have it)
 * Scan the items you want and just place them in your bag
 * Select the quantity for the scanned products (or remove them if you have changed your mind)
 * Proceed to checkout and select your desired payment choice
 * That's it! After the payment's confirmation you will receive a QR code so you can exit the shop
 
 
### Technical Details

The repository conists of the two main components:
 * the Android application
 * the server hosting the database and a REST API for ease of communcation
 
The Android application is esentially the client calling the API methods asynchronously, while the server receives the client's requests and data, parses them, checks them against
illegal actions and provides the answer back to the Android client. Little to no processing is done on the Android side, to ensure the application run as smooth as possible.

Of course, relying on a server is not always the best idea, unless the server and the API are designed carefully to not only provide security but also top performance.
The server is built using `Django`, a web server framework for Python. Each endpoint request is carefully checked against invalid or malicious data to ensure the database's security.

Although security is in place, users have a lot of features to explore while shopping:
 * Scanning the barcodes is extremly fast, even with old cameras
 * Items in the shopping cart can be easily recognized thanks to short names and products' photos
 * Buying more identical items? Just set the desired quantity
 * Past shopping sessions are stored and made available so one can easily remember what they've bought in the past
 * Multiple payment methods can be stored to a user's account in order to make the checkout as fast as possible
 * Total cost always available in the shopping list so users can manage their spendings
 
The application's color theme is orage/white and the UI/UX is carefully maintained throughout the application so even unexperienced users can find the app very easy to use.

Screenshots of the Android application are available [here](https://github.com/DanBrezeanu/PoliGo/tree/master/screenshots).
