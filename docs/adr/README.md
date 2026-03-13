# Architecture Decision Records (ADR)

This directory contains the architectural decisions made during the development of the project.

The project uses the ADR (Architecture Decision Record) approach to document important technical decisions.

Each ADR records:

- **What** was decided
- **Why** it was decided
- **What alternatives** were considered

---

## Index
| #                                                             | Title                                                        | Status   | Date       |
|---------------------------------------------------------------|--------------------------------------------------------------|----------|------------|
| [ADR-001](ADR-001-validation-presence-vs-value-separation.md) | Separation of Presence from Value Validation                 | Accepted | 2026-02-28 |
| [ADR-002](ADR-002-business-vs-entity-validation.md)           | Fail-Fast Strategy & Rich Domain Model for State Transitions | Accepted | 2026-03-01 |
| [ADR-003](ADR-003-medication-embeddable-to-entity.md)         | Converting MedicalTreatment from @Embeddable to @Entity      | Accepted | 2026-03-02 |
| [ADR-004](ADR-004-granular-resource-based-api.md)             | Use Granular Resource-Based API for Sub-Resources            | Accepted | 2026-03-02 |
| [ADR-005](ADR-005-centralized-error-handling.md)              | Centralized Error Handling and ErrorCode-driven API Responses| Accepted | 2026-03-05 |

---

## Status Definitions

| Status          | Meaning                                  |
|-----------------|------------------------------------------|
| `Proposed`      | Proposed, not yet approved               |
| `Accepted`      | Approved and being implemented           |
| `Deprecated`    | Replaced by a newer decision             |
| `Superseded by` | Replaced by a specific ADR               |