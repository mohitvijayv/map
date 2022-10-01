[![Hits](https://hits.sh/github.com/mohitvijayv/map.svg)](https://hits.sh/github.com/mohitvijayv/map/)

https://img.shields.io/tokei/lines/github/mohitvijayv/map

# Run Application

## 1. Make sure your java_home is pointing to java 8
## 2. Execute the below script to compile the code into jar file and the running the jar file
```
mvn clean install
java -jar target/map-1.0-SNAPSHOT.jar
```
## 3. now our application is exposed to 8080 port of our local machine

# Request-Response

|Resource URL|GET|
|------------|-----|
|/api/directions/equidistant/?origin={origin-coordinates}&destination={destination-coordinates}| we have to pass starting and ending map location as query params then it returns array containing coordinates on route between origin and destination, each coordinate will be seperated by 50m

## Example
### Request
GET http://localhost:8080/api/directions/equidistant?origin=12.92662,77.63696&destination=12.93175,77.62872
### Response
[ 
12.92658,77.63716
12.926133846150734,77.63706769225654
12.926098571436787,77.63672428571462
12.92574108108201,77.63655351340161
12.925434529155535,77.6363158487726
12.925363776274073,77.63585987333103
12.92529302259727,77.63540389812076
12.925222268134618,77.63494792320289
12.92515151288085,77.63449194854347
12.925080756835976,77.6340359741425
12.92501,77.63358
12.925459494932282,77.63352949504107
12.925909026548839,77.63347911504783
12.926360353996698,77.63343486735687
12.926811681433337,77.63339061950627
12.927261254922337,77.63334721594138
12.92771027452821,77.63330407888778
12.928159294203144,77.63326094167162
12.928608313779195,77.63321780430911
12.929057333348165,77.63317466679139
12.929509062501712,77.63312906253483
12.929760967820537,77.63292596767516
12.929583548392193,77.63249854837153
12.929447058833432,77.63205294117178
12.929493521326524,77.63159211274248
12.929574629871075,77.63113574085965
12.929676481915823,77.63068203722318
12.92977906794646,77.63022915262059
12.929885000534593,77.62978000026546
12.929990932356572,77.62933084752173
12.930364750003994,77.62910800001437
12.93077833335361,77.62891305564752
12.931195000014071,77.62873250006838
12.931610212778757,77.628542978752
12.93166,77.62852
 ]

 # Project Overview

 ## Logic
 we traverse along the polyline and find nearest two ponints wrt 50m interval. if point lie exactly on 50m interval then we pass it to equisdistant array, otherwise we take these two nearest points to 50m interval and by using interpolation find point which lies on 50m interval.

## Design

 ### Tech stack used
 1. Springboot to expose APIs
 2. Java 8

 ### Project Structure
 1. Controller package is created to handle web request.
 2. Service package handles the business logic.
 3. Model package is created to contain application data.
 4. Constants class holds all constants used in project.

 #### Controller
 1. DirectionController handles the request to find directions between two points

 #### Service
 1. HTTPClientService handles the httpcalls.
 2. DirectionService handles the logic to calculate lat lng each seperated by 50m on the route. It first hits the http request using HTTPClient to fetch direction response from map api, then it extracts polyline from it. polyline is decoded by PolylineService, these decoded points then sent to Geometry Service to get equidistant points.
 3. PolylineService handles the encoded polyline to get point along polyline in latlng format.
 4. GeometryService helps in interpolation and calculating distance between two points.


