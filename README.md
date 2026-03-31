# Merimna — Supported Living Management System

## Overview

**Merimna** (meaning "care" in Greek) is a Spring Boot REST API designed to support the management of supported living
structures for people with disabilities.

The project is inspired by real supported living environments, where staff need clear records, reliable updates, and
simple day-to-day workflows around care-related information.

This project focuses on modeling real-world care workflows, where data consistency, clear domain rules, and controlled
state transitions are critical.

At this stage, the focus is on the backend and on keeping the structure understandable as the domain grows.

## Key Features & Architecture

- **Greek-aware search:** A custom Specification-based search approach handles Greek accents, case sensitivity, and
  common character variations (e.g., σ/ς).

- **Domain-driven validation:**
    - **Composite Annotations:** Custom reusable constraints (for example `@ValidAmka`) combine standard checks with
      domain-specific validation logic.
    - **Validation sequencing:** `@GroupSequence` is used to control validation order and reduce noisy error output.

- **Centralized error handling:** A global `@RestControllerAdvice` translates exceptions into consistent API responses.

- **Rich domain model:** Important state changes, such as discharge, are handled in the domain model instead of being
  left only to controllers.

- **Database versioning:** Schema changes are managed through Flyway migrations to keep database structure predictable
  and controlled.

## Technical Stack

- **Backend:** Java 21, Spring Boot 4.x
- **Data Persistence:** Spring Data JPA, PostgreSQL, Flyway
- **Build Tool:** Maven
- **Utilities:** Lombok
- **API Documentation:** Springdoc OpenAPI
- **Validation Engine:** Jakarta Bean Validation (Hibernate Validator)

## API Overview

The API is centered around the `Beneficiary` aggregate and its related domain resources.

### Core endpoints

- `POST /api/beneficiaries`
- `GET /api/beneficiaries`
- `GET /api/beneficiaries/{id}`
- `PATCH /api/beneficiaries/{id}`
- `POST /api/beneficiaries/{id}/discharge`
- `PATCH /api/beneficiaries/{id}/house-unit/{code}`

### Related resources

- `/api/beneficiaries/{beneficiaryId}/allergies`
- `/api/beneficiaries/{beneficiaryId}/medications`
- `/api/beneficiaries/{beneficiaryId}/legal-representatives/{legalRepresentativeId}`

### Additional resources

- `/api/legal-representatives`
- `/api/house-units`
- `/api/employees`

## API Documentation

- `/api/v3/api-docs` — OpenAPI specification
- `/api/scalar` — interactive API UI

## Design Considerations

- Validation is split between DTO-level checks and domain/business rules.
- API responses aim to stay consistent across resources and error cases.
- Controllers, services, mappers, and entities keep separate responsibilities.

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

- Add role-based access control (RBAC) with Spring Security
- Improve test coverage and introduce integration tests
- Containerize the application with Docker

## Project Evolution

As the project evolved, architectural decisions started to be documented using ADRs, while technical improvements and
refactoring tasks are tracked through GitHub Issues.

Some changes that shaped the current codebase include:

- Moving from larger nested request payloads toward dedicated nested resources.
- Making validation and domain rules more explicit and isolated by responsibility.
- Centralizing error handling so API responses remain consistent.

## Non-functional Concerns

### Security
Dependencies are periodically reviewed and updated to address known vulnerabilities (CVEs) during development.

### Documentation
Architectural decisions are documented in the [ADR section](docs/adr).

### Project Tracking
Ongoing improvements are tracked through [Issues](https://github.com/amichailides/merimna/issues).