# Margin Hotel Management System

Backend for Margin Hotel — a capstone project (ADP372S) that digitises front-desk operations for a small hotel chain in South Africa.

## Project summary

This repository contains the complete backend (domain model → REST controllers). The public booking website and staff UI are out of scope.

## Tech stack

- Java 21 (required — annotation processors for Lombok/MapStruct do not run correctly on newer JDKs)
- Spring Boot 4.1.0
- Spring Data JPA / Hibernate
- Spring Security 6 (stateless, JWT-based)
- MySQL (runtime)
- Maven (wrapper included)
- MapStruct, Lombok (annotation processors), Flyway

(See pom.xml for exact dependencies and versions.)

## Getting started

1. Make sure MySQL is running locally and you have a `root` user with a password (or update `application.properties` to match your own credentials).
2. Copy `src/main/resources/application.properties` and set your own `spring.datasource.password`, `jwt.secret` and `app.admin.password`. Never commit real secrets — these are for local development only.
3. Run the app:

```
./mvnw.cmd spring-boot:run
```

4. The API is served at `http://localhost:8080/marginhotel`.

On first run, `AdminSeeder` creates a default admin account (email/password from `app.admin.email` / `app.admin.password` in `application.properties`) so you can log in immediately. If `app.demo-data.enabled=true`, `DemoDataSeeder` also loads sample rooms, guests, bookings, invoices and payments so the app has data to demo/mark against.

## API reference

Detailed controller routes and example requests are documented in API_ENDPOINTS.md in the repository root. Key base paths exposed by the controllers:

- `/auth` — register, login, check-email
- `/booking`
- `/guest`
- `/invoice`
- `/payment`
- `/room`
- `/staff`

## Core business rules (high level)

- All domain attributes are mandatory except Name.middleName.
- Factories return null on validation failures (do not throw IllegalArgumentException).
- No null or empty-string values are persisted for required fields.
- Anti-double-booking enforced in the service layer (no overlapping bookings for same room).
- 1 Booking → 1 Invoice → 1 Payment (no partial payments supported).
- A guest is matched to their account by email, not by ID — registering with a known email links to that guest instead of duplicating them.

## Security

Authentication is JWT-based (stateless — no sessions). Roles are `USER`, `RECEPTIONIST` and `ADMIN`.

- Anyone can register and log in via `/auth/**`.
- `/room/**` is readable by anyone; creating, updating or deleting rooms requires `ADMIN`.
- `/booking/**`, `/guest/**` and `/invoice/**` require `RECEPTIONIST` or `ADMIN`.
- `/staff/**` requires `ADMIN`.
- Everything else requires a valid, logged-in user by default.

To call a protected endpoint, log in via `POST /auth/login` and send the returned token as `Authorization: Bearer <token>` on subsequent requests.
