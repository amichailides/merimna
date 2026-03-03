# ADR-004: Use Granular Resource-Based API for Sub-Resources

## Status
Accepted

## Date
2026-03-02

---

## Context
Initially, the project utilized a **Monolithic/Bulk DTO** approach for creating and updating beneficiaries. The `BeneficiarySaveDTO` included nested lists for sub-resources like `Medication` and `Allergy`.

```java
// Initial approach
public class BeneficiarySaveDTO {
    // ... basic fields
    private List<MedicationDTO> medications;
    private List<AllergyDTO> allergies;
}
```

This required the `BeneficiaryMapper` to handle not just the beneficiary entity but also the complex logic of mapping, persisting, and associating multiple sub-entities in a single transaction.

## Problem
This "Bulk" approach led to several issues:
1.  **Complex Mapping Logic:** Mappers became bloated with logic for handling nested collections, leading to maintenance difficulties.
2.  **Encapsulation Violations:** Direct manipulation of entity collections often conflicted with defensive coding practices (e.g., unmodifiable collections returned by getters).
3.  **Error Handling:** A validation error in a single sub-resource (e.g., one invalid medication) would reject the entire beneficiary creation/update, providing a poor user experience.
4.  **Performance:** Large payloads could lead to long-running transactions.

## Decision
We decided to shift to a **Granular Resource-Based API**.
Sub-resources are now managed via dedicated REST endpoints, decoupling the `Beneficiary` lifecycle from its related resources.

### Changes
1.  **Dedicated Endpoints:**
    *   `POST /api/beneficiaries` (Creates only the Beneficiary)
    *   `POST /api/beneficiaries/{id}/medications` (Adds a Medication)
    *   `POST /api/beneficiaries/{id}/allergies` (Adds an Allergy)
2.  **DTO Decoupling:** `BeneficiarySaveDTO` and `BeneficiaryUpdateDTO` no longer contain lists of medications or allergies.
3.  **Mapper Simplification:** `BeneficiaryMapper` is now responsible solely for the `Beneficiary` entity.

## Rationale
*   **Separation of Concerns:** Each resource is managed by its own Service and Controller, adhering to the Single Responsibility Principle.
*   **REST Best Practices:** The API structure reflects the resource hierarchy (`/beneficiaries/{id}/sub-resource`).
*   **Granular Validation:** Errors are reported per resource. A failure to add a medication does not invalidate the existence of the beneficiary.

---

## Consequences

*   ✅ **Simplified Mappers:** Mappers are cleaner and focused on a single entity type.
*   ✅ **Better Maintainability:** Changes to `Medication` logic do not impact `Beneficiary` logic.
*   ✅ **Clearer API Contracts:** The API is self-documenting regarding resource relationships.
*   ❌ **Frontend Complexity:** The client must make multiple API calls to fully populate a beneficiary record (or use a separate orchestration layer/BFF if needed).