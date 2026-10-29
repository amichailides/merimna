# ADR-010: Scope-Aware Authorization with Configurable Application Roles

## Status

Accepted

## Date

2026-10-28

---

## Context

Merimna is designed for organizations that operate supported-living facilities across several house units.

In such organizations, staff work in one or more units, sometimes temporarily cover another unit, and some people
supervise several units without working in any of them.

The current authorization model attaches permissions directly to `EmployeePosition` and distinguishes users only as
`ADMIN` or `STAFF`. This has several structural limitations:

- A professional job title determines application privileges, so changing an employee's position silently changes what
  they can do.
- Permission sets can be defined per position, but there are no Application Roles that can be configured
  independently of job titles. Responsibilities can only be configured by changing what a position means.
- Permissions are derived from the employee's position, while accessible house units are determined separately through
  active Assignments and Placements. Where house-unit scope checks are enforced, the same position-derived permissions
  apply across all accessible units, regardless of the employee's responsibilities within each unit.
- `ADMIN` receives every permission and bypasses house-unit scope.
- Authorization checks are spread across controllers and services: permission checks are declared on controller
  endpoints, while house-unit checks are applied in individual services and not consistently across all operations.
  This makes consistent enforcement difficult.
- The beneficiary details response includes medication, allergy and legal-representative data under
  `BENEFICIARY_READ`, without separate authorization for those nested data categories.

The authorization model needs to support configurable responsibilities, explicit resource scope, supervision without
fictitious workforce records, and strong protection against privilege escalation.

---

## Problem

How should Merimna model application access so that organizations can configure responsibilities without code changes,
while permissions remain scoped to the resources they apply to and administrative authority cannot be acquired or
removed through ordinary operational actions?

---

## Decision

### 1. Platform-defined permissions, organization-defined Application Roles

The platform defines a catalogue of business-capability permissions, such as `MEDICATION_READ`, `MEDICATION_MANAGE`
and `BENEFICIARY_TRANSFER`. Permissions describe business capabilities rather than CRUD operations.

Organizations define `ApplicationRole`s as named collections of these permissions. Initial roles, such as Organization
Owner, Coordinator, House Manager and Caregiver, are configurable templates rather than a hardcoded professional
hierarchy. For example, one organization may configure its Caregiver role to include `MEDICATION_MANAGE`, while another
may not.

Each permission in the catalogue has a defined scope type and, where applicable, explicit dependencies on other
permissions.

### 2. EmployeePosition is separate from authorization

`EmployeePosition` represents a professional job title only. It does not carry permissions.

A position may define an optional default Application Role to prefill onboarding forms, but changing an employee's
position does not change their application privileges.

### 3. Permissions are evaluated within a resource scope

Every permission is held at a scope: one or more house units, or the whole organization.

- A permission held in Unit A does not apply in Unit B.
- Authorization is deny-by-default. An operation is permitted only when the required permissions are effective in the
  scope of the resource being accessed.
- Some operations involve more than one scope. A beneficiary transfer requires authorization in both the source and the
  destination unit.

### 4. Two access sources: workforce records and AccessGrants

Effective access is derived from two sources, evaluated together:

- **Assignment / Placement** records where an employee works, in which operational role and for which period. An active
  Assignment or Placement may confer that operational role within its house unit. When a Placement ends, the access it
  conferred ends with it.
- **AccessGrant** explicitly grants an Application Role at a scope, independently of where the employee works. It is
  used for administrative responsibilities and for access that is not tied to a workforce placement.

Workforce facts and authorization facts remain distinct. A person who supervises a unit without working there receives
access through an AccessGrant, not through a fictitious Placement. Employees may exist without any Assignment or
Placement.

Independent AccessGrants are governed by the Organization Owner or an appropriately authorized administrative role.

### 5. Operational and administrative role categories

Every Application Role is either `OPERATIONAL` or `ADMINISTRATIVE`.

- Operational roles may be obtained through valid Assignments or Placements, or through explicit AccessGrants.
- Administrative roles are assigned only through explicit AccessGrants, never through workforce records.
- A small set of permissions is administrative-only and can never be part of an operational role: `ROLE_MANAGE`,
  `ACCESS_GRANT_MANAGE`, `EMPLOYEE_POSITION_MANAGE`, `HOUSE_UNIT_MANAGE` and `SECURITY_AUDIT_READ`.
- `ADMINISTRATIVE` does not mean unrestricted. An administrative role contains only the permissions configured for it.
- A person may hold operational and administrative roles through the same account.

### 6. Coordinator and Organization Owner

The Organization Owner and the Coordinator are distinct initial role templates. One person may hold both, but neither
role depends on the other, and the Coordinator is not modelled as a subordinate rank of the Owner.

**Organization Owner** controls organization-level access governance: Application Role definitions and independent
AccessGrants, according to protected platform rules.

**Coordinator** is an `OPERATIONAL` role responsible for staffing and day-to-day operations across the house units in
its authorized scope. By default:

- its scope covers all house units through an explicit AccessGrant;
- it has organization-wide `EMPLOYEE_READ` and `USER_ACCESS_MANAGE`, including employees without any Assignment or
  Placement;
- it may invite operational staff, manage Assignments and Placements, and arrange temporary Placements between the
  units it oversees;
- it may assign existing, approved operational roles through Assignments, Placements and staffing workflows.

The Coordinator cannot create independent AccessGrants, modify Application Role definitions or grant administrative
access through its operational authority.

### 7. Delegation and self-escalation prevention

Delegation rules are platform-enforced and deliberately simple. There is no configurable delegation engine.

- Authorized staff managers may assign operational access within their permitted scope.
- Administrative role assignment requires appropriate access-management authority.
- No user may obtain additional effective access through an unauthorized self-directed action. Legitimate Owner
  governance operations remain subject to the protected-Owner rules below.
- Users may create or change their own Assignments or Placements only when the operation does not increase their
  effective access. A Coordinator may record themselves as covering a shift while keeping their existing permissions.
  An access-increasing change must be performed by another suitably authorized person.
- Role-definition changes must not allow unauthorized self-escalation, including changes that indirectly expand the
  editor's effective permissions. An authorized Owner who also holds the Coordinator role may still maintain that role.
- Ordinary invitation privileges never imply authority to grant elevated access. Authorization validates the access an
  invitation or staffing change would result in, not merely the presence of the permission used to perform it.

### 8. Protected Organization Owner access

Effective Organization Owner status is the one specially protected case.

- Removing a person's effective Owner status requires authorization by an active Owner. This applies to direct
  revocation and to indirect removal, such as employment termination or account suspension.
- `EMPLOYEE_STATUS_MANAGE` or `ACCESS_GRANT_MANAGE` alone does not authorize removing Owner status.
- The last active Owner can never be removed, suspended, demoted or otherwise lose effective Owner authority.
- Owner access does not expire automatically.
- Changes to an Owner account's email or authentication credentials are subject to the same invariants and require
  appropriate verification.

Ordinary administrative roles do not receive this protection. An authorized user with organization-wide
`EMPLOYEE_STATUS_MANAGE` may terminate an employee holding ordinary administrative grants, subject to the normal
access-governance checks.

### 9. Centralized authorization and access governance

Authorization is evaluated centrally rather than through checks scattered across controllers and services. The leading
implementation proposal is an `Authz` component backed by a per-request `AccessSnapshot` of the caller's effective
permissions and scopes; the concrete design is not yet finalized.

Operations that change access — AccessGrant changes, account suspension, employment termination, role-definition
changes and access-affecting staffing changes — pass through a single access-governance write boundary. The boundary
validates the actor's authorization, the resulting access, the protected-Owner invariants and audit requirements.
Employment termination and the resulting access revocation are transactionally consistent, and the Owner invariants are
enforced transactionally and safely under concurrent modification. Entry points do not implement their own variants of
these safeguards.

### 10. Permission-aware API responses

The backend is the source of truth for authorization. Frontend visibility checks improve usability but never replace
backend enforcement.

API responses include sensitive fields and nested records only when the caller holds the corresponding effective READ
permission. For example, a beneficiary profile includes medication, allergy and legal-representative information only
with `MEDICATION_READ`, `ALLERGY_READ` and `LEGAL_REPRESENTATIVE_READ` respectively. The same applies to other nested
information, such as employee staffing data. One endpoint can therefore serve users with different permissions while
returning only what each is allowed to see. Responses distinguish an unauthorized section from an authorized but empty
one; the exact serialization is an implementation decision.

---

## Rules

In addition to the decisions above, the following invariants apply:

- Permission dependencies are defined explicitly per permission in the catalogue and enforced at runtime in the
  required effective scope. Permissions from different roles may satisfy a dependency when they apply to that scope. A
  dependency is a prerequisite, never an implicit grant.
- Ending employment suspends the user account and ends effective Assignments, Placements and AccessGrants, while
  preserving history. Reactivating employment restores none of them; application access is restored only through a
  separately authorized process on the existing account.
- After account suspension, existing tokens are no longer accepted for authorization.
- `ACTIVITY_AUDIT_READ` does not bypass the permissions protecting the underlying data. `SECURITY_AUDIT_READ` is a
  separate, administrative-only permission.

---

## Consequences

### Positive

- Organizations configure responsibilities to match their own procedures without code changes.
- Job titles and application privileges evolve independently.
- Access is limited to the units where a responsibility actually exists, which closes cross-unit privilege leakage.
- Supervision, temporary cover and administrative responsibility are modelled explicitly, without fictitious workforce
  records.
- Privilege-escalation paths through workforce changes, role editing or invitations are blocked by platform rules
  rather than by convention.
- The organization cannot lose its last Owner through an ordinary operational action.
- Sensitive data is protected at the API boundary, independently of the frontend.

### Negative

- Authorization requires evaluating roles, scopes and two access sources per request, which is more complex than
  checking a flat permission set.
- Response DTOs must be assembled according to the caller's permissions rather than returned unconditionally.
- Access-changing operations must go through the access-governance boundary, which constrains how new features that
  affect access are implemented.
- Existing controllers, services and frontend workflows built on the current model must be migrated.
- A safe Owner account-access recovery procedure is required before production deployment and is not defined by this
  ADR.

---

## Alternatives Considered

### Keep permissions on EmployeePosition

Rejected. It couples job titles to privileges, prevents independent configuration of application responsibilities, and turns HR edits into privilege changes.

### Hardcoded role hierarchy

Rejected. Fixed relationships such as "Coordinator ranks below Owner" or "Caregivers may only read medication" do not
reflect how different organizations operate, and would require code changes to accommodate them.

### Configurable delegation engine

Rejected. Simple, platform-enforced delegation rules cover the required scenarios with far less complexity and fewer
opportunities for misconfiguration.

### Safeguards implemented per endpoint

Rejected. Relying on each entry point to remember protected-access and self-escalation checks is error-prone. A single
access-governance boundary ensures the same rules apply regardless of how an access change is requested.

### Separate privileged accounts

Not required. A person may hold operational and administrative roles on one account; protection comes from scoped
permissions, role categories and the access-governance rules.

---

## Related

- [ADR-008](ADR-008-permission-aware-frontend-authorization.md): Permission-aware frontend authorization. It describes
  the currently implemented frontend behavior. Its principle that the backend is the source of truth is retained by
  this ADR.
- [ADR-009](ADR-009-employee-activity-audit-semantics.md): Employee activity audit semantics.
