# ADR-003: Converting MedicalTreatment from @Embeddable to @Entity

## Status
Accepted

## Date
2026-03-02

---

## Context
In the initial implementation, the medication treatment (`medicalTreatment`) was defined as an `@ElementCollection` with `@Embeddable` objects. This approach treated medications as simple "data" (Value Objects) belonging to the beneficiary, without their own autonomous existence in the database.

```java
// Old implementation (Value Object logic)
@ElementCollection
@CollectionTable(name = "beneficiary_medical_treatments", joinColumns = @JoinColumn(name = "beneficiary_id"))
private Set<MedicalTreatment> medicalTreatments = new HashSet<>();
```

However, the application domain (Supported Living Structure for People with Disabilities) requires stricter control and future expansion in medication management.

## Problem
Using `@ElementCollection` presented the following limitations:

1.  **Lack of Identity:** Medications did not have their own Primary Key (ID). This makes it difficult for the Front-end to identify a specific treatment line.
2.  **Inability to Relate:** An `@Embeddable` cannot be the target of a Foreign Key from other tables (e.g., Inventory/Stock).
3.  **Querying Limitations:** Inefficient searching for statistical or clinical data at the medication level.

## Decision
It was decided to upgrade `Medication` to a full `@Entity` with its own Lifecycle.

### Design Choices
1.  **Identity:** Added `@Id` with `@GeneratedValue(strategy = GenerationType.IDENTITY)`.
2.  **Relationship:** `@ManyToOne` relationship from `Medication` to `Beneficiary` (Bidirectional).
3.  **Ownership:** The `Beneficiary` remains the Aggregate Root. Management is done through it with `cascade = CascadeType.ALL` and `orphanRemoval = true`.
4.  **Encapsulation:** Use of helper methods to ensure the bidirectional relationship:

```java
public void addMedication(@NonNull Medication medication) {
    this.medicalTreatment.add(medication);
    medication.assignToBeneficiary(this);
}
```

## Consequences

*   ✅ **Inventory Tracking (Stash):** Allows Phase 2 implementation for stock tracking by linking stock to the Medication ID.
*   ✅ **Audit Trail:** Ability to log history (which caregiver administered the medication).
*   ✅ **API Clarity:** The DTO returns IDs, facilitating updates for the Front-end.

*   ❌ **Increased Complexity:** Requirement for `MedicationMapper` and explicit update management (Clear & Add strategy).
*   ❌ **ID Instability:** Medication IDs are renewed on every update due to `orphanRemoval` (acceptable trade-off for Mapper simplicity at this stage).

## Future (Phase 2)
*   **Medication Inventory:** Introduction of notifications (Low Stock Alerts) when stock falls below the threshold.
*   **Administration Logging:** Creation of a dose logging table in real-time for beneficiary safety.