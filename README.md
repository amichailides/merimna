# Merimna - Supported Living Management System

## Overview

**Merimna** (Μέριμνα - Greek for "Care") is a Spring Boot REST API designed to manage supported living
structures for people with disabilities.

The system manages beneficiary lifecycles and includes search capabilities tailored for the Greek language.

Currently, the project focuses on the back-end infrastructure, emphasizing clean code, layered architecture,
and maintainability.

## Key Features & Architecture

- **Greek-aware search:** A custom Specification-based search engine that handles Greek accents (e.g., ά/α),
  case sensitivity, and character variations (e.g., σ/ς).

- **Domain-driven validation:**
    - **Composite Annotations:** Custom reusable constraints (e.g., `@ValidAmka`, `@ValidName`) combine standard checks
      with domain-specific validation logic.
    - **Waterfall Validation:** `@GroupSequence` is used to enforce hierarchical constraint execution (short-circuit
      logic), preventing validation noise for the end user.

- **Centralized error handling:** A Global `@RestControllerAdvice` translates exceptions into consistent API responses.

- **Rich domain model:** Important state transitions, such as discharge, are enforced in the domain model rather than
  being handled only at the controller or service layer.

## Technical Stack

- **Backend:** Java 21, Spring Boot 3.x
- **Data Persistence:** Spring Data JPA, PostgreSQL
- **Build Tool:** Maven
- **Utilities:** Lombok
- **Validation Engine:** Hibernate Validator (Bean Validation 3.0)

## API Endpoints

| Method  | Endpoint                            | Description                                                |
|---------|-------------------------------------|------------------------------------------------------------|
| `POST`  | `/api/beneficiaries`                | Creates a new beneficiary with full validation.            |
| `GET`   | `/api/beneficiaries/{id}`           | Retrieves a beneficiary by their unique ID.                |
| `GET`   | `/api/beneficiaries/amka/{amka}`    | Retrieves a beneficiary by their AMKA.                     |
| `GET`   | `/api/beneficiaries`                | Retrieves all beneficiaries with pagination/filters.       |
| `PATCH` | `/api/beneficiaries/{id}`           | Updates beneficiary details (partial update).              |
| `POST`  | `/api/beneficiaries/{id}/discharge` | Discharges a beneficiary from the structure.               |
| `GET`   | `/api/beneficiaries/search`         | Performs a global search using the Greek-optimized engine. |

### Nested Resources

Related beneficiary data is managed through dedicated nested endpoints:

- **Allergies:** `/api/beneficiaries/{id}/allergies`
- **Medications:** `/api/beneficiaries/{id}/medications`
- **Legal Representatives:** `/api/beneficiaries/{id}/legal-representatives`

Each nested resource follows the same general REST pattern (create, retrieve, update, remove).

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
    * Open `src/main/resources/application.properties`.
    * Update the `spring.datasource.url`, `spring.datasource.username`, and `spring.datasource.password` properties.
3. **Run the application:**
   ```bash
   mvn spring-boot:run
   ```

## Future Vision

- **Full-Stack Application:** Develop a front-end application for care professionals.
- **Security:** Implement Role-Based Access Control (RBAC) using Spring Security.
- **CI/CD:** Set up automated pipelines for testing and deployment.

## Project Evolution

As the project evolved, architectural decisions started to be documented using ADRs, while technical improvements and
refactoring tasks are tracked through GitHub Issues.

Some changes that shaped the current codebase include:

- Moving from larger nested request payloads toward dedicated nested resources.
- Making validation and domain rules more explicit and isolated by responsibility.
- Centralizing error handling so API responses remain consistent.

Architectural decisions are documented in the [ADR section](docs/adr).

Ongoing improvements are tracked through [Issues](https://github.com/amichailides/merimna/issues).