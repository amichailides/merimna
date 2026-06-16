# Frontend Visual Guidelines

## Typography

- Page and section titles use `font-semibold text-slate-900`.
- Primary domain values inside panels use `font-semibold text-slate-950`.
- Secondary contextual text uses `font-medium text-slate-600`.
- Metadata values use `font-medium text-slate-700`.
- Labels use `text-xs font-medium text-slate-500`.

## Cards

- Main content cards use `rounded-xl border-slate-200 bg-white shadow-sm`.
- Domain panels inside cards use `rounded-xl border-teal-100 bg-teal-50/20`.
- Empty states inside cards use dashed slate borders and muted slate backgrounds.

## Component usage

### Structured domain cards

Use `EmployeeInfoCard` for structured domain cards, such as placement and assignment.

### Compact informational cards

Use compact custom layouts for simple identity-adjacent information, such as address cards.

### Metadata fields

Use `InfoItem` for small label/value metadata, not for primary titles or headline values.

## Color

- Teal is the Merimna accent color.
- Emerald/green is reserved for active or success states.
- Slate is used for neutral text, borders, and muted UI.
