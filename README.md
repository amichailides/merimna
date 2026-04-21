# Merimna — Supported Living Management API

## Overview

**Merimna** (meaning "care" in Greek) is a Spring Boot REST API for managing
supported living structures for people with disabilities.

The project is inspired by real supported living environments, where staff need clear
records, reliable updates, and straightforward workflows around care-related information.
It focuses on data consistency, explicit domain rules, and controlled state transitions.

## Key Features & Architecture

- **Greek-aware search:** A custom Specification-based search approach handles Greek accents, case
  sensitivity, and common character variations (e.g., σ/ς).

- **Authentication & authorization:**
    - Stateless JWT authentication with a custom filter chain
    - Method-level access control via Spring Security annotations
    - Permissions are derived from the employee's position

- **Centralized error handling:**
    - Domain and validation exceptions are structured under base classes
    - A global `@RestControllerAdvice` maps them to consistent `ApiResponse` error payloads
    - Stable error types are defined through an `ErrorCode` enum

- **User & employee linkage:** Users are tied one-to-one to employees; account state
  follows the employee lifecycle (e.g., deactivated on termination).

- **Rich domain model:** Important state changes, such as discharge, are handled in
  the domain model instead of being left only to controllers.

- **Employee assignments:**
    - Assignment lifecycle handled through domain logic, enforcing rules such as
      valid date ranges and conflict prevention

- **Domain validation:** Custom annotations and sequencing enforce domain rules and
  reduce noisy error output.

- **Database versioning:** Schema changes are managed through Flyway migrations to keep
  database structure predictable and controlled.

## Technical Stack

- **Backend:** Java 21, Spring Boot 4.x
- **Security:** Spring Security 7.x, JWT, Argon2 password hashing
- **Data Persistence:** Spring Data JPA, PostgreSQL, Flyway
- **Build Tool:** Maven
- **Utilities:** Lombok
- **API Documentation:** Springdoc OpenAPI
- **Validation Engine:** Jakarta Bean Validation (Hibernate Validator)

## API Overview

The API is centered around the `Beneficiary` aggregate, alongside employee management,
assignments, and user administration.

### Authentication

- `POST /api/auth/login`

### Beneficiaries

- `POST /api/beneficiaries`
- `GET /api/beneficiaries`
- `GET /api/beneficiaries/{id}`
- `PATCH /api/beneficiaries/{id}`
- `POST /api/beneficiaries/{id}/discharge`
- `PATCH /api/beneficiaries/{id}/house-unit/{code}`

**Related resources:**

- `/api/beneficiaries/{beneficiaryId}/allergies`
- `/api/beneficiaries/{beneficiaryId}/medications`
- `/api/beneficiaries/{beneficiaryId}/legal-representatives/{legalRepresentativeId}`

### Employees

- `GET /api/employees`
- `POST /api/employees`
- `PATCH /api/employees/{id}`
- `POST /api/employees/{id}/terminate`
- `POST /api/employees/{id}/reactivate`

### Employee Assignments

- `POST /api/employees/{employeeId}/assignments`
- `GET /api/employees/{employeeId}/assignments`
- `GET /api/employees/{employeeId}/assignments/{assignmentId}`
- `POST /api/employees/{employeeId}/assignments/{assignmentId}/cancel`
- `POST /api/employees/{employeeId}/assignments/{assignmentId}/terminate`

### Users

- `POST /api/users`
- `GET /api/users`
- `GET /api/users/{id}`
- `PATCH /api/users/{id}`
- `GET /api/users/me`
- `PATCH /api/users/me/password`

### Supporting resources

Standard CRUD operations are available for:

- `/api/legal-representatives`
- `/api/house-units`
- `/api/employee-positions`

## API Documentation

- `/api/v3/api-docs` — OpenAPI specification
- `/api/scalar` — interactive API UI

## Design Considerations

- Validation is split between DTO-level constraints and domain rules to keep business logic explicit.
- Error handling is centralized, providing consistent API responses across all failure scenarios.
- Responsibilities are clearly separated between controllers, services, mappers, and domain entities.
- Domain logic is kept inside the model where possible, avoiding an anemic domain design.

## Development Setup

### Prerequisites

- **Java 21** or higher
- **Maven 3.9+**
- **PostgreSQL 15+**

### Installation & Configuration

1. **Clone the repository:**
   ```bash
   git clone https://github.com/amichailides/merimna.git
   cd merimna
   ```
2. **Configure the database:**
    * Set `DB_URL`, `DB_USERNAME`, and `DB_PASSWORD`.
    * Flyway migrations run automatically on startup.
3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

## Future Vision

- Refine authorization coverage across all endpoints
- Improve security logging and auditing
- Add integration tests for critical flows
- Introduce a frontend application

## Project Evolution

As the project evolved, architectural decisions started to be documented using ADRs, while technical improvements and
refactoring tasks are tracked through GitHub Issues.

Some changes that shaped the current codebase include:

- Moving from simple CRUD-style endpoints toward more domain-driven workflows.
- Centralizing error handling to ensure consistent API responses.
- Evolving authorization from role-based checks to permission-based access control.

## Non-functional Concerns

### Security

Dependencies are periodically reviewed and updated to address known vulnerabilities (CVEs) during development.

### Documentation

Architectural decisions are documented in the [ADR section](docs/adr).

### Project Tracking

Ongoing improvements are tracked through [Issues](https://github.com/amichailides/merimna/issues).