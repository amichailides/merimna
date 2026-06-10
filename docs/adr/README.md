# Architecture Decision Records (ADR)

This directory contains the architectural decisions made during the development of the project.

The project uses the ADR (Architecture Decision Record) approach to document important technical decisions.

Each ADR records:

- **What** was decided
- **Why** it was decided
- **What alternatives** were considered

---

## Index

| #                                                                            | Title                                                              | Status   | Date       |
|------------------------------------------------------------------------------|--------------------------------------------------------------------|----------|------------|
| [ADR-001](ADR-001-validation-presence-vs-value-separation.md)                | Separation of Presence from Value Validation                       | Accepted | 2026-02-28 |
| [ADR-002](ADR-002-business-vs-entity-validation.md)                          | Fail-Fast Strategy & Rich Domain Model for State Transitions       | Accepted | 2026-03-01 |
| [ADR-003](ADR-003-medication-embeddable-to-entity.md)                        | Converting MedicalTreatment from @Embeddable to @Entity            | Accepted | 2026-03-02 |
| [ADR-004](ADR-004-granular-resource-based-api.md)                            | Use Granular Resource-Based API for Sub-Resources                  | Accepted | 2026-03-02 |
| [ADR-005](ADR-005-centralized-error-handling.md)                             | Centralized Error Handling and ErrorCode-driven API Responses      | Accepted | 2026-03-05 |
| [ADR-006](ADR-006-refresh-token-transport-strategy.md)                       | Hybrid Refresh Token Transport for Browser and Non-Browser Clients | Accepted | 2026-05-01 |
| [ADR-007](ADR-007-exception-classification-and-validation-error-strategy.md) | Exception Classification and Validation Error Strategy             | Accepted | 2026-05-07 |
| [ADR-008](ADR-008-permission-aware-frontend-authorization.md)                | Permission-Aware Frontend Authorization                            | Accepted | 2026-06-09 |

---

## Status Definitions

| Status                  | Meaning                                                 |
|-------------------------|---------------------------------------------------------|
| `Accepted`              | Current valid decision                                  |
| `Superseded by ADR-XXX` | Replaced by a newer ADR and kept for historical context |
