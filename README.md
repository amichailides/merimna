# Merimna — Supported Living Management API

## Overview

In supported living environments, important information about residents, staff responsibilities, house units, and
care-related records can become scattered across paper forms, shared folders, spreadsheets, and informal communication.
That can create gaps in accountability, consistency, and access control.

Inspired by real supported living workflows, **Merimna** is a Spring Boot REST API that models these operations in a
structured way, with explicit domain rules, controlled access, and traceability for sensitive changes.

## Key Features & Architecture

- **Authentication & authorization:** Stateless JWT authentication with short-lived access tokens and rotating,
  database-backed opaque refresh tokens. Logout invalidates the submitted refresh token, while password changes,
  password resets, and refresh token reuse detection revoke all active refresh tokens for the affected user. Browser
  clients receive tokens via HttpOnly cookies; non-browser clients fall back to response body delivery.

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

- **Audit logging:** Important domain, user-management, and security-sensitive events are captured through application
  events and persisted as structured audit records, keeping audit concerns centralized rather than scattered across
  services.

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
- **Containerization:** Docker, Docker Compose
- **API Documentation:** Springdoc OpenAPI
- **Validation Engine:** Jakarta Bean Validation (Hibernate Validator)

## API Overview

The API covers core supported-living workflows, including beneficiary management, employee administration, assignments,
temporary placements, user accounts, and supporting reference data.

### Authentication

- **POST** `/api/auth/login`
- **POST** `/api/auth/refresh`
- **POST** `/api/auth/logout`
- **POST** `/api/auth/forgot-password`
- **POST** `/api/auth/reset-password`

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

- **Docker & Docker Compose**

### Run Locally

1. **Clone the repository:**

```bash
git clone https://github.com/amichailides/merimna.git
cd merimna
```

2. **Start the application:**

```bash
docker compose up --build
```

The API will be available at `http://localhost:8080`.

## Future Vision

- **Care activity records:** Add staff-facing forms for recording beneficiary care activities, incidents, and daily
  notes.
- **Authorization testing:** Expand integration tests for placement-aware access control and other critical security
  flows.
- **Audit expansion:** Extend audit coverage to additional domain events and sensitive data operations.
- **Staff dashboard:** Build a frontend application focused on common staff workflows in supported living environments.
- **Refresh token hardening:** Expand bulk refresh token revocation flows, such as password changes and user
  deactivation.
