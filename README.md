# Aggregator Service

Aggregator Service has only one rest endpoints for aggregate model.
* GET /aggregation (get aggreate of all api)


* Response has been provided with ResponseEntity model.
* Get stock list api provides full list if no paging information added in url.
* In case of huge data, GET /api/stocks (get a list of stocks) can also be used with pageNo and
  PageSize. For Example: /api/stocks?pageNo=1&pageSize=4

# Tech Stack

Made assumptions in the implementation explicit,
* As per requirement, name String , Null value not allowed 
* currentPrice double , Only this field will be updated with patch API. 
* lastUpdated date, Updating this from backend with current date. 
* Need Postgres image to run database on docker. below command can be used:
* ````
  docker pull postgres
  ````
#### Run docker compose file that is available in resources/docker folder to create a database and inserting some stock entries** .

choices of tech stack are good but need to be communicated.
## Tech
* JAVA 18
* Postgres Database / Spring JPA / Liquibase
* Springboot
* Swagger
* Checkstyle / Jacoco
* Maven
* github
## Reason:
* Java 18, the latest version to support all functionalities needed in the libraries;
* Springboot, to have an easy and quick environment ready to run our application;
* Checkstyle / Jupiter, so have good quality in tests, code and reports (checkstyle configured to not allow builds that fails in code standards)
* Maven, since is always good to have build tool like Maven or Gradle to automatize our daily routines (dependencies or tasks
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
  * 2 - checkstyle check;
  * 3 - tests;
You can access the API documentation via Swagger in:
* [Swagger API Documentation](http://localhost:8086/swagger-ui/) 
## Functionality
This microservice provide support to 5 different endpoints #### STORE Side Content
```
####GET
```
GET - http://localhost:8086/api/stocks/{id}
```
###### Where:
* {id} is a Long value for unique stock ID;
  This API return specific stock details for given ID.
http://localhost:8086/api/stocks/1
* Expected results are in:
* HttpStatusCode = 200, means request went well and also return stock detils
* HttpStatusCode = 400, means bad request, will also return the exception message;
* HttpStatusCode = 404, means no record found, will also return the exception message; [Feel free to get from Swagger as well](http://localhost:8080/swagger-ui/) 


## Improvements
* 1 - To scale and have HA (High Availability), we can deploy this microservice using Kubernetes with multiple pods 
 strategy;
* 2 - We can also add a Jenkins/gitlab pipeline to automatize the job routines (Code Checkout/Build/Code Quality 
(checkstyle/sonar)/Vulnerability Check(Artifactory Creation/Deploy);
* 3 - Add some async processing for large number of request at the same microservice (not implemented, as per requirement), maybe an extra layer with some load balancer that will handle the number of 
 requests/capacity per instance(pod in a kubernetes environment);


