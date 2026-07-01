ALTER TABLE employees
    ADD COLUMN date_of_birth date,
    ADD COLUMN emergency_contact_name varchar(120),
    ADD COLUMN emergency_contact_phone_number varchar(40);

UPDATE employees
SET
    date_of_birth = DATE '1990-01-01',
    emergency_contact_name = 'Mock Emergency Contact',
    emergency_contact_phone_number = '+306900000000'
WHERE date_of_birth IS NULL
   OR emergency_contact_name IS NULL
   OR emergency_contact_phone_number IS NULL;

ALTER TABLE employees
    ALTER COLUMN date_of_birth SET NOT NULL,
    ALTER COLUMN emergency_contact_name SET NOT NULL,
    ALTER COLUMN emergency_contact_phone_number SET NOT NULL;