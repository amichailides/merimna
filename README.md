# Merimna — Supported Living Management API

## Overview

**Merimna** (meaning "care" in Greek) is a Spring Boot REST API for managing
supported living structures for people with disabilities.

The project is inspired by real supported living environments, where staff need clear
records, reliable updates, and straightforward workflows around care-related information.
It focuses on data consistency, explicit domain rules, and controlled state transitions.

## Key Features & Architecture

- **Authentication & authorization:** Stateless JWT authentication with a custom filter chain, method-level access
  control via Spring Security annotations, and permissions derived from each employee's position.

- **Placement-aware access control:** Active placements temporarily extend an employee's access to beneficiary records
  of another house unit, with scope resolved at runtime from active assignments and placements.

- **Rich domain model:** Core business workflows, such as beneficiary discharge and employee termination, are captured
  through dedicated domain methods.

- **Assignment & placement lifecycle:** Domain methods handle lifecycle transitions, while validation policies use
  repository-level overlap checks to prevent invalid date ranges and conflicting active assignments or placements.

- **Greek-aware search:** Custom JPA Specifications support accent-insensitive and case-insensitive Greek search using
  PostgreSQL `unaccent`, including Greek-specific variations such as σ/ς. For example, searching for `Σαββας` can match
  `Σάββας`, `ΣΑΒΒΑΣ`, and `Σαββας`.

- **Centralized error handling:** Domain and validation exceptions are mapped by a global `@RestControllerAdvice` to
  consistent `ApiResponse` error payloads with stable `ErrorCode` values.

- **Domain validation:** Custom validation annotations and validation group sequencing enforce domain rules while
  reducing noisy error output.

- **User & employee linkage:** User accounts are linked one-to-one with employees, and account state follows the
  employee lifecycle, such as automatic deactivation on termination.

- **Database versioning:** Flyway migrations manage schema changes, keeping database structure predictable and
  controlled.

## Development Context

As the project evolves, [ADRs](docs/adr) are used to document important design
decisions and trade-offs.

Planned features, technical debt, and refactoring tasks are tracked
through [GitHub Issues](https://github.com/amichailides/merimna/issues)
to keep the development workflow structured and the project's evolution visible.

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
assignments, placements, and user administration.

### Authentication

- **POST** `/api/auth/login`

### Beneficiaries

- **GET** `/api/beneficiaries`
- **POST** `/api/beneficiaries`
- **GET** `/api/beneficiaries/{publicId}`
- **PATCH** `/api/beneficiaries/{publicId}`
- **PATCH** `/api/beneficiaries/{publicId}/house-unit/{houseUnitPublicId}`
- **POST** `/api/beneficiaries/{publicId}/discharge`

**Related beneficiary resources:**

- `/api/beneficiaries/{beneficiaryPublicId}/allergies`
- `/api/beneficiaries/{beneficiaryPublicId}/medications`
- `/api/beneficiaries/{beneficiaryPublicId}/legal-representatives/{legalRepresentativePublicId}`

### Employees

- **GET** `/api/employees`
- **POST** `/api/employees`
- **GET** `/api/employees/{publicId}`
- **PATCH** `/api/employees/{publicId}`
- **POST** `/api/employees/{publicId}/terminate`
- **POST** `/api/employees/{publicId}/reactivate`

### Employee Assignments

- **GET** `/api/employees/{employeePublicId}/assignments`
- **POST** `/api/employees/{employeePublicId}/assignments`
- **GET** `/api/employees/{employeePublicId}/assignments/{assignmentPublicId}`
- **POST** `/api/employees/{employeePublicId}/assignments/{assignmentPublicId}/cancel`
- **POST** `/api/employees/{employeePublicId}/assignments/{assignmentPublicId}/terminate`

### Employee Placements

- **GET** `/api/placements`
- **POST** `/api/placements`
- **GET** `/api/placements/{publicId}`
- **POST** `/api/placements/{publicId}/terminate`

### Users

- **GET** `/api/users`
- **POST** `/api/users`
- **GET** `/api/users/{publicId}`
- **PATCH** `/api/users/{publicId}`
- **GET** `/api/users/me`
- **PATCH** `/api/users/me/password`

### Reference data & supporting resources

Standard CRUD operations are available for:

- `/api/legal-representatives`
- `/api/house-units`
- `/api/employee-positions`

## API Documentation

After starting the application locally, API documentation is available at:

- `/api/v3/api-docs` — OpenAPI specification
- `/api/scalar` — interactive API UI

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

- **Care activity records:** Add staff-facing forms for recording beneficiary care activities, incidents, and daily
  notes.
- **Authorization testing:** Expand integration tests for placement-aware access control and other critical security
  flows.
- **Audit logging:** Introduce structured audit logs for sensitive data changes and access-related events.
- **Staff dashboard:** Build a frontend application focused on common staff workflows in supported living environments.
