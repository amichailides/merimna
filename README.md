# Merimna - Supported Living Management System

## Overview

**Merimna** (Μέριμνα - Greek for "Care") is a RESTful API developed with Spring Boot, designed to manage
supported living structures for people with disabilities. The system provides a solid solution for handling beneficiary
lifecycles, ensuring data integrity and specialized search capabilities for the Greek language.

Currently, the project focuses on the back-end infrastructure, built with a commitment to clean code, layered
architecture, and enterprise-level scalability.

## Key Features & Architecture

- **Advanced Greek Search:** Custom Specification-based search engine that handles Greek accents (e.g., ά/α),
  case sensitivity, and special characters (e.g., σ/ς).
- **Domain-Driven Validation:**
    - **Composite Annotations:** Implementation of custom, reusable constraints (e.g., `@ValidAmka`, `@ValidName`) that
      bundle standard checks with complex business logic.
    - **Waterfall Validation:** Utilization of `@GroupSequence` to enforce hierarchical constraint execution (
      short-circuit logic), preventing validation noise for the end user.
- **Error Handling:** Global `@RestControllerAdvice` translating all exceptions into a standardized `ApiResponse`
  structure.
- **Lifecycle Management:** Soft-delete functionality to deactivate beneficiaries while preserving historical data
  integrity.

## Technical Stack

- **Backend:** Java 21, Spring Boot 3.x
- **Data Persistence:** Spring Data JPA, PostgreSQL
- **Build Tool:** Maven
- **Utilities:** Lombok
- **Validation Engine:** Hibernate Validator (Bean Validation 3.0)

## API Endpoints

| Method  | Endpoint                             | Description                                                |
|---------|--------------------------------------|------------------------------------------------------------|
| `POST`  | `/api/beneficiaries`                 | Creates a new beneficiary with full validation.            |
| `GET`   | `/api/beneficiaries/{id}`            | Retrieves a beneficiary by their unique ID.                |
| `GET`   | `/api/beneficiaries/amka/{amka}`     | Retrieves a beneficiary by their AMKA.                     |
| `GET`   | `/api/beneficiaries`                 | Retrieves all beneficiaries with pagination/filters.       |
| `PATCH` | `/api/beneficiaries/{id}/deactivate` | Soft-deactivates a beneficiary record.                     |
| `GET`   | `/api/beneficiaries/search`          | Performs a global search using the Greek-optimized engine. |

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