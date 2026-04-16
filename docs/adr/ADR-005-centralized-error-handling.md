# ADR-005: Centralized Error Handling and ErrorCode-driven API Responses

## Status
Accepted

## Date
2026-03-05

---

## Context
As the API evolved, error handling logic became increasingly scattered across different layers of the application. Domain exceptions were tightly coupled with HTTP concerns (e.g., `HttpStatus` embedded directly inside exception classes), and the API response structure for errors lacked a consistent contract.

Initially, exceptions such as `BaseBusinessException` carried HTTP status codes and message keys directly. This design worked but created architectural leakage between layers: domain exceptions were aware of transport concerns (HTTP), which violates separation of concerns.

Additionally, the API responses for errors were wrapped in a generic `ApiResponse` object containing fields such as `success`, `errorCode`, `status`, `error`, and `message`. Over time, this structure started to diverge from widely adopted REST error response conventions and made the error payload less clear.

Validation errors were also handled in multiple ways:
*   Custom validation exceptions
*   Spring’s `MethodArgumentNotValidException`
*   `ConstraintViolationException`
*   JSON parsing errors

These different cases required a unified strategy so that clients always receive consistent error responses.

## Problem
A redesign of the API error handling architecture was required to address:
1.  **Inconsistency:** Different types of errors produced different JSON structures.
2.  **Coupling:** Domain exceptions depended on Web layer classes (`HttpStatus`).
3.  **Maintenance:** Error messages and codes were hardcoded in multiple places.

---

## Decision
The application now uses a centralized error handling architecture based on the following principles:

### 1. ErrorCode as the Single Source of Truth
An `ErrorCode` enum is used to define all application error types. Each error code now contains:
*   A stable machine-readable `code` (e.g., `BENEFICIARY_NOT_FOUND_BY_ID`)
*   The associated `HttpStatus` (e.g., `NOT_FOUND`)
*   A `messageKey` used for i18n message resolution

This allows the system to derive both the HTTP status and the localized message from a single definition.

### 2. Removal of HTTP Concerns from Domain Exceptions
Domain exceptions no longer carry `HttpStatus` directly. Instead, they extend `BaseDomainException` and only carry:
*   The `ErrorCode`
*   Optional arguments used for message interpolation

This ensures that domain exceptions remain independent from the web layer's specific status codes, delegating that mapping to the `ErrorCode`.

### 3. Centralized Exception Translation
A `GlobalExceptionHandler` is responsible for translating all exceptions into HTTP responses. The handler:
1.  Retrieves the `HttpStatus` from the `ErrorCode`.
2.  Resolves localized messages using the `messageKey`.
3.  Constructs a standardized `ApiResponse` error object.

### 4. Standardized Error Response Schema
All error responses follow a consistent structure inspired by the **Problem Details for HTTP APIs (RFC 7807)**.

**General Error Response:**
```json
{
  "code": "BENEFICIARY_NOT_FOUND_BY_ID",
  "status": 404,
  "title": "Not Found",
  "detail": "The beneficiary with ID 123 was not found.",
  "path": "/api/beneficiaries/123",
  "timestamp": "2026-03-05T10:00:00Z"
}
```

**Validation Error Response:**
Includes an additional `validationErrors` field with field-level details.
```json
{
  "code": "VALIDATION_FAILED",
  "status": 400,
  "title": "Bad Request",
  "detail": "Validation failed for the request.",
  "path": "/api/beneficiaries",
  "timestamp": "2026-03-05T10:00:00Z",
  "validationErrors": {
    "amka": "must not be blank",
    "firstName": "size must be between 2 and 50"
  }
}
```

### 5. Unified Validation Error Handling
Validation errors from all sources (`BaseValidationException`, `MethodArgumentNotValidException`, `ConstraintViolationException`) are normalized to produce the same response format, populating the `validationErrors` map with field-level details.

---

## Consequences

### Positive
*   ✅ **Separation of Concerns:** Clear separation between domain logic and HTTP transport concerns.
*   ✅ **Consistency:** Centralized and consistent error handling across the entire API.
*   ✅ **REST Alignment:** Stronger alignment with REST error response conventions (Problem Details).
*   ✅ **Localization:** Simplified localization through message keys in `ErrorCode`.
*   ✅ **Maintainability:** Easier maintenance and extension of error types via a single Enum.
*   ✅ **Frontend Experience:** Improved API clarity for frontend consumers with predictable error structures.

### Negative
*   ❌ **Infrastructure Complexity:** Slightly increased complexity in the error handling infrastructure (GlobalExceptionHandler).
*   ❌ **Coupling (Pragmatic):** `ErrorCode` (if shared) still couples the application to HTTP concepts, but this is accepted for simplicity in this monolithic architecture.
*   ❌ **Refactoring Effort:** Existing exceptions required refactoring to remove embedded HTTP status handling.

---

## Alternatives Considered

**1. Keeping HttpStatus inside exception classes**
*   Rejected because it tightly coupled domain logic with HTTP semantics. Exceptions originating from the domain layer should not depend on transport-level concepts.

**2. Returning raw exceptions without structured responses**
*   Rejected because it would result in inconsistent error payloads and a poor client experience.

**3. Maintaining the original ApiResponse structure with a success flag**
*   Rejected because it produced unnecessary noise in error responses and did not align well with common REST error response patterns. The HTTP status code itself indicates success or failure.