# ADR-007: Exception Classification and Validation Error Strategy

## Status

Accepted

## Date

2026-05-07

## Context

Merimna uses custom exceptions carrying an `ErrorCode`, HTTP status, message key, and optional runtime arguments.

Originally, many custom exceptions extended `BaseDomainException` mechanically, even when they represented different
kinds of expected application errors, such as:

- resource not found errors
- authentication/security errors
- duplicate field conflicts
- domain lifecycle/state violations
- structured validation errors

This made the exception hierarchy less expressive and blurred the difference between:

- a domain rule violation
- a field-level validation response
- a generic expected application error

We also needed a consistent rule for deciding when duplicate/existing-data checks should be returned as
`ConflictValidationException` instead of custom single exceptions.

## Decision

We introduced `BaseApplicationException` as the common root for all expected application exceptions that carry an
`ErrorCode`.

The hierarchy is:

```text
RuntimeException
└── BaseApplicationException
    ├── BaseDomainException
    └── BaseValidationException
        ├── DomainValidationException
        └── ConflictValidationException
```

### BaseApplicationException

Used as the common root for expected application errors.

Examples:

- not found errors
- security/authentication errors
- generic application-level errors
- expected application errors without field-level validation context

### BaseDomainException

Used for single domain/business rule violations without field-level validation context.

Examples:

- employee already terminated
- beneficiary already inactive
- assignment overlap not allowed
- house unit full
- placement already closed
- relationship invariants inside entities, such as an allergy or medication already assigned to another beneficiary

### BaseValidationException

Used only when the API response should contain structured field-level errors through a `validationErrors` map.

### DomainValidationException

Used when a business/domain rule is expressed as field-level validation.

Example:

```text
amka -> AMKA does not match date of birth
```

### ConflictValidationException

Used when an input field conflicts with existing persisted data.

Examples:

```text
contactEmail -> employee email already exists
code -> house unit code already exists
code -> employee position code already exists
email -> user email already exists
substance -> allergy substance already exists for beneficiary
```

## Classification Rule

```text
Has validationErrors map with field context?
→ BaseValidationException

Conflict with existing data on a specific input field?
→ ConflictValidationException

Business rule on a specific input field?
→ DomainValidationException

Domain rule / invariant without field context?
→ BaseDomainException

Expected application error without domain/validation semantics?
→ BaseApplicationException
```

## Consequences

The global exception handler now handles `BaseApplicationException`, so all expected custom exceptions share one
handling path.

This keeps the API behavior stable while allowing more expressive exception classification.

Duplicate field conflicts are now returned consistently as structured validation errors where appropriate.

Domain invariants remain explicit custom exceptions when they represent a single business rule failure rather than
field-level validation.

## Examples

### Field-level conflict

```java
Map<String, String> conflicts = new LinkedHashMap<>();
conflicts.

put("email",ErrorCode.EMAIL_ALREADY_EXISTS.getMessageKey());
        throw new

ConflictValidationException(conflicts);
```

### Field-level domain validation

```java
Map<String, String> errors = new LinkedHashMap<>();
errors.

put("amka",ErrorCode.AMKA_DATE_MISMATCH.getMessageKey());
        throw new

DomainValidationException(errors);
```

### Single domain rule violation

```java
throw new HouseUnitFullException(houseUnit.getCode(),count);
```

### Expected application error

```java
throw new BeneficiaryNotFoundByPublicIdException(publicId);
```

## Related ADRs

- [ADR-002](ADR-002-business-vs-entity-validation.md): Defines the fail-fast strategy for
  state transitions. This ADR refines the exception classification terminology used there.

## Notes

HTTP status is defined by `ErrorCode`, not by the base exception class.

For example, a `BaseDomainException` can still return `409 Conflict` if its `ErrorCode` uses `HttpStatus.CONFLICT`.

The base class answers:

```text
What kind of application error is this?
```

The `ErrorCode` answers:

```text
What HTTP status and message key should the API return?
```