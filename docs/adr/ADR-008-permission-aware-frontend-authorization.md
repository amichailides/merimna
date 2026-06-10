# ADR 0001: Permission-aware frontend authorization

## Status

Accepted

## Context

Merimna supports authenticated users with broad roles such as `ADMIN` and `STAFF`.

However, frontend behavior cannot be based only on roles. Staff users may have different employee positions, such as
house manager or caregiver, and those positions may allow different actions inside the same feature area.

For example, both a house manager and a caregiver may be allowed to view medication plans, but only the house manager
may be allowed to create or update them.

Merimna also has data access scope rules in the backend. An employee can access beneficiaries that belong to house units
where the employee has an active assignment or an active placement.

Therefore, authorization has two separate concerns:

- permissions define what actions a user may perform
- scope defines which data the user may access

## Decision

The frontend will be permission-aware.

Navigation items and page actions will be rendered based on the authenticated user's permissions, not only based on the
user's role.

The backend remains the source of truth for authorization and will enforce access through endpoint-level authorization
rules, such as `@PreAuthorize`.

The frontend will use permissions only to improve user experience by hiding unavailable navigation items and actions.

## Rules

- Sidebar items are shown based on feature-level read permissions.
- Page actions are shown based on action-level permissions.
- The frontend must not rely on role checks alone for feature visibility.
- The frontend must not calculate data access scope.
- Data scope remains enforced by the backend.
- Backend authorization remains mandatory even when the frontend hides unavailable actions.

## Examples

A user with `BENEFICIARY_READ` can see beneficiary-related care information, including Medication Plans.

A user with `BENEFICIARY_UPDATE` can see actions that modify beneficiary-related care information, such as creating,
editing, or ending a medication plan.

A caregiver with `BENEFICIARY_READ` but without `BENEFICIARY_UPDATE` can view medication plans but will not see Create,
Edit, or End actions.

If a user manually calls a restricted endpoint, the backend still rejects the request.

> Note: Medication Plans are currently treated as beneficiary-related care data. Therefore, medication-plan visibility and
> actions are controlled through beneficiary permissions rather than separate medication-plan permissions.

This avoids scattering role checks such as `user.role === "ADMIN"` throughout the frontend.

It allows different staff positions to share the same pages while seeing different available actions.

It keeps the frontend aligned with the backend permission model.

It improves user experience without weakening backend security.

## Implementation Notes

The access token should include the user's permissions.

The frontend auth user model should include a permissions array.

Permission constants should be defined in a shared frontend file to avoid hardcoded strings across components.

The auth layer should expose a helper such as `hasPermission(permission)`.

Navigation configuration should include required permissions for each item.

Page-level actions should check permissions before rendering buttons or controls.

Permission changes must invalidate the user's active refresh-token sessions.

If a user's role, employee position, or permissions change, the backend must revoke all active refresh tokens for that
user. This prevents old access tokens from being renewed with outdated permissions and ensures that the next
authentication flow receives the updated permission set.

## Related Domain Rules

Employee assignment defines the employee's primary organizational house-unit scope.

Employee placement defines temporary operational access to an additional house unit.

Active assignment and active placement together determine which beneficiaries an employee can access.

The frontend does not calculate this scope. It renders the data returned by the backend.