# PokeApiClient
Test job for android developer vacancy. Mobile client for PokeApi service

Text document of the test job:
http://hr.sibers.com/Android-test.pdf

## Description of completed test job

During the test job execution, I implemented the following functionality of the android application:
- Show a list of of 30 pokemon on the main activity. Each element of the list contains the name of the pokemon, it's image and three main characteristics: attack, defense and healthpoints. All information, stored on server, is received by the application using the Retrofit 2 library. This library allows me to implement client-server interaction by defining an interface and using annotations. All headers, url parameters and request body passed to this interface as parameters and I don't need to make an http request manually. 

![Alt text](https://github.com/GARFILD1000/PokeApiClient/blob/master/screenshots/screenshot1.png?raw=true "screenshot")
![Alt text](https://github.com/GARFILD1000/PokeApiClient/blob/master/screenshots/screenshot3.png?raw=true "screenshot")

- Abitity to view detailed information about pokemon on additional activity, which can be accessed by clicking on this pokemon in the list. Detailed information of the pokemon contains a variety of characteristics: the type of pokemon, his experience, weight, height, speed, attack, defense and health. Also this activity views various images of this pokemon, sliding them to the side.

![Alt text](https://github.com/GARFILD1000/PokeApiClient/blob/master/screenshots/screenshot5.png?raw=true "screenshot")
![Alt text](https://github.com/GARFILD1000/PokeApiClient/blob/master/screenshots/screenshot6.png?raw=true "screenshot")
![Alt text](https://github.com/GARFILD1000/PokeApiClient/blob/master/screenshots/screenshot7.png?raw=true "screenshot")
![Alt text](https://github.com/GARFILD1000/PokeApiClient/blob/master/screenshots/screenshot8.png?raw=true "screenshot")

- Page-by-page loading of pokemon list items when scrolling the list. When you reach the end end of the list, the following 30 items are loaded. 30 items is the minimum size of this list. This list was implemented by using Recycler View component, that allows me to display a large number of list items (with complex views) without performance losses, as would be the case with regular List component.

- The "Reload" button on the toolbar. When you click that button, the pokemon list is re-inititialized from a random item from the PokeApi server's database. In this case, the cache stored in database on the device is cleared and filled with the new values, received from server.

- Three CheckBox'es in the toolbar of main activity, which controls the sorting parameters of the list: attack, defense, healthpoints. When the "Attack", "Defense" or "HP" CheckBox'es are activated, the pokemon list is sorted by the selected peremeters. Pokemon in the beginning of the list, the characteristics of which are better than all others, is hightlighted in red. Sorting is performed by total parameters of the selected characteristics.

![Alt text](https://github.com/GARFILD1000/PokeApiClient/blob/master/screenshots/sorting.png?raw=true "screenshot")
![Alt text](https://github.com/GARFILD1000/PokeApiClient/blob/master/screenshots/sorting2.png?raw=true "screenshot")

Of the additional tasks I implemented sorting the current list of pokemon by selected peremeters. 
Also, I implemented lazy caching of received from server data. For this I used the Android Room library, that allows me to create simple interface for all SQLite database operations. Then I just use object DAO, that creates library, and complete saving and restoring all data. Also, for retrieve data, stored in database, I use LiveData class, that provides ability to observe updates in data from database. This approach allows user to cache and access data even it is not possible to receieve it from the server (without internet connection). 
The Picasso library is responsible for receiving and caching images. It uses LRU memory (around 15% application RAM) cache and disk (amount of 50 MB) cache to store images.

Completed APK:
https://yadi.sk/d/uc3RVLxp_NSP2A
