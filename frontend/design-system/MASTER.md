# Employee Details Page Design Rules

## Page role

The employee details page explains who the employee is, where they officially belong, where they are currently placed if different, and what recently happened about them.

It should not feel like a dashboard. It should feel like a clean profile/detail page.

## Content sections

The page contains:

1. Employee profile header
2. Organizational assignment
3. Current placement
4. Recent activity

## Layout rules

If the employee has both an active assignment and an active placement:

- Show assignment and placement side by side
- Recent activity must move below them
- This preserves a clear distinction between official unit and temporary placement

If the employee has no active placement:

- Do not show a large empty placement card just to keep symmetry
- Prefer showing assignment and recent activity side by side if the layout feels balanced
- Empty placement information may be omitted or shown as subtle inline context only if needed

## Section naming

Preferred section names:

- `Organizational assignment`
- `Current placement`
- `Recent activity`

Avoid:

- `Cards`
- `Widgets`
- `Panels`
- `Work location` if it blurs assignment vs placement
- `Activity feed` if it sounds too social/productivity-app-like

## Assignment section

Assignment means the employee's official organizational house unit.

Visual priority:

- Important, stable, structural information
- Should feel slightly more foundational than activity
- Must not be confused with placement

## Placement section

Placement means temporary current placement.

Visual priority:

- Important only when active
- Should be shown near assignment when active
- Should make the difference from assignment clear

## Recent activity section

Recent activity is supporting context.

Rules:

- It should not compete visually with assignment/placement
- It can sit below when assignment and placement both exist
- It may sit beside assignment when there is no active placement
- Keep it narrow and readable
- Avoid heavy timeline styling

## Empty states

Empty states should be quiet and human.

Prefer:

- “No active placement”
- “No active assignment”
- “No recent activity”

Avoid:

- Large empty boxes
- Warning-like empty states unless there is a real issue
- Decorative illustrations

## Visual tone

Use the global Merimna design system.

The page should feel:

- calm
- clear
- structured
- professional
- restrained

It should not feel:

- like a generic SaaS dashboard
- like a card grid
- like a landing page
- like a Dribbble concept