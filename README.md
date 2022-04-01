# CovidCorrelationApp
CovidCorrelationApp is a simple app used to get the correlation coefficient between percentage of people died and got vaccinated of COVID-19 for all continents.

## How to run the app
1. Install java version from https://www.oracle.com/in/java/technologies/javase/jdk11-archive-downloads.html
2. Set the JAVA_HOME environment variable to point to the java bin folder
3. Either fork or download the app and open the folder in the cli
4. Run the backend spring boot app using 'java -jar .\target\covid-0.0.1-SNAPSHOT.jar' command. The backend application will be served at http://localhost:8080
5. Go to the app folder and open Index.html file using your browser

## How to use the app
1. Move the mouse over the continent for which you would like to know the current correlation coefficient (Using Pearson's r)
2. The value will be displayed below the map
3. The absolute magnitude of the observed correlation coefficient and their interpretation is provided in the app for your reference

## Third party libraries and API's used
- https://github.com/M-Media-Group/Covid-19-API
- https://simplemaps.com/world
- https://commons.apache.org/proper/commons-math/javadocs/api-3.3/

## What the app looks like
![alt text](https://github.com/ashleymani/CovidCorrelationApp/blob/main/CovidCorrelationApp.png)

