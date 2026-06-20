# ADR-009: Employee Activity Audit Semantics

## Status

Accepted

## Context

Merimna records audit logs for domain events such as employee updates, employee assignments, employee placements,
beneficiary changes, medication updates, authentication events, and user management events.

The audit log stores `employee_public_id`, which represents the employee associated with the authenticated actor who
performed the action.

However, some audit events are also about an employee as the affected domain subject.

For example, when an admin assigns an employee to a house unit:

* the actor employee is the person performing the assignment;
* the subject employee is the employee being assigned;
* the changed entity is the assignment itself.

Using only `employee_public_id` would make employee-related audit activity ambiguous, because the same field could be
interpreted either as the actor employee or as the employee the event is about.

## Decision

Audit logs distinguish between actor employee and subject employee:

* `employee_public_id` represents the actor employee who performed the action.
* `subject_employee_public_id` represents the employee the event is about.
* `entity_public_id` represents the domain entity that changed.
* `metadata` stores event-specific structured context.

`subject_employee_public_id` is nullable because many audit events are not about an employee.

Employee, assignment, and placement audit events populate `subject_employee_public_id` when the audit event is about an
employee.

Medication, allergy, beneficiary, authentication, and user events do not populate `subject_employee_public_id` unless
the event is explicitly about an employee as the domain subject.

The initial employee activity endpoint represents activity about an employee and is queried by
`subject_employee_public_id`.

In this context, `EmployeeActivityDTO` means activity about the employee shown in Employee Details. It does not mean
activity performed by that employee.

If Merimna later needs to show actions performed by an employee, that will be treated as a separate concept, queried by
`employee_public_id`, and named separately, for example `EmployeePerformedActivityDTO` or `EmployeeActorActivityDTO`.

## Consequences

Employee Details Recent Activity can be queried efficiently with:

```sql
where subject_employee_public_id = :employeePublicId
order by occurred_at desc
limit 5
```

The exact number of returned rows may be controlled by endpoint pagination or request size, but the query semantics
remain based on `subject_employee_public_id`.

This avoids querying JSON metadata for employee-related activity.

The actor employee remains available in audit logs through `employee_public_id`.

The first employee activity DTO does not need to expose actor information unless the UI needs to display “performed by”
details.

If actor display becomes necessary, the API should expose display-ready actor information, such as an actor full name,
instead of expecting the frontend to display raw UUIDs.

Future “activity performed by this employee” should not be mixed with employee subject activity. It should use a
separate concept, naming, and query path, such as `EmployeePerformedActivityDTO`, `EmployeeActorActivityDTO`, or a
broader audit-log query.

## Related

* Migration: `V3__add_subject_employee_public_id_to_audit_logs.sql`
