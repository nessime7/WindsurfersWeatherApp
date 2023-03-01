# Windsurfer's Weather App

## Description:
App will help you find the best place for Windsurfing around the whole world. 
For example in places:
1. Jastarnia (Poland)
2. Bridgetown (Barbados)
3. Fortaleza (Brazil)
4. Pissouri (Cyprus)
5. Le Morne (Mauritius)
If there are no available locations - application will return information:
"No matching city found."

## How to run app
Key points:
1. Java 11
2. Gradle - build automation system, optionally there is also Gradle wrapper
3. Spring Boot
4. REST API

To build project, please use:
```
gradle build
```

To execute tests:
```
gradle test
```

To run program after build:
```
gradle run
```
To run the jar file after build:
```
java -jar build/libs/Windsurfers-Weather-App-Sara-0.0.1-SNAPSHOT.jar
```
After execution of above command, the service will start at port 8080.

Date format: yyyy-mm-dd

Check files:
```
./src/main/resources/schema.sql
./src/main/resources/data.sql
```