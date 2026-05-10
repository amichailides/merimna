# ADR-001: Separation of Presence Validation from Value Validation in Custom Annotations

## Status

Accepted

## Date

2026-02-28

---

## Context

The project uses custom validation annotations (`@ValidFirstName`, `@ValidLastName`, `@ValidAmka`, `@ValidDateOfBirth`)
with validation groups in two phases:

- `FirstOrder` → structural checks (`@NotBlank`, `@Size`, `@NotNull`, `@Past`)
- `SecondOrder` → business logic, regex patterns, custom validators

Initially, custom annotations contained **both** presence checks (`@NotBlank`, `@NotNull`) **and** value checks (size,
regex):

```java
// Initial implementation
@NotBlank(message = "{name.required}", groups = FirstOrder.class)
@Size(min = 2, max = 20, message = "{name.size}", groups = FirstOrder.class)
@Constraint(validatedBy = FirstNameValidator.class)
public @interface ValidFirstName { ...
}
```

This worked well for the **POST (Create)** endpoint using `@Validated(ValidationGroupSequence.class)`.

---

## Problem

During the implementation of the **PATCH (Update)** endpoint, a conflict arose:

- With `@Valid` → only the `Default` group runs, `FirstOrder`/`SecondOrder` are completely ignored.
- With `@Validated(ValidationGroupSequence.class)` → `@NotBlank` rejects `null` fields, breaking partial updates.

The PATCH endpoint needs to accept partial data (e.g., only `{ "active": false }`),
but at the same time, if a field is sent, it must run normal validation.

---

## Considered Options

**1. Separate annotations for Update** (`@ValidFirstNameUpdate`)

- ❌ Boilerplate — duplicate annotations for every field
- ❌ Difficult maintenance

**2. `required` attribute in annotation** (`@ValidFirstName(required = false)`)

- ❌ The custom validator cannot handle `@NotBlank`/`@Size` conditionally
- ❌ Separate error messages are lost

**3. `JsonNullable` for PATCH semantics**

- ✅ The most complete solution long-term
- ❌ Premature for the current stage — the API has not stabilized yet
- 📌 Recorded as Phase 2 evolution

**4. Separation of presence from value validation** ✅ SELECTED

- Custom annotations keep value validation and remain nullable for PATCH reuse
- Requiredness checks (`@NotBlank`, `@NotNull`) are placed separately in DTOs that require presence

---

## Decision

Custom validation annotations should not decide whether a field is required.

Requiredness is the responsibility of the DTO context:

- Create DTOs use `@NotBlank`, `@NotNull`, or similar constraints for mandatory fields.
- Update DTOs omit required constraints so `null` can mean "field not provided / no update".

Reusable custom validators should remain nullable.

For string fields where blank has no valid domain meaning, custom validators may reject blank values. This is considered
value validation, not presence validation, because the field was provided but contains an invalid value.

In practice, reusable string validators should generally follow this rule:

- `null` → valid, because the field may be omitted in PATCH requests
- blank string (`""` or `"   "`) → invalid, when blank has no valid domain meaning
- non-blank value → validate format, range, or business rule

### Changes

**Custom annotations** → presence checks are removed:

| Annotation          | Removed             |
|---------------------|---------------------|
| `@ValidFirstName`   | `@NotBlank`         |
| `@ValidLastName`    | `@NotBlank`         |
| `@ValidAmka`        | `@NotBlank`         |
| `@ValidDateOfBirth` | `@NotNull`, `@Past` |

**`BeneficiarySaveDTO`** → requiredness is declared separately in the DTO:

```java

@NotBlank(message = "{name.required}", groups = FirstOrder.class)
@ValidFirstName(groups = SecondOrder.class)
String firstName,
```

**`BeneficiaryUpdateDTO`** → nullable value validation, without requiredness:

```java

@ValidFirstName(groups = SecondOrder.class)
String firstName,
```

---

## Consequences

✅ PATCH accepts partial updates because `null` remains valid in reusable validators  
✅ Blank strings can be rejected when they are invalid domain values  
✅ POST continues to require mandatory fields through `@NotBlank` / `@NotNull`  
✅ Reusable validators can work consistently in both create and update DTOs  
✅ Update diff/audit tracking becomes safer because blank values are not silently accepted or ignored  
✅ Validation responsibility remains separated: DTOs decide requiredness, validators decide value validity

---

## Revision Notes

### 2026-05-10

Clarified nullable custom validation semantics for reusable validators.

Custom validation annotations must not decide whether a field is required. Requiredness remains the responsibility of
DTO-level constraints such as `@NotBlank` and `@NotNull`.

Reusable custom validators should generally allow `null` so they can be used in PATCH DTOs where `null` means "field not
provided / no update".

For string fields where blank has no valid domain meaning, custom validators may reject blank values such as `""` or
`"   "`. This is treated as value validation, not presence validation, because the field was provided but contains an
invalid value.

## Future (Phase 2)

When the API stabilizes, `JsonNullable` may be introduced in PATCH/Update DTOs
to explicitly distinguish between:

- field not sent
- field sent with null (clear value)
- field sent with value (update)

Deferred to Phase 2 as no current use case requires explicit null semantics.
Introducing JsonNullable now would add complexity (wrapper types,
MapStruct config, OpenAPI schema changes) without sufficient benefit.
