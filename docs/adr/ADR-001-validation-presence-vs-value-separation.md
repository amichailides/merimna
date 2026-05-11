# ADR-001: Separation of Presence Validation from Value Validation in Custom Annotations

## Status

Accepted

## Date

2026-02-28

---

## Context

The project uses Bean Validation with explicit validation groups and a group sequence to control validation order.

The intended validation strategy is:

- `Default` → standard Bean Validation fallback group; Merimna does not intentionally place application DTO validation rules in this group
- `FirstOrder` → required input and basic structural guards, such as `@NotBlank`, `@NotNull`, `@OptionalNotBlank`, missing nested DTO objects, basic date presence, and class-level presence rules such as `@AtLeastOnePhonePresent`
- `SecondOrder` → value validation, regex patterns, and reusable custom validators such as `@ValidFirstName`, `@ValidLastName`, `@ValidAmka`, `@ValidEmail`, `@ValidMobile`, `@ValidLandline`, `@ValidAfm`, and `@ValidGreekLatinText`

Validation runs in waterfall order through `ValidationGroupSequence`:

```java
@GroupSequence({Default.class, FirstOrder.class, SecondOrder.class})
public interface ValidationGroupSequence {
}
```

This means:

1. fallback/default constraints are evaluated first, if any exist
2. required/basic input errors are collected together in `FirstOrder`
3. format/value errors are evaluated only if `FirstOrder` passes

Initially, custom annotations contained both presence checks and value checks.

Example:

```java
// Initial implementation
@NotBlank(message = "{name.required}", groups = FirstOrder.class)
@Size(min = 2, max = 20, message = "{name.size}", groups = FirstOrder.class)
@Constraint(validatedBy = FirstNameValidator.class)
public @interface ValidFirstName {
    ...
}
```

This worked well for create requests, but it made reusable validators harder to use safely in PATCH/update DTOs.

---

## Problem

PATCH/update endpoints need partial update semantics:

- `null` means "field not provided / no update"
- blank string means "field was provided but contains an invalid value"
- valid value means "apply update and include it in diff/audit tracking"

When requiredness checks are embedded inside reusable custom annotations, the same annotation cannot be reused cleanly for both create and update DTOs.

For example:

```java
@ValidFirstName
String firstName;
```

In a create DTO, `firstName` may be required.

In an update DTO, `firstName = null` should be allowed because it means "do not update this field".

If `@ValidFirstName` internally contains `@NotBlank`, then PATCH requests break because omitted fields are rejected.

There was also a group-related issue: when validation is triggered without the project group sequence, only the Bean Validation `Default` group runs and `FirstOrder` / `SecondOrder` constraints are not evaluated.

Therefore, DTOs must be validated through the project group sequence where waterfall validation is required.

---

## Considered Options

### 1. Separate annotations for update DTOs

Example:

```java
@ValidFirstName
@ValidFirstNameUpdate
```

Rejected.

- Adds boilerplate
- Duplicates annotations and validators
- Makes maintenance harder as validation rules evolve

---

### 2. Add a `required` attribute to custom annotations

Example:

```java
@ValidFirstName(required = false)
```

Rejected.

- The custom validator cannot conditionally control composed annotations such as `@NotBlank` or `@Size`
- Error messages become harder to keep precise
- Requiredness and value validation remain mixed together

---

### 3. Use `JsonNullable` for PATCH semantics

Accepted as a possible future evolution, but rejected for the current stage.

`JsonNullable` can distinguish between:

- field not sent
- field sent as `null`
- field sent with a value

This is the most complete long-term model, but it would currently add complexity:

- wrapper types in DTOs
- MapStruct configuration
- OpenAPI schema changes
- more complex controller/service handling

The current API does not yet require explicit "clear this value by sending null" semantics.

---

### 4. Separate presence validation from value validation

Selected.

Reusable custom annotations validate values, not requiredness.

Requiredness is declared explicitly at the DTO level, depending on the use case.

---

## Decision

Custom validation annotations must not decide whether a field is required.

Requiredness belongs to the DTO context.

Create DTOs declare required fields explicitly:

```java
@NotBlank(message = "{firstName.required}", groups = FirstOrder.class)
@ValidFirstName(groups = SecondOrder.class)
String firstName;
```

Update DTOs omit requiredness constraints so `null` can mean "no update":

```java
@ValidFirstName(groups = SecondOrder.class)
String firstName;
```

Reusable custom validators should generally allow `null`:

```text
null -> valid
```

This allows the same validator to be reused in PATCH/update DTOs.

For string fields where blank has no valid domain meaning, reusable validators should reject blank values:

```text
""    -> invalid
"   " -> invalid
```

This is value validation, not presence validation, because the field was provided but contains an invalid value.

Reusable string validators should generally follow this rule:

```text
null            -> valid, because the field may be omitted in PATCH requests
blank string    -> invalid, when blank has no valid domain meaning
non-blank value -> validate format, range, or business rule
```

---

## Validation Group Strategy

Merimna uses the following convention:

### `Default`

Do not intentionally use this group for application DTO validation rules.

It remains first in `ValidationGroupSequence` as the standard Bean Validation fallback group for constraints without an explicit group.

### `FirstOrder`

Use this group for required input and basic structural guards.

Examples:

```java
@NotNull(groups = FirstOrder.class)
@NotBlank(groups = FirstOrder.class)
@OptionalNotBlank(groups = FirstOrder.class)
@AtLeastOnePhonePresent(groups = FirstOrder.class)
```

This includes:

- missing nested objects
- missing simple fields
- blank required fields
- optional fields that must not be blank if provided
- class-level presence rules such as "at least one phone"
- basic date presence

Example:

```java
@NotNull(message = "{address.required}", groups = FirstOrder.class)
@Valid
AddressDTO address;
```

