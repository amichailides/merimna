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

Initially, custom annotations contained **both** presence checks (`@NotBlank`, `@NotNull`) **and** value checks (size, regex):

```java
// Initial implementation
@NotBlank(message = "{name.required}", groups = FirstOrder.class)
@Size(min=2, max=20, message = "{name.size}", groups = FirstOrder.class)
@Constraint(validatedBy = FirstNameValidator.class)
public @interface ValidFirstName { ... }
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
- Custom annotations keep only value validation (size, regex, format)
- Presence checks (`@NotBlank`, `@NotNull`) are placed separately only in `SaveDTO`

---

## Decision

Custom annotations **should not assume** the responsibility of "is the field required".
This is the responsibility of the **DTO** which knows the context (Create vs Update).

```
presence constraint ≠ value constraint
```

### Changes

**Custom annotations** → presence checks are removed:

| Annotation | Removed |
|---|---|
| `@ValidFirstName` | `@NotBlank` |
| `@ValidLastName` | `@NotBlank` |
| `@ValidAmka` | `@NotBlank` |
| `@ValidDateOfBirth` | `@NotNull`, `@Past` |

**`BeneficiarySaveDTO`** → presence checks are added separately:

```java
@NotBlank(message = "{name.required}", groups = FirstOrder.class)
@ValidFirstName(groups = SecondOrder.class)
String firstName,
```

**`BeneficiaryUpdateDTO`** → only value validation, without presence:

```java
@ValidFirstName(groups = SecondOrder.class)
String firstName,
```

---

## Consequences

*   ✅ PATCH accepts partial updates without breaking
*   ✅ If a field is sent, size/regex validation runs normally
*   ✅ POST continues to require all mandatory fields
*   ✅ Clean separation of concerns

---

## Future (Phase 2)

When the API stabilizes, the introduction of `JsonNullable` is considered for explicit separation
between `null` = "not sent" and `null` = "delete value".