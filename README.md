# Customer Service

## Overview
This project implements a CRUD system for managing customer data and is built on the Spring Web MVC architecture.

## New Features
* Unit and Integration tests for validating the application

## Dependencies
* [Spring Web](https://docs.spring.io/spring-boot/docs/3.2.1/reference/htmlsingle/index.html#web): creates HTTP server and controller to handle requests.

* [Spring Validation](https://www.baeldung.com/spring-boot-bean-validation): Utilizes Hibernate Validator for validating user input.

* [Spring Data JPA](https://spring.io/projects/spring-data-jpa/): Implements Java Persistence API Repositories and a data access layer.

* [H2](https://www.h2database.com/html/main.html): In-memory database for development.

* [Spring Cloud OpenFeign](https://spring.io/projects/spring-cloud-openfeign/): Declarative REST Client.

* [Lombok](https://projectlombok.org/): Eliminates boilerplate code.

## Getting Started
1. **Clone the Repository:**
```bash
git clone https://github.com/pedrokmra/customer-service.git
cd customer-service
```

2. **Build the Project:**
```bash
./gradlew clean build
```

2. **Run the Application:**
```bash
cd build/libs
java -jar customer-service-1.0.0.jar program arguments: --spring.profiles.active=staging --server.port=8081
```