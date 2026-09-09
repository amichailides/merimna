delete from medications;

alter table medications
    add column started_at date not null,
    add column ended_at date;

alter table medications
    add constraint chk_medications_date_range
        check (ended_at is null or ended_at >= started_at);

alter table medications
    drop constraint fk_medication_beneficiary;

alter table medications
    add constraint fk_medication_beneficiary
        foreign key (beneficiary_id) references beneficiaries (id);

create index idx_medications_beneficiary_id
    on medications (beneficiary_id);
