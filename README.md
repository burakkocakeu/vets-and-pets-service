# Vets and Pets Service

This is a sample Spring Boot application that provides a RESTful API for managing pets and veterinarians. The application includes a web-based user interface built using Angular.
Requirements

To build and run this application, you will need:

    Java 17 or later
    Apache Maven 3.6.0 or later
    PostgreSQL 15.2 or later
    Redis 4.0 or later
    Node.js 15.0 or later
    Angular CLI 15.0 or later

## Installation

To build and run the application, use the following steps:

    Clone the repository: git clone https://github.com/burakkocakeu/vets-and-pets-service.git
    Navigate to the project directory: cd vets-and-pets-service
    Open a new terminal window at this directory and run the command: docker compose up
    Build and start the Spring Boot application using Maven: mvn clean spring-boot:run
    Open a new terminal window and navigate to the dashboard-ui directory: cd dashboard-ui
    Install the required dependencies: npm install
    Start the Angular development server: ng serve --open

## Configuration

The Spring Boot application can be configured by modifying the application.properties file in the src/main/resources directory. Here are some of the configuration properties that you can modify:

    spring.datasource.url: the JDBC URL of the PostgreSQL database
    spring.datasource.username: the username for accessing the PostgreSQL database
    spring.datasource.password: the password for accessing the PostgreSQL database
    spring.redis.host: the hostname of the Redis server
    spring.redis.port: the port number of the Redis server
    jwt.secret: the secret key used for JWT token generation

## API Documentation

The Spring Boot application provides a RESTful API for managing pets and veterinarians. The API postman collection is available at `Vets and Pets APIs.postman_collection.json` and can be used once the application is running.

## User Interface

The user interface is built using Angular and can be accessed at http://localhost:4200 once the Angular development server is running.
**Default admin user credentials**: `admin/admin`

## Testing

The Spring Boot application includes unit and integration tests that can be run using the following command:

`mvn test`

The Angular user interface includes unit tests that can be run using the following command:

`ng test`

## License

This project is licensed under the MIT License - see the LICENSE file for details.