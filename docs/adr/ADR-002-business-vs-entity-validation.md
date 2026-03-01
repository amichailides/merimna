# ADR-002: Fail-Fast Strategy & Entity Logic for State Transitions

## Status

**Accepted**

## Date

2026-03-01

## Context

Το endpoint `/api/beneficiaries/{id}/discharge` υλοποιεί μια μετάβαση κατάστασης (state transition) που απενεργοποιεί
έναν ωφελούμενο.

Η διαδικασία περιλαμβάνει δύο ειδών ελέγχους:

1. **Preconditions (State Invariants):** Ο ωφελούμενος πρέπει να είναι ενεργός (`isActive = true`).
2. **Business Rules (Validation):** Δεν πρέπει να υπάρχουν εκκρεμότητες (οφειλές, ανοιχτά αιτήματα).
   _(TODO: Not yet implemented)_
   Μέχρι πρότινος, η λογική αυτή βρισκόταν στο Service Layer (Transaction Script pattern), με κίνδυνο ασυνέπειας αν
   παρακαμφθεί το Service.

---

## Problem

Πρέπει να αποφασίσουμε τη στρατηγική διαχείρισης σφαλμάτων και την τοποθεσία της λογικής, ώστε να αποφύγουμε:

1. **Confusing UX:** Εμφάνιση validation errors (π.χ. "εκκρεμεί οφειλή") για ωφελούμενο που έχει ήδη αποχωρήσει.
2. **Anemic Domain Model:** Η γνώση του "πότε επιτρέπεται το discharge" να είναι διάσπαρτη στα services και όχι στην
   οντότητα.
3. **Unpredictable API:** Το Frontend να μην ξέρει αν θα λάβει 409 (Conflict) ή 400 (Validation Error).

---

## Decision

### 1. Fail-Fast Strategy (State First)

Αποφασίζουμε να εφαρμόσουμε **Fail-Fast** στρατηγική για τα state transitions.
Ελέγχουμε **πρώτα** αν η μετάβαση είναι έγκυρη για την τρέχουσα κατάσταση του Entity, και **μετά** αν ικανοποιούνται οι
υπόλοιποι επιχειρησιακοί κανόνες.

Αυτό εξασφαλίζει ότι:

* Αν το Entity είναι σε λάθος state -> **409 Conflict** (Single Error Message).
* Αν το Entity είναι σε σωστό state αλλά αποτύχουν οι κανόνες -> **400 Bad Request** (Validation Errors Map).

### 2. Rich Domain Model (Logic inside Entity)

Μεταφέρουμε την ευθύνη της μετάβασης από το Service στο Entity.

* **Πριν (Service):** `if (entity.isActive()) { entity.setActive(false); }`
* **Τώρα (Entity):** `entity.discharge()`

Το Entity προστατεύει τον εαυτό του (Encapsulation) και ρίχνει το σχετικό Exception αν κληθεί σε λάθος κατάσταση.

---

## Consequences

✅ **Predictable API & UX**: Ο client λαμβάνει ιεραρχημένα σφάλματα. Δεν ζητάμε διόρθωση δεδομένων (validation) για
ενέργεια που ούτως ή άλλως απαγορεύεται (state).
✅ **Data Integrity**: Είναι αδύνατο να γίνει discharge σε inactive ωφελούμενο, από οποιοδήποτε σημείο του κώδικα κι αν
κληθεί η μέθοδος.
✅ **Performance**: Αποφεύγουμε την εκτέλεση πολύπλοκων validation rules (DB queries) αν το state check αποτύχει.

---

## Future

Add `exitReason`, `exitDate`, `approvedBy` fields to support
full discharge workflow (notifications, approvals, audit trail).
