# Frontend Visual Guidelines

Merimna uses a quiet, structured, light-mode interface for operational care-management workflows.

The UI should feel flat, compact, data-dense, and breathable. Prefer clear page structure, readable information hierarchy, and subtle dividers over card-heavy layouts.

Avoid chunky cards, heavy shadows, gradients, excessive rounding, decorative surfaces, and unnecessary component abstractions within page content.

## App shell exception

The “avoid excessive rounding” rule applies to content-level elements inside the page: sections, panels, groups, and repeated UI blocks.

It does not apply to the outer application shell.

The app shell may use a persistent sidebar alongside a single rounded, bordered main content panel. This is a structural page container, not a card pattern.


## Core direction

Use:

* white and slate surfaces
* quiet metadata
* subtle dividers
* hairline borders
* compact spacing
* consistent typography
* clear alignment

Avoid:

* card-heavy layouts
* decorative panels
* nested boxes
* random widgets
* large empty illustrations
* heavy timeline/feed styling

## Typography

Use this as the default frontend typography scale.

| Role                         | Classes                                                                |
|------------------------------|------------------------------------------------------------------------|
| Page title                   | `text-[18px] font-medium text-slate-900`                               |
| Page subtitle                | `text-[13px] text-slate-400`                                           |
| Section title                | `text-[13px] font-medium text-slate-700`                               |
| Body text                    | `text-[13px] text-slate-900`                                           |
| Secondary text               | `text-[13px] text-slate-500`                                           |
| Label                        | `text-[11px] text-slate-400`                                           |
| Eyebrow / compact item label | `text-[11px] font-semibold uppercase tracking-[0.04em] text-slate-500` |
| Helper / date text           | `text-[12px] text-slate-400`                                           |

Do not introduce one-off text sizes without a clear reason.

Use opacity-based text colors sparingly. Prefer solid slate colors for normal text. A narrow exception is
`text-slate-950/80` for softened primary item titles in compact editorial sections.

## Layout

Prefer page architecture over isolated cards.

Use:

* sections
* rows
* metadata groups
* rails
* subtle dividers
* compact activity/timeline items

Avoid making every domain concept into a card.

Cards are not forbidden, but they should not be the default layout strategy.

## Surfaces and dividers

Use borders, spacing, and alignment before adding backgrounds.

Prefer:

* subtle dividers
* hairline borders
* quiet section separation
* whitespace between content groups

Avoid:

* shadows
* gradients
* thick borders
* nested card backgrounds
* large rounded containers

## Detail pages

Detail pages should feel editorial, not dashboard-like.

Prefer:

* a clear profile/header area
* primary content in the main column
* supporting metadata in a quiet rail or secondary column
* compact history/activity sections
* subtle dividers between content groups

Avoid:

* symmetrical cards created only to fill space
* empty cards for missing optional data
* separate boxed panels for every domain concept

## Activity and timeline content

Use activity/timeline UI for compact chronological context.

Current reusable primitive:

* `src/components/reui/timeline.tsx`

Timeline usage should remain restrained:

* small markers
* compact item titles
* quiet timestamps
* subtle separators
* no oversized indicators
* no social-feed styling

## Empty states

Empty states should be quiet and useful.

Prefer short, human copy.

Avoid:

* large empty boxes
* warning-like styling unless there is a real issue
* decorative illustrations
* exaggerated empty-state text

## Component discipline

Do not create a new component for every visual variation.

Create components when they represent a stable reusable pattern.

Prefer existing primitives before adding new abstractions.

Avoid random abstractions that only hide simple markup.

## Naming language

Prefer:

* section
* row
* rail
* metadata group
* activity item
* timeline item

Avoid using these as default abstractions:

* card
* widget
* panel

