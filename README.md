# Aggregator Service

Aggregator Service has only one rest endpoints for aggregate model.
* GET /aggregation (get aggregate of all api)


* Response has been provided with ResponseEntity model.
* Get aggregate list api provides aggregate of shipment ,track and pricing api.

# Tech Stack

Made assumptions in the implementation explicit,
* As per requirement,getApi takes 3 parameters but all are set required= false.
* All the order number taken as Long number and countryCode taken as String.
* Creates a aggregate model for response. 
* All external APIs are called asynchronously with timeout of 5 second.

choices of tech stack are good but need to be communicated.
## Tech
* JAVA 18
* Springboot
* Maven
* github
## Reason:
* Java 18, the latest version to support all functionalities needed in the libraries;
* Springboot, to have an easy and quick environment ready to run our application;
* Maven, It is always good to have build tool like Maven or Gradle to automatize our daily routines (dependencies or tasks
  to be executed in build processes)
* Added docker file to run the latest image of aggregator service.
## Requirements:
* Maven 3.8.5
* Java/JDK 18

## Build & Run
To build this application we can easily call Maven to help us with the below command: ```mvn
clean install
``` To run the application we can use the below command: ``` mvn sprint-boot :run ```
``` This process will perform the below activities sequence:
  * 1 - compilation check in java;
  * 2 - tests;
## Functionality
Get Aggregate Service provide aggregate of 3 services. Each service return some list of Object for one orderNumber or 
countryCode. A map collection element has been used to store each key and value combination return by given external services.
A collection of these maps are stored in Aggreate Object.That is retrun as  API response.
All the external API has been called asynchronously and Future response has been collected .
All th external API has timeout of 5 second else We ignore that element from the given set of input list.  
```
####GET
```
GET - http://localhost:8080/aggregation?shipmentsOrderNumbers=987654321,123456789&trackOrderNumbers=123456789&pricingCountryCodes=NL,CN
```
###### Where:
http://localhost:8080/aggregation?shipmentsOrderNumbers=987654321,123456789&trackOrderNumbers=123456789&pricingCountryCodes=NL,CN
* Expected results are in:
* HttpStatusCode = 200, means request went well and also return aggregate response

http://localhost:8080/aggregation?shipmentsOrderNumbers=987654321,123456789
* HttpStatusCode = 200, means request went well and return only shipment details in aggregate response

http://localhost:8080/aggregation?trackOrderNumbers=123456789
* HttpStatusCode = 200, means request went well and return only track details in aggregate response

http://localhost:8080/aggregation?pricingCountryCodes=NL,CN
* HttpStatusCode = 200, means request went well and return only pricing details in aggregate response

http://localhost:8080/aggregation?
* HttpStatusCode = 200, means request went well and return empty aggregate response

## Improvements
* 1 - We could Add Swagger and code quality check with code
* 2 - We can also add a Jenkins/gitlab pipeline to automatize the job routines (Code Checkout/Build/Code Quality 
(checkstyle/sonar)/Vulnerability Check(Artifactory Creation/Deploy);
* 3 - We can also add interface layer for all external api handling , that i have mentioned in my design document.


