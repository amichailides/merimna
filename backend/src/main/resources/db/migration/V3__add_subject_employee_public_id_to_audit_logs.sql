alter table audit_logs
    add column subject_employee_public_id uuid;

create index idx_audit_logs_subject_employee
    on audit_logs (subject_employee_public_id);