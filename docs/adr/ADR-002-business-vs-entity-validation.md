# ADR-002: Fail-Fast Strategy & Entity Logic for State Transitions

## Status

Accepted

## Date

2026-03-01

## Context

The endpoint `/api/beneficiaries/{id}/discharge` implements a state transition that deactivates a beneficiary.

The process involves two types of checks:

1. **Preconditions (State Invariants):** The beneficiary must be active (`isActive = true`).
2. **Business Rules (Validation):** There must be no pending obligations (debts, open requests).

Previously, this logic resided in the Service Layer (Transaction Script pattern), risking inconsistency if the Service
was bypassed.

---

## Problem

We need to decide on the error handling strategy and the location of the logic to avoid:

1. **Confusing UX:** Displaying validation errors (e.g., "debt pending") for a beneficiary who has already been
   discharged.
2. **Anemic Domain Model:** Knowledge of "when discharge is allowed" being scattered across services rather than in the
   entity.
3. **Unpredictable API:** The Frontend not knowing whether it will receive a 409 (Conflict) or 400 (Validation Error).

---

## Decision

### 1. Fail-Fast Strategy (State First)

We decide to apply a **Fail-Fast** strategy for state transitions.
We check **first** if the transition is valid for the current Entity state, and **then** if the remaining business rules
are satisfied.

This ensures that:

* If the Entity is in the wrong state -> a single domain error, usually **409 Conflict**.
* If the Entity is in the correct state but field-level rules fail -> a structured validation error response.

### 2. Rich Domain Model (Logic inside Entity)

We move the responsibility of the transition from the Service to the Entity.

* **Before (Service):** `if (entity.isActive()) { entity.setActive(false); }`
* **Now (Entity):** `entity.discharge()`

The Entity protects itself (Encapsulation) and throws the relevant Exception if called in an invalid state.

---

## Consequences

* ✅ **Predictable API & UX**: The client receives prioritized errors. We do not ask for data correction (validation) for
  an action that is prohibited anyway (state).
* ✅ **Data Integrity**: It is impossible to discharge an inactive beneficiary, regardless of where the method is called
  from.
* ✅ **Performance**: We avoid executing complex validation rules (DB queries) if the state check fails.

---

## Update: Alignment with Exception Classification Strategy

This ADR remains valid, but the error classification terminology has been refined
by [ADR-007](ADR-007-exception-classification-and-validation-error-strategy.md).

For state transitions, Merimna follows this rule:

- If the entity is in a state where the operation is not allowed, the entity throws a single domain exception extending
  `BaseDomainException`.
- If the operation is allowed but request fields violate business rules, the application returns a structured
  `DomainValidationException`.
- If request fields conflict with existing persisted data, the application returns a structured
  `ConflictValidationException`.

The HTTP status is defined by the associated `ErrorCode`, not by the base exception class.

Examples:

Single domain exception:

`throw new BeneficiaryAlreadyInactiveException();`

Field-level domain validation:

`throw new DomainValidationException(errors);`

Field-level conflict validation:

`throw new ConflictValidationException(conflicts);`

This keeps the original fail-fast rule intact:

1. Check entity state first.
2. Stop immediately if the state transition is not allowed.
3. Run field-level validation/conflict checks only when the operation itself is valid.


## Future

Add `exitReason`, `exitDate`, `approvedBy` fields to support full discharge workflow (notifications, approvals, audit
trail).