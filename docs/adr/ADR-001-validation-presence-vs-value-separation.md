# ADR-001: Διαχωρισμός Presence Validation από Value Validation στα Custom Annotations

## Status
Accepted

## Ημερομηνία
2026-02-28

---

## Πλαίσιο

Το project χρησιμοποιεί custom validation annotations (`@ValidFirstName`, `@ValidLastName`, `@ValidAmka`, `@ValidDateOfBirth`)
με validation groups σε δύο φάσεις:

- `FirstOrder` → structural checks (`@NotBlank`, `@Size`, `@NotNull`, `@Past`)
- `SecondOrder` → business logic, regex patterns, custom validators

Αρχικά, τα custom annotations περιείχαν **και** τους presence checks (`@NotBlank`, `@NotNull`) **και** τους value checks (size, regex):

```java
// Αρχική υλοποίηση
@NotBlank(message = "{name.required}", groups = FirstOrder.class)
@Size(min=2, max=20, message = "{name.size}", groups = FirstOrder.class)
@Constraint(validatedBy = FirstNameValidator.class)
public @interface ValidFirstName { ... }
```

Αυτό δούλευε καλά για το **POST (Create)** endpoint με `@Validated(ValidationGroupSequence.class)`.

---

## Πρόβλημα

Κατά την υλοποίηση του **PATCH (Update)** endpoint, προέκυψε conflict:

- Με `@Valid` → τρέχει μόνο το `Default` group, τα `FirstOrder`/`SecondOrder` αγνοούνται εντελώς
- Με `@Validated(ValidationGroupSequence.class)` → το `@NotBlank` απορρίπτει τα `null` πεδία, σπάζοντας το partial update

Το PATCH endpoint θέλουμε να δέχεται μερικά δεδομένα (π.χ. μόνο `{ "active": false }`),
αλλά ταυτόχρονα αν σταλεί κάποιο πεδίο να τρέχει κανονικά το validation.

---

## Εναλλακτικές που εξετάστηκαν

**1. Ξεχωριστά annotations για Update** (`@ValidFirstNameUpdate`)
- ❌ Boilerplate — διπλά annotations για κάθε πεδίο
- ❌ Δύσκολη συντήρηση

**2. `required` attribute στο annotation** (`@ValidFirstName(required = false)`)
- ❌ Ο custom validator δεν μπορεί να χειριστεί `@NotBlank`/`@Size` conditionally
- ❌ Χάνονται τα ξεχωριστά μηνύματα λάθους

**3. `JsonNullable` για PATCH semantics**
- ✅ Η πιο complete λύση μακροπρόθεσμα
- ❌ Premature για το τρέχον στάδιο — το API δεν έχει σταθεροποιηθεί ακόμα
- 📌 Καταγράφεται ως Phase 2 evolution

**4. Διαχωρισμός presence από value validation** ✅ ΕΠΙΛΕΧΘΗΚΕ
- Τα custom annotations κρατούν μόνο value validation (size, regex, format)
- Τα presence checks (`@NotBlank`, `@NotNull`) μπαίνουν ξεχωριστά μόνο στο `SaveDTO`

---

## Απόφαση

Τα custom annotations **δεν πρέπει να αναλαμβάνουν** την ευθύνη του "είναι υποχρεωτικό το πεδίο".
Αυτή είναι ευθύνη του **DTO** που γνωρίζει το context (Create vs Update).

```
presence constraint ≠ value constraint
```

### Αλλαγές

**Custom annotations** → αφαιρούνται οι presence checks:

| Annotation | Αφαιρείται |
|---|---|
| `@ValidFirstName` | `@NotBlank` |
| `@ValidLastName` | `@NotBlank` |
| `@ValidAmka` | `@NotBlank` |
| `@ValidDateOfBirth` | `@NotNull`, `@Past` |

**`BeneficiarySaveDTO`** → προστίθενται οι presence checks ξεχωριστά:

```java
@NotBlank(message = "{name.required}", groups = FirstOrder.class)
@ValidFirstName(groups = SecondOrder.class)
String firstName,
```

**`BeneficiaryUpdateDTO`** → μόνο value validation, χωρίς presence:

```java
@ValidFirstName(groups = SecondOrder.class)
String firstName,
```

---

## Συνέπειες

✅ PATCH δέχεται partial updates χωρίς να σπάει
✅ Αν σταλεί πεδίο, τρέχει κανονικά το size/regex validation
✅ POST συνεχίζει να απαιτεί όλα τα υποχρεωτικά πεδία
✅ Καθαρός διαχωρισμός ευθυνών

---

## Future (Phase 2)

Όταν το API σταθεροποιηθεί, εξετάζεται εισαγωγή `JsonNullable` για explicit διαχωρισμό
μεταξύ `null` = "δεν στάλθηκε" και `null` = "σβήσε την τιμή".