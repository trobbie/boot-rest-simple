# micro-rest-resource

This starter project is an example of how to provide a RESTful microservice for a generic resource using the following specific technologies:
- Language: Java
- Package Manager: Gradle
- Version Control: Git
- IoC: Spring Boot
- Testing: JUnit and Mockito
- *DevTools: CircleCI [**FUTURE TASK**]*
- *Authenticity and security: HTTPS and OAuth2 [**FUTURE TASK**]*
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
git clone git@github.com:trobbie/micro-rest-resource.git
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
To access/add data directly (without using the REST API), navigate to the following:
```
http://localhost:8080/h2-console
```
Ensure JDBC URL is "jdbc:h2:mem:resourcedb" before connecting.
5) To access data through REST API, navigate to the resource URL:
```
http://localhost:8080/resources
```

## Running the tests

To run the unit tests:
```
gradlew test
```

### Break down into end to end tests

TODO: Explain what these tests test and why

```
Give an example
```

### And coding style tests

TODO: Explain what these tests test and why

```
Give an example
```

## Deployment

TODO: Add additional notes about how to deploy this on a live system

## Built With

* [Spring Boot](https://spring.io/projects/spring-boot/) - The web framework used
* [Gradle](https://gradle.org/) - Dependency Management

## Versioning

We use [SemVer](http://semver.org/) for versioning. For the versions available, see the [tags on this repository](https://github.com/trobbie/micro-rest-resource/releases). 

## Authors

* **Trevor Robbie** - *Initial work* - [Github account](https://github.com/trobbie)

## License

This project is licensed under the MIT License - see the [LICENSE.md](LICENSE.md) file for details

## Acknowledgments

* All the resources out there on each technology.
