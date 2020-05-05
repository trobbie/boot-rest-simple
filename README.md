# boot-rest-simple

This Spring Boot project can be used as a starter project for a RESTful generic resource that does **not** use hypermedia (i.e. HATEOAS/HAL).  The responses are simple JSON formats of the resource.  Thus this Spring Boot project does **not** use spring-boot-starter-data-rest, which gives a more complete implementation using HAL-formatted responses.  

In particular, the following specific technologies are in use:
- Language: Java
- Package Manager: Gradle
- Version Control: Git
- IoC/Starters: Spring Boot (NOTE: **not** using spring-boot-starter-data-rest)
- Testing: JUnit and Mockito
- Code Coverage: JaCoCo
- IDE: Eclipse (but could use any IDE)

# REST API Implementation
Every REST implementation has its nuances.  I have tried to stick to best practices.  Below should identify the subjective distinctions of the implementation. 

| General  |  |
| ------------- |:-------------:|
| Root URI: | http://localhost:8080/v1 |
| Resource name: | resources |
| Representation format: | JSON (requests and responses) |

| HTTP Method  | URI (relative) | Implementation Distinctions  | Request  | Response  |
| ------------- |:-------------:|:-------------:|:------:|:------:|
| GET | /resources| Get all resources [**FUTURE TASK: add filtering, pagination, etc.**]) | Empty body | ***200 OK***: Array of resources |
| GET | /resources/:id | Get resource identified by id (Standard)| Empty body | ***200 OK***: Resource |
| PUT | /resources/:id | Replace resource identified by id | Resource | ***200 OK*** (on update) or ***201 CREATED*** (on insert): Same resource but some fields are allowed to have been changed server-side, e.g. dateModified |
| POST | /resources | Add a new resource | Resource (id assumed null for request) | ***201 CREATED*** (on success) |
| DELETE | /resources/:id | Delete resource identified by id (Standard)| Empty body | ***204 NO CONTENT*** (on sending delete request without error) or ***404 NOT FOUND*** (when id not found) |

## Getting Started

These instructions will get you a copy of the project up and running on your local machine for development and testing purposes. See deployment for notes on how to deploy the project on a live system.

### Prerequisites

You need the following installed locally:
- JDK 8
- Git
- Gradle

### Installing (as is, before customizing)

1) Fork or clone this repository.
```
git clone git@github.com:trobbie/boot-rest-simple.git
```
2) Run gradle build script in repository's directory.  This assembles and tests the project.
```
gradlew build
```
3) If using Eclipse, generate the Eclipse files before opening project from filesystem.
```
gradlew eclipse
```
4) To run the embedded Tomcat service and in-memory H2 database, run the following:
```
gradlew bootRun
```
To access/add data directly (without using the REST API) in this development environment, navigate to the following:
```
http://localhost:8080/h2-console
```
Ensure JDBC URL is "jdbc:h2:mem:resourcedb" before connecting.  Username:Password is sa:`<empty password>`
  
5) To access data through REST API, navigate to the resource URL:
```
http://localhost:8080/v1/resources
```

## Running the tests

To run the unit tests and code coverage:
```
gradlew check
```

## Deployment

To deploy from the archived JAR (instead of using ``` gradlew bootRun ```):
```
java -jar build/libs/boot-rest-simple-0.0.1-SNAPSHOT.jar
```

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot/) - Web Framework
* [Gradle](https://gradle.org/) - Dependency/Task Management

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/trobbie/boot-rest-simple/releases). 

## Authors

* **Trevor Robbie** - *Initial work* - [Github account](https://github.com/trobbie)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* All the resources out there on each technology.
