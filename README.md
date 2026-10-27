# Margin Hotel Management System

## Project overview

Margin Hotel Management System is an academic project that provides a REST API for common hotel operations. It supports managing rooms, guests, bookings, staff, invoices, and payments. This repository contains the backend application.

The companion [Margin Hotel frontend](https://github.com/Madondo07/margin-hotel-frontend) is built with Next.js and TypeScript. It supports public room browsing and booking, as well as an administration dashboard for staff.

## Technology

- Java 17
- Spring Boot 4.1.0
- Spring Web, Spring Data JPA, and Spring Security
- MySQL
- Maven Wrapper
- Flyway database migration support

## Requirements

- JDK 17 or later
- MySQL server

## Setup and run

1. Clone the repository and start MySQL.
2. Configure the application’s database connection, JWT signing secret, and administrator credentials. Supply these as environment variables rather than committing credentials:

   - `SPRING_DATASOURCE_URL`
   - `SPRING_DATASOURCE_USERNAME`
   - `SPRING_DATASOURCE_PASSWORD`
   - `JWT_SECRET`
   - `APP_ADMIN_EMAIL`
   - `APP_ADMIN_PASSWORD`

   The default database URL targets a local MySQL database named `marginhoteldb`.
3. From the repository root, start the application:

   ```powershell
   .\mvnw.cmd spring-boot:run
   ```

The API is available at `http://localhost:8080/marginhotel`. On startup, the application creates an administrator account using the configured email and password if that account does not already exist. Demo data is enabled by default; set `APP_DEMO_DATA_ENABLED=false` to disable it.

## API documentation

For routes, request and response types, and endpoint examples, see [API_ENDPOINTS.md](./API_ENDPOINTS.md).

## Tests

Run the test suite from the repository root:

```powershell
.\mvnw.cmd test
```

## Project structure

- `src/main/java` — application, domain, controller, repository, and service code
- `src/main/resources` — application configuration and web resources
- `src/test/java` — automated tests

## UML Diagram
![Margin Hotel UML Diagram](Margin_Hotel_UML_2.jpeg)

