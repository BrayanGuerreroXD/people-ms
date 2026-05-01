# People Microservice

Spring WebFlux API for handling CREATE, READ, and UPDATE operations on persons and authentication flow via Spring Security JWT token

## Technologies
- Spring WebFlux
- R2DBC
- Java 25
- Gradle
- MySQL
- OpenApi Swagger
- Apache Kafka

## Architecture

This project follows **Clean Architecture** principles as implemented in the Bancolombia scaffold. The architecture is organized into independent layers that facilitate maintenance and scalability.

### Project Structure (Based on Bancolombia Scaffold)

```
people-ms/
├── applications/                 # Application layer (entry points)
│   └── app-service/             # Main application service
│       ├── src/
│       │   ├── main/
│       │   │   ├── java/        # Main source code
│       │   │   └── resources/   # Configuration resources
│       │   └── test/            # Tests
│       └── build.gradle         # Gradle configuration for the service
├── domain/                      # Domain layer (pure business)
│   ├── model/                   # Entities and domain models
│   └── usecase/                 # Application use cases
├── infrastructure/              # Infrastructure layer (technical details)
│   ├── driven-adapters/         # Adapters to external systems
│   │   └── r2dbc-repository/    # Reactive repository adapter
│   └── entry-points/            # Application entry points
│       └── reactive-web/        # Reactive web adapter (WebFlux)
├── deployment/                  # Deployment configurations
├── build.gradle                 # Root Gradle configuration
├── settings.gradle              # Multi-project configuration
└── README.md                    # This file
```

### Layer Details

1. **Domain**: Contains pure business logic, independent of frameworks and technologies.
   - `model`: Entities representing business concepts
   - `usecase`: Implementation of use cases that orchestrate application logic

2. **Infrastructure**: Technical implementation details.
   - `driven-adapters`: Adapters that allow the domain to communicate with the outside world (databases, external services)
   - `entry-points`: System entry points (APIs, message queues, etc.)

3. **Applications**: Specific configuration for each service/application.
   - Contains the main class with the `main` method
   - Configures beans and dependencies specific to the service

## Configuration

Example configuration in `applications/app-service/src/main/resources/application.yml`:

```yaml
server:
  port: 7500
spring:
  application:
    name: "ms-people"
  devtools:
    add-properties: false
  r2dbc:
    url: r2dbc:mysql://localhost:3306/db_people
    username: root
    password: 1234
  flyway:
    url: jdbc:mysql://localhost:3306/db_people
    user: root
    password: 1234
    locations: classpath:db/migration
  profiles:
    include: null
management:
  endpoints:
    web:
      exposure:
        include: "health,prometheus"
  endpoint:
    health:
      probes:
        enabled: true
jwt:
  secret: {secret-key}
  expiration-hours: 24
cors:
  allowed-origins: "http://localhost:4200,http://localhost:7500"
spring:
  kafka:
    bootstrap-servers: {kafka-host}:{kafka-port}   # e.g. localhost:9092
    producer:
      key-serializer: org.apache.kafka.common.serialization.StringSerializer
      value-serializer: org.springframework.kafka.support.serializer.JsonSerializer

kafka:
  topics:
    auth-login-admin: {topic-name}       # e.g. auth.login.admin
    generic-auth-login: {topic-name}     # e.g. generic.auth.login
    generic-auth-logout: {topic-name}    # e.g. generic.auth.logout
```

## Development Commands

### Start the API

```bash
# From the project root
./gradlew applications:app-service:bootRun
```

The API will be available at `http://localhost:7500`

### Run Tests

```bash
# Run all tests
./gradlew test

# Run tests for a specific module
./gradlew applications:app-service:test
./gradlew domain:model:test
./gradlew domain:usecase:test
./gradlew infrastructure:driven-adapters:r2dbc-repository:test
./gradlew infrastructure:entry-points:reactive-web:test
```

### Generate OpenApi Documentation (Swagger)

Once the application is running, access:
- Swagger UI: `http://localhost:7500/swagger-ui.html`
- OpenApi JSON: `http://localhost:7500/v3/api-docs`

## Main Endpoints

### Persons
- `POST /persons` - Create a new person
- `GET /persons/{id}` - Get a person by ID
- `PUT /persons/{id}` - Update an existing person

### Authentication
- `POST /auth/login` - Log in and obtain JWT token
- `POST /auth/logout` - Log out

## Implemented Features

- ✅ Clean Architecture following Bancolombia principles
- ✅ Reactive programming with Spring WebFlux and R2DBC
- ✅ Authentication and authorization with JWT
- ✅ Automatic API documentation with OpenApi/Swagger
- ✅ Global exception handling
- ✅ CORS configuration
- ✅ Health checks and metrics with Actuator
- ✅ Unit tests in all layers
- ✅ Database migrations with Flyway
- ✅ Async Kafka event publishing on login/logout

## Prerequisites

- Java 25
- Gradle 8.x
- MySQL 8.x
- Docker (optional, for development with containers)

## Default Admin Credentials

- **Email:** `admin@pragma.com`
- **Password:** `Admin123456`

## Getting Started

1. Clone the repository
2. Create the `db_people` database in MySQL
3. Configure credentials in `application.yml` if needed
4. Run `./gradlew applications:app-service:bootRun`
5. Access `http://localhost:7500/swagger-ui.html` to test the endpoints