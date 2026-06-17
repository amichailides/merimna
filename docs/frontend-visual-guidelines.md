# Frontend Visual Guidelines

> Design direction: Linear-inspired light mode — flat, compact, data-dense but breathable.
> No chunky cards, shadows, gradients, or excessive rounding.

---

## Typography

| Role | Classes |
|---|---|
| Page title | `text-[18px] font-medium text-slate-900` |
| Page subtitle | `text-[13px] text-slate-400` |
| Section title | `text-[13px] font-medium text-slate-900` |
| Content / body | `text-[13px] text-slate-900` |
| Secondary text | `text-[13px] text-slate-500` |
| Labels | `text-[11px] text-slate-400` |
| Metadata values | `text-[11px] font-medium text-slate-700` |
| Primary domain values (detail panels) | `text-[13px] font-semibold text-slate-950` |

---

## Borders & Separators

- Subtle dividers between rows and sections: `border-slate-100`
- Component borders (inputs, segmented controls): `border-slate-100`
- Domain panel borders (teal-tinted sections): `border-teal-100`
- Avoid `border-slate-200` for regular component borders; reserve it for empty states or stronger separators where more contrast is needed.

---

## Spacing & Layout

- Page content max width: `max-w-3xl` or `max-w-4xl` depending on density
- Vertical rhythm between page sections: `space-y-4`
- Padding inside panels: `px-4 py-3` or `p-4`
- Row padding: `px-1.5 py-3`

---

## Cards & Containers

- **Avoid** wrapping lists or sections in shadcn `Card` with `shadow-sm`.
- **Avoid** `rounded-xl` on containers — use `rounded-md` or `rounded-lg` at most.
- Domain panels inside detail pages: `rounded-lg border border-teal-100 bg-teal-50/20`
- Empty states: dashed `border-slate-200`, muted `bg-slate-50`, `text-[13px] text-slate-400`

---

## Lists & Rows

- No outer box or wrapper — rows live directly on the page background.
- Row separator: `border-b border-slate-100`, removed on last child via `last:border-b-0`
- Row hover: `hover:bg-teal-50/50 hover:translate-x-0.5` with `transition-all duration-150`
- Row border radius: `rounded-md` (for hover background clipping)
- Rows are `<Link>` elements with `group` for coordinated child transitions.
- Optional subtle entrance animation may be used for list rows (e.g. fade + translateY), but avoid heavy or distracting motion.

---

## Avatars

- Size: `h-[34px] w-[34px]` with `rounded-full`
- Active employee: `bg-teal-50 text-teal-800`
- Inactive employee: `bg-slate-100 text-slate-400`
- Initials font: `text-[11px] font-medium`
- Hover scale: `group-hover:scale-105 transition-transform duration-150`

---

## Status Badges

- Shape: `rounded-full` — always pill, never square
- Padding: `px-2 py-0.5`
- Font: `text-[11px] font-medium`
- Active: `bg-emerald-50 text-emerald-700` with a `h-1.5 w-1.5 rounded-full bg-emerald-500` dot
- Inactive: `bg-slate-100 text-slate-500` — no dot
- Emerald/green is reserved exclusively for active or success states.

---

## Buttons

- Primary action: `bg-teal-700 text-white hover:bg-teal-800 text-[13px] font-medium rounded-md`
- Height: `h-8` with `px-3`
- Icons inside buttons: `h-3.5 w-3.5 shrink-0` Lucide icons with `gap-1.5`
- Use shadcn `Button` for actions, but override primary action styling explicitly with Merimna classes.

---

## Filter Bar

- Layout: `flex items-center gap-2` — flat, no wrapper card or border
- Search input: `h-8 text-[13px] border-slate-100 bg-slate-50` with Lucide `Search` icon (`h-3.5 w-3.5`)
- Focus ring: `focus-visible:ring-1 focus-visible:ring-teal-500/30 focus-visible:border-teal-400`
- Status control: segmented button group, not a `Select` dropdown
  - Container: `flex items-center rounded-md border border-slate-100 overflow-hidden`
  - Inactive tab: `bg-white text-slate-500 hover:bg-slate-50 hover:text-slate-700`
  - Active tab: `bg-slate-200 text-slate-900 font-medium`
  - Tab height: `h-8 px-3 text-[13px]`
  - Separator between tabs: `border-r border-slate-100 last:border-r-0`
- Additional filters (position, house unit, etc.): `Filters` button with `SlidersHorizontal` icon — opens a popover, shows badge count when active (e.g. `Filters · 2`)

---

## Dates

- Format: `D-M-YYYY` via `formatDate` utility (e.g. `9-4-2026`). No leading zeros — matches natural Greek date writing.

---

## Contact & Metadata Rows

- Separator between inline metadata items: `·` dot character
- Icons: Lucide, `h-3.5 w-3.5 text-slate-400`

---

## Color Palette Summary

| Role | Color |
|---|---|
| Accent | Teal (`teal-700`, `teal-50`, `teal-100`) |
| Active / Success | Emerald (`emerald-50`, `emerald-500`, `emerald-700`) |
| Neutral text & borders | Slate |
| Hover tint on rows | `teal-50/50` |
| Avatar (active) | `teal-50` bg / `teal-800` text |
| Avatar (inactive) | `slate-100` bg / `slate-400` text |

---

## Component Reference

| Component | Purpose |
|---|---|
| `EmployeeInfoCard` | Structured domain cards (placement, assignment) |
| `InfoItem` | Small label/value metadata pairs |
| `ListPagination` | Shared pagination for all list pages |
| `EmployeeListRow` | Borderless clickable row with avatar, name, position, badge |
| `EmployeeListFilters` | Flat filter bar with search + segmented status + Filters popover |