# Merimna - Supported Living Management System

## Overview

**Merimna** ("Care") is a Spring Boot REST API designed to support the management of supported living structures for
people with disabilities.

The project is inspired by real supported living environments, where staff need clear records, reliable updates, and
simple day-to-day workflows around care-related information. At this stage, the focus is on the backend and on keeping
the structure understandable as the domain grows.

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

## API Endpoints

| Method  | Endpoint                            | Description                                                    |
|---------|-------------------------------------|----------------------------------------------------------------|
| `POST`  | `/api/beneficiaries`                | Creates a beneficiary after request and domain validation.     |
| `GET`   | `/api/beneficiaries/{id}`           | Returns a beneficiary by ID along with related information.    |
| `GET`   | `/api/beneficiaries`                | Lists beneficiaries with pagination and basic filtering.       |
| `PATCH` | `/api/beneficiaries/{id}`           | Applies partial updates while keeping validation rules intact. |
| `POST`  | `/api/beneficiaries/{id}/discharge` | Marks a beneficiary as inactive through a domain action.       |

### Nested Resources

Related beneficiary data is managed through dedicated nested endpoints:

- **Allergies:** `/api/beneficiaries/{id}/allergies`
- **Medications:** `/api/beneficiaries/{id}/medications`
- **Legal Representatives:** `/api/beneficiaries/{id}/legal-representatives`

Each nested resource follows the same general REST pattern (create, retrieve, update, remove).

## Design Considerations

- Validation is split between DTO-level checks and domain/business rules.
- API responses aim to stay consistent across resources and error cases.
- Controllers, services, mappers, and entities keep separate responsibilities.

## API Documentation

- `/api/v3/api-docs` is the OpenAPI contract.
- `/api/scalar` is the interactive UI.

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

Architectural decisions are documented in the [ADR section](docs/adr).

Ongoing improvements are tracked through [Issues](https://github.com/amichailides/merimna/issues).