### `SecondOrder`

Use this group for value correctness after required/basic checks pass.

Examples:

```java
@ValidFirstName(groups = SecondOrder.class)
@ValidLastName(groups = SecondOrder.class)
@ValidEmail(groups = SecondOrder.class)
@ValidMobile(groups = SecondOrder.class)
@ValidLandline(groups = SecondOrder.class)
@ValidAmka(groups = SecondOrder.class)
@ValidAfm(groups = SecondOrder.class)
@ValidGreekLatinText(groups = SecondOrder.class)
@Pattern(groups = SecondOrder.class)
```

This keeps validation UI-friendly:

- all basic form errors are returned together
- format/value errors are evaluated only after required/basic input is present
- blank values do not produce both "required" and "invalid format" errors

---

## Examples

### Create DTO

```java
public record BeneficiaryCreateDTO(

        @NotBlank(message = "{firstName.required}", groups = FirstOrder.class)
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @NotBlank(message = "{lastName.required}", groups = FirstOrder.class)
        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @NotBlank(message = "{amka.required}", groups = FirstOrder.class)
        @ValidAmka(groups = SecondOrder.class)
        String amka,

        @NotNull(message = "{address.required}", groups = FirstOrder.class)
        @Valid
        AddressDTO permanentAddress
) {}
```

### Update DTO

```java
public record BeneficiaryUpdateDTO(

        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @ValidAmka(groups = SecondOrder.class)
        String amka,

        @Valid
        AddressUpdateDTO permanentAddress
) {}
```

### Optional PATCH field with raw pattern

For optional PATCH fields that use raw Bean Validation annotations instead of custom validators, use `@OptionalNotBlank` in `FirstOrder` and the value rule in `SecondOrder`.

```java
@OptionalNotBlank(message = "{houseUnit.code.blank}", groups = FirstOrder.class)
@Pattern(
        regexp = ValidationPatterns.HOUSE_UNIT_CODE,
        message = "{houseUnit.code.invalid}",
        groups = SecondOrder.class
)
String code;
```

### Custom string validator behavior

```java
public class ValidEmailValidator implements ConstraintValidator<ValidEmail, String> {

    @Override
    public boolean isValid(String value, ConstraintValidatorContext context) {
        if (value == null) {
            return true;
        }

        String normalized = value.trim();

        if (normalized.isBlank()) {
            return false;
        }

        return normalized.matches(ValidationPatterns.EMAIL);
    }
}
```

The same nullable-but-blank-invalid rule is applied to reusable string validators such as:

- `@ValidFirstName`
- `@ValidLastName`
- `@ValidEmail`
- `@ValidMobile`
- `@ValidLandline`
- `@ValidAfm`
- `@ValidGreekLatinText`

---

## Changes

Presence checks were removed from reusable custom annotations.

| Annotation          | Removed             |
|---------------------|---------------------|
| `@ValidFirstName`   | `@NotBlank`         |
| `@ValidLastName`    | `@NotBlank`         |
| `@ValidAmka`        | `@NotBlank`         |
| `@ValidDateOfBirth` | `@NotNull`, `@Past` |

Later cleanup extended the same rule to reusable string validators:

- `@ValidEmail`
- `@ValidMobile`
- `@ValidLandline`
- `@ValidAfm`
- `@ValidGreekLatinText`

These validators allow `null`, reject blank values when blank has no valid domain meaning, and validate non-blank values.

---

## Consequences

✅ PATCH accepts partial updates because `null` remains valid in reusable validators  
✅ Blank strings can be rejected when they are invalid domain values  
✅ POST/create requests continue to require mandatory fields through DTO-level `@NotBlank` / `@NotNull` constraints  
✅ Reusable validators work consistently in both create and update DTOs  
✅ Validation errors are more UI-friendly because required/basic errors are returned together before format/value validation runs  
✅ Update diff/audit tracking becomes safer because blank values are not silently accepted or ignored  
✅ Validation responsibility remains separated: DTOs decide requiredness, validators decide value validity  
✅ The `Default` group remains available as Bean Validation fallback but is not intentionally used for application DTO rules

---

## Revision Notes

### 2026-05-10

Clarified nullable custom validation semantics for reusable validators.

Custom validation annotations must not decide whether a field is required. Requiredness remains the responsibility of DTO-level constraints such as `@NotBlank` and `@NotNull`.

Reusable custom validators should generally allow `null` so they can be used in PATCH DTOs where `null` means "field not provided / no update".

For string fields where blank has no valid domain meaning, custom validators may reject blank values such as `""` or `"   "`. This is treated as value validation, not presence validation, because the field was provided but contains an invalid value.

### 2026-05-11

Clarified the intended validation group strategy.

`Default` remains part of `ValidationGroupSequence` as the standard Bean Validation fallback group, but Merimna does not intentionally place application DTO validation rules in `Default`.

Application DTO validation rules should use:

- `FirstOrder` for required input and basic structural guards, including missing nested objects, missing simple fields, blank checks, `@OptionalNotBlank`, basic date presence, and class-level presence rules such as `@AtLeastOnePhonePresent`.
- `SecondOrder` for value validation, patterns, and reusable custom validators.

This keeps create/update validation predictable and UI-friendly: basic form errors are returned together, while format/value errors are evaluated only after the basic input guards pass.

## Future (Phase 2)

When the API stabilizes, `JsonNullable` may be introduced in PATCH/Update DTOs to explicitly distinguish between:

- field not sent
- field sent with null (clear value)
- field sent with value (update)

Deferred to Phase 2 as no current use case requires explicit null semantics.

Introducing `JsonNullable` now would add complexity:

- wrapper types
- mapper configuration
- OpenAPI schema changes
- additional service-layer handling

without sufficient benefit at the current stage.
