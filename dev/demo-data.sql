-- =====================================================================
-- Merimna demo data (curated)
-- =====================================================================
-- LOCAL / DEMO USE ONLY.
--
-- Run after:
--   docker compose up -d --build
--
-- Load with:
--   docker compose exec -T postgres \
--     psql -U merimna_user -d merimna_db < dev/demo-data.sql
--
-- This script intentionally:
--   - keeps Flyway schema/history under Flyway's control
--   - does not seed refresh tokens, password-reset tokens or invitations
--   - replaces application/demo data so it can be re-run locally
--   - uses explicit column names to stay readable and safer across changes
-- =====================================================================

BEGIN;

-- ---------------------------------------------------------------------
-- Reset demo/application data
-- ---------------------------------------------------------------------
-- CASCADE also clears dependent transient rows (for example refresh tokens,
-- password-reset tokens and invitations) without touching flyway_schema_history.
TRUNCATE TABLE
    audit_logs,
    beneficiary_legal_representatives,
    medications,
    allergies,
    employee_placements,
    employee_assignments,
    users,
    legal_representatives,
    beneficiaries,
    employees,
    employee_position_permissions,
    employee_positions,
    house_units
RESTART IDENTITY CASCADE;

-- ---------------------------------------------------------------------
-- House units
-- ---------------------------------------------------------------------

INSERT INTO house_units
    (id, public_id, code, display_name, address, max_capacity)
VALUES
    (1, '1e2fb330-ecd3-4575-bf90-96510bda7fbc', 'UNIT_A', 'Αναγέννηση 1', 'Πατησίων 120, Αθήνα', 5),
    (2, 'fcf24954-6de4-45ac-b63b-2be2caf83b74', 'UNIT_B', 'Αναγέννηση 2', 'Κηφισίας 210, Αθήνα', 4),
    (3, 'b1386dee-c7fb-462a-b6b7-ad8346569f10', 'UNIT_C', 'Αναγέννηση 3', 'Ακτή Μιαούλη 82, Πειραιάς', 6);

-- ---------------------------------------------------------------------
-- Employee positions
-- ---------------------------------------------------------------------

INSERT INTO employee_positions
    (id, code, display_name, requires_exclusive_placement)
VALUES
    (1, 'ADMIN', 'Administrator', false),
    (2, 'CAREGIVER', 'Caregiver', true),
    (3, 'HOUSE_MANAGER', 'House Manager', false),
    (4, 'PSYCHOLOGIST', 'Ψυχολόγος', false),
    (5, 'SOCIAL_WORKER', 'Κοινωνικός Λειτουργός', false),
    (6, 'PSYCHIATRIST', 'Ψυχίατρος', false);

-- ---------------------------------------------------------------------
-- Position permissions
-- ---------------------------------------------------------------------

-- ADMIN: full permission set
INSERT INTO employee_position_permissions (position_id, permission) VALUES
    (1, 'BENEFICIARY_READ'),
    (1, 'BENEFICIARY_CREATE'),
    (1, 'BENEFICIARY_UPDATE'),
    (1, 'BENEFICIARY_DISCHARGE'),
    (1, 'EMPLOYEE_READ'),
    (1, 'EMPLOYEE_CREATE'),
    (1, 'EMPLOYEE_UPDATE'),
    (1, 'EMPLOYEE_TERMINATE'),
    (1, 'EMPLOYEE_REACTIVATE'),
    (1, 'EMPLOYEE_ACTIVITY_READ'),
    (1, 'HOUSE_UNIT_READ'),
    (1, 'HOUSE_UNIT_CREATE'),
    (1, 'HOUSE_UNIT_UPDATE'),
    (1, 'ASSIGNMENT_READ'),
    (1, 'ASSIGNMENT_CREATE'),
    (1, 'ASSIGNMENT_TERMINATE'),
    (1, 'ASSIGNMENT_CANCEL'),
    (1, 'USER_READ'),
    (1, 'USER_CREATE'),
    (1, 'USER_UPDATE'),
    (1, 'USER_DEACTIVATE'),
    (1, 'USER_REACTIVATE'),
    (1, 'PLACEMENT_READ'),
    (1, 'PLACEMENT_CREATE'),
    (1, 'PLACEMENT_UPDATE'),
    (1, 'PLACEMENT_TERMINATE');

-- CAREGIVER
INSERT INTO employee_position_permissions (position_id, permission) VALUES
    (2, 'EMPLOYEE_READ'),
    (2, 'BENEFICIARY_UPDATE'),
    (2, 'HOUSE_UNIT_READ'),
    (2, 'BENEFICIARY_READ'),
    (2, 'ASSIGNMENT_READ');

-- HOUSE_MANAGER
INSERT INTO employee_position_permissions (position_id, permission) VALUES
    (3, 'EMPLOYEE_READ'),
    (3, 'USER_READ'),
    (3, 'ASSIGNMENT_CREATE'),
    (3, 'BENEFICIARY_UPDATE'),
    (3, 'BENEFICIARY_DISCHARGE'),
    (3, 'BENEFICIARY_CREATE'),
    (3, 'ASSIGNMENT_READ'),
    (3, 'ASSIGNMENT_CANCEL'),
    (3, 'HOUSE_UNIT_READ'),
    (3, 'HOUSE_UNIT_UPDATE'),
    (3, 'EMPLOYEE_UPDATE'),
    (3, 'BENEFICIARY_READ'),
    (3, 'ASSIGNMENT_TERMINATE'),
    (3, 'EMPLOYEE_ACTIVITY_READ');

-- PSYCHOLOGIST
INSERT INTO employee_position_permissions (position_id, permission) VALUES
    (4, 'BENEFICIARY_READ'),
    (4, 'PLACEMENT_READ'),
    (4, 'BENEFICIARY_UPDATE'),
    (4, 'HOUSE_UNIT_READ');

-- SOCIAL_WORKER
INSERT INTO employee_position_permissions (position_id, permission) VALUES
    (5, 'BENEFICIARY_READ'),
    (5, 'PLACEMENT_READ'),
    (5, 'BENEFICIARY_UPDATE'),
    (5, 'HOUSE_UNIT_READ');

-- PSYCHIATRIST
INSERT INTO employee_position_permissions (position_id, permission) VALUES
    (6, 'BENEFICIARY_READ'),
    (6, 'PLACEMENT_READ'),
    (6, 'BENEFICIARY_UPDATE'),
    (6, 'HOUSE_UNIT_READ');

-- ---------------------------------------------------------------------
-- Employees
-- ---------------------------------------------------------------------

INSERT INTO employees
    (
        id, public_id, position_id,
        first_name, last_name, contact_email, mobile_number,
        street, street_number, city, zip_code,
        hire_date, is_active,
        date_of_birth, emergency_contact_name, emergency_contact_phone_number
    )
VALUES
    (1,  '550e8400-e29b-41d4-a716-446655440000', 1, 'Γεώργιος', 'Παπαδόπουλος', 'nikos.papadopoulos@merimna.gr', '6944000001', 'Τσιμισκή', '43', 'Θεσσαλονίκη', '54623', '2026-01-10', true,  '1985-04-12', 'Ελένη Παπαδοπούλου', '+306944000002'),
    (2,  '0994d047-b753-4b8b-a517-905e009c827a', 2, 'Κώστας', 'Παπαδάκης', 'kostas.papadakis@example.com', '6934567890', 'Πατησίων', '45', 'Αθήνα', '10434', '2026-04-09', true,  '1990-01-18', 'Μαρία Παπαδάκη', '+306934567891'),
    (3,  '26bdc212-3469-4dc3-9dd6-0e1d830a2fbb', 3, 'Μαρία', 'Νικολάου', 'maria.nikolaou@example.com', '6923456789', 'Κηφισίας', '200', 'Αθήνα', '11525', '2026-04-09', true,  '1988-07-22', 'Αντώνης Νικολάου', '+306923456780'),
    (4,  'd1daaee1-938e-4054-b694-f638ac83d0fb', 2, 'Μαρία', 'Κωνσταντίνου', 'mkonstantinou@example.com', '6945678901', 'Εγνατίας', '120', 'Θεσσαλονίκη', '54622', '2023-11-15', true,  '1992-03-11', 'Πέτρος Κωνσταντίνου', '+306945678902'),
    (5,  '0d96552d-db24-41cf-8274-4ed26460d336', 4, 'Ελένη', 'Παπαδοπούλου', 'eleni.papadopoulou@example.com', '6945678911', 'Ιπποκράτους', '34', 'Αθήνα', '10680', '2026-05-02', true,  '1987-09-14', 'Αλέξανδρος Παπαδόπουλος', '+306945678912'),
    (6,  'a2de9b55-084d-41ba-812b-faec2c2613ea', 6, 'Ανδρέας', 'Κωνσταντίνου', 'andreas.konstantinou@example.com', '6982345678', 'Βασιλίσσης Σοφίας', '115', 'Αθήνα', '11521', '2026-03-20', true,  '1983-12-02', 'Νίκη Κωνσταντίνου', '+306982345679'),
    (7,  '64ca6aaf-2354-4e84-9ee2-b00d9d9574fc', 5, 'Σοφία', 'Αντωνίου', 'sofia.antoniou@example.com', '6973456789', 'Σόλωνος', '58', 'Αθήνα', '10672', '2026-04-18', true,  '1991-05-27', 'Γιάννης Αντωνίου', '+306973456780'),
    (8,  '121f0e00-e9d5-46c3-a11e-5c6b3b25c82d', 2, 'Γεωργία', 'Παπαδάκη', 'georgia.papadaki@example.com', '6941234567', 'Πατησίων', '85', 'Αθήνα', '10434', '2026-02-10', true,  '1994-08-03', 'Χρήστος Παπαδάκης', '+306941234568'),
    (9,  '21d18248-86bf-4f7a-8445-a7b997407715', 2, 'Νίκος', 'Μαυρίδης', 'nikos.mavridis@example.com', '6976543210', 'Λιοσίων', '128', 'Αθήνα', '10445', '2026-03-05', true,  '1993-11-19', 'Άννα Μαυρίδου', '+306976543211'),
    (10, '0bacaf9d-787d-48bd-903d-11bc31c90c78', 2, 'Κατερίνα', 'Σταθοπούλου', 'katerina.stathopoulou@example.com', '6987654321', 'Αχαρνών', '210', 'Αθήνα', '10446', '2026-04-22', true,  '1995-02-06', 'Μιχάλης Σταθόπουλος', '+306987654322'),
    (11, '310a14e9-e58c-4a95-bf01-49db4efaa50d', 2, 'Παναγιώτης', 'Δημητρίου', 'panagiotis.dimitriou@example.com', '6939876543', 'Σεπολίων', '33', 'Αθήνα', '10444', '2026-01-18', false, '1989-06-30', 'Αναστασία Δημητρίου', '+306939876544'),
    (12, '022ff47e-b079-44c0-b898-c6ce76f5c339', 2, 'Ιωάννα', 'Καραγιάννη', 'ioanna.karagianni@example.com', '6954332198', 'Κυψέλης', '87', 'Αθήνα', '11362', '2026-05-12', true,  '1996-10-21', 'Δημήτρης Καραγιάννης', '+306954332199'),
    (13, '44f4afd2-0006-45c0-afe6-194c2c82141e', 3, 'Δημήτρης', 'Αλεξίου', 'dimitris.alexiou@merimna.com', '6962345648', 'Μιχαλακοπούλου', '91', 'Αθήνα', '11528', '2026-06-01', true,  '1986-01-25', 'Ευαγγελία Αλεξίου', '+306962345649');

-- ---------------------------------------------------------------------
-- Beneficiaries
-- ---------------------------------------------------------------------

INSERT INTO beneficiaries
    (
        id, public_id, first_name, last_name, amka, date_of_birth,
        is_active, discharge_date, discharge_reason, discharged_by_employee_id,
        house_unit_id,
        perm_street, perm_street_number, perm_city, perm_zip_code,
        emergency_first_name, emergency_last_name, emergency_relationship,
        emergency_mobile_number, emergency_landline_phone, emergency_email,
        emergency_street, emergency_street_number, emergency_city, emergency_zip_code
    )
VALUES
    (
        1, '7a5bae37-8479-450b-a2e4-a8668be55530',
        'Παναγιώτης', 'Σταματίου', '17037754321', '1977-03-17',
        true, NULL, NULL, NULL,
        1,
        'Λιοσίων', '89', 'Αθήνα', '10445',
        'Δήμητρα', 'Σταματίου', 'FRIEND',
        '6953344556', '2103344556', 'dimitra.stamatiou@example.com',
        'Ιουλιανού', '14', 'Αθήνα', '10433'
    ),
    (
        2, 'f4c29c31-946b-4637-a4f7-f5752f0b5832',
        'Λάμπρος', 'Δημητρίου', '05098224680', '1982-09-05',
        true, NULL, NULL, NULL,
        2,
        'Κηφισίας', '120', 'Αθήνα', '11526',
        'Ειρήνη', 'Δημητρίου', 'SOCIAL_WORKER',
        '6936543210', '2106543210', 'eirini.dimitriou@example.com',
        'Πατησίων', '210', 'Αθήνα', '11252'
    ),
    (
        3, '3cc9c95b-e6b1-4ad8-93c0-43d0b34f27f1',
        'Νικόλαος', 'Αντωνίου', '12068512345', '1985-06-12',
        true, NULL, NULL, NULL,
        3,
        'Χαριλάου Τρικούπη', '77', 'Αθήνα', '10681',
        'Αλεξάνδρα', 'Αντωνίου', 'SIBLING',
        '6948123456', NULL, 'alexandra.antoniou@example.com',
        'Ακαδημίας', '55', 'Αθήνα', '10679'
    ),
    (
        4, '78e3131d-7292-4a3e-96f0-a8fc7b1fa401',
        'Μαρία', 'Θεοδώρου', '23047967890', '1979-04-23',
        false, '2026-08-30', 'Μεταφορά σε άλλη δομή υποστηριζόμενης διαβίωσης', 7,
        1,
        'Αχαρνών', '142', 'Αθήνα', '10446',
        'Αντώνης', 'Θεοδώρου', 'OTHER_RELATIVE',
        '6977001122', '2107001122', 'antonis.theodorou@example.com',
        '3ης Σεπτεμβρίου', '90', 'Αθήνα', '10434'
    );

-- ---------------------------------------------------------------------
-- Allergies
-- ---------------------------------------------------------------------

INSERT INTO allergies
    (id, public_id, beneficiary_id, substance, severity, reaction)
VALUES
    (1, 'a1111111-1111-4111-8111-111111111111', 1, 'Πενικιλίνη', 'HIGH', 'Έντονο εξάνθημα και δυσκολία στην αναπνοή'),
    (2, 'a2222222-2222-4222-8222-222222222222', 2, 'Φιστίκια', 'HIGH', 'Οίδημα και κνίδωση'),
    (3, 'a3333333-3333-4333-8333-333333333333', 2, 'Γύρη', 'LOW', 'Ρινίτιδα και ερεθισμός στα μάτια');

-- ---------------------------------------------------------------------
-- Medications
-- ---------------------------------------------------------------------
-- ended_at IS NULL => active
-- ended_at NOT NULL => discontinued / historical

INSERT INTO medications
    (
        id, public_id, beneficiary_id,
        administration_times, dosage, frequency, instructions, name,
        started_at, ended_at
    )
VALUES
    (
        1, 'b1111111-1111-4111-8111-111111111111', 1,
        '08:00,20:00', '1 mg', 'Twice daily',
        'Να λαμβάνεται μετά το φαγητό', 'Risperidone',
        '2026-03-10', NULL
    ),
    (
        2, 'b2222222-2222-4222-8222-222222222222', 1,
        '22:00', '5 mg', 'Once daily',
        'Βραδινή λήψη', 'Diazepam',
        '2026-01-15', '2026-05-20'
    ),
    (
        3, 'b3333333-3333-4333-8333-333333333333', 2,
        '09:00', '50 mg', 'Once daily',
        'Πρωινή λήψη', 'Sertraline',
        '2026-06-01', NULL
    ),
    (
        4, 'b4444444-4444-4444-8444-444444444444', 3,
        '08:00,20:00', '25 mg', 'Twice daily',
        NULL, 'Quetiapine',
        '2026-07-05', NULL
    ),
    (
        5, 'b5555555-5555-4555-8555-555555555555', 4,
        '21:00', '10 mg', 'Once daily',
        'Ιστορική αγωγή πριν την αποχώρηση', 'Escitalopram',
        '2026-02-12', '2026-08-30'
    );

-- ---------------------------------------------------------------------
-- Legal representatives
-- ---------------------------------------------------------------------

INSERT INTO legal_representatives
    (
        id, first_name, last_name, afm, type,
        mobile_number, landline_phone, email, notes
    )
VALUES
    (
        1, 'Δήμητρα', 'Σταματίου', '123456783', 'LEGAL_GUARDIAN',
        '6953344556', '2103344556', 'dimitra.stamatiou.rep@example.com',
        'Κύρια νόμιμη εκπρόσωπος του Παναγιώτη Σταματίου.'
    ),
    (
        2, 'Αλέξανδρος', 'Παπαϊωάννου', '111111114', 'JUDICIAL_SUPPORTER',
        '6944556677', NULL, 'alexandros.papai@example.com',
        'Δικαστικός συμπαραστάτης.'
    ),
    (
        3, 'Κοινωνική Υπηρεσία', 'Δήμου Αθηναίων', '987654324', 'PUBLIC_AUTHORITY',
        NULL, '2105550100', 'social.service@example.com',
        'Δημόσια αρχή για διοικητική εκπροσώπηση όπου απαιτείται.'
    );

INSERT INTO beneficiary_legal_representatives
    (beneficiary_id, legal_representative_id)
VALUES
    (1, 1),
    (2, 2),
    (4, 3);

-- ---------------------------------------------------------------------
-- Employee assignments
-- ---------------------------------------------------------------------

INSERT INTO employee_assignments
    (id, public_id, employee_id, house_unit_id, status, start_date, end_date)
VALUES
    (1, 'c7738ed9-1227-43aa-b3b8-d4aeb8e9c5ee', 2,  1, 'ACTIVE',     '2026-05-19', NULL),
    (2, '2fe8c071-be49-47cb-b930-12f963127605', 3,  2, 'ACTIVE',     '2026-05-19', NULL),
    (3, 'ed7d4779-3603-4efc-9dda-521b1570ce7c', 4,  2, 'ACTIVE',     '2026-06-04', NULL),
    (4, 'bc2af45c-0255-4c6f-a21b-e6f97bca004a', 13, 3, 'COMPLETED',  '2026-06-20', '2026-07-20'),
    (5, '3140b511-500f-4498-971e-155cb50c0b42', 9,  2, 'ACTIVE',     '2026-06-20', NULL),
    (6, 'fcf4ba69-a486-4bb9-bc19-40081b3905c6', 8,  3, 'COMPLETED',  '2026-06-21', '2026-07-21'),
    (7, '0ca41c86-822b-4ce5-9abf-da7a40c786d9', 12, 2, 'ACTIVE',     '2026-06-21', NULL),
    (8, 'de9c3e37-674c-42a7-91af-040ca62f3f37', 10, 1, 'TERMINATED', '2026-02-01', '2026-04-15');

-- ---------------------------------------------------------------------
-- Temporary placements
-- ---------------------------------------------------------------------
-- Active placement: end_date IS NULL.
-- Each active placement is intentionally in a different house unit from the
-- employee's current official assignment.

INSERT INTO employee_placements
    (id, public_id, employee_id, house_unit_id, start_date, end_date, reason)
VALUES
    (1, '838fb167-8f89-4c9e-8741-8e310f34b9ea', 2,  2, '2026-08-01', NULL,         'TEMPORARY_COVERAGE'),
    (2, 'b60d9752-c5e0-4323-b237-5428e5709ea8', 4,  1, '2026-08-15', NULL,         'TEMPORARY_COVERAGE'),
    (3, '7ab22ff5-61b7-4b0b-9e31-18af8b810c49', 13, 2, '2026-06-20', '2026-07-23', 'TEMPORARY_COVERAGE'),
    (4, '1cbd18f6-ee56-45bd-99b0-3d20b01b4023', 12, 3, '2026-09-01', NULL,         'TEMPORARY_COVERAGE');

-- ---------------------------------------------------------------------
-- Users
-- ---------------------------------------------------------------------
-- Existing hashes are preserved from the original dev dataset so the project's
-- documented local credentials continue to work as before.

INSERT INTO users
    (id, public_id, employee_id, username, password, email, role, active)
VALUES
    (
        1, '11111111-1111-1111-1111-111111111111', 1, 'admin',
        '$argon2id$v=19$m=16384,t=2,p=1$I7VPM7b3D2YWdSLtjdI5Qg$Plir5tK0GQn3GhstAvAC7UH5xHMDvukViIRqXUVB3HA',
        'admin@merimna.local', 'ADMIN', true
    ),
    (
        2, '989bcac6-1cc3-4545-b78d-6feb355719bc', 4, 'maria.konstantinou',
        '$argon2id$v=19$m=16384,t=2,p=1$9oTWkhdqKKUTX7BpUx+vgA$73ora28GDQhXwh/SUACV+8ZiWVsMyzmnWu8Ed1mPeiQ',
        'mkonstantinou@example.com', 'STAFF', true
    ),
    (
        3, '893e6b12-e08a-4c8b-a986-7828f7528dfe', 9, 'nikos.mavridis',
        '$argon2id$v=19$m=16384,t=2,p=1$b1UJr7olOj4WOLqWJpP0ZA$pHu5CxDgIJKD/1ZiYDMSWRgmEbCG+2KEM6lhM8tc8ts',
        'nikos.mavridis@example.com', 'STAFF', true
    ),
    (
        4, '53cd9374-953a-4c1b-9ded-86ca50a1f3d0', 3, 'maria.nikolaou',
        '$argon2id$v=19$m=16384,t=2,p=1$xzxOUluhqO0UuCGszAH/qw$NO7KoPprmhKlNECcTyd+swG1edXWRc1jaJBT2uA6ork',
        'maria.nikolaou@example.com', 'STAFF', true
    );

-- ---------------------------------------------------------------------
-- Curated audit history
-- ---------------------------------------------------------------------
-- Only action names already present in the original dev history are used here.
-- This avoids the raw refresh-token reuse spam while keeping representative
-- activity for the employee/activity/dashboard views.

INSERT INTO audit_logs
    (
        id, public_id, action, entity_type, entity_public_id,
        user_public_id, employee_public_id,
        ip_address, user_agent, occurred_at, outcome, metadata,
        subject_employee_public_id
    )
VALUES
    (
        1, 'd0000001-0000-4000-8000-000000000001',
        'AUTH_LOGIN_SUCCESS', 'AUTH',
        '11111111-1111-1111-1111-111111111111',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-08 08:55:00+03', 'SUCCESS', '{}'::jsonb, NULL
    ),
    (
        2, 'd0000002-0000-4000-8000-000000000002',
        'EMPLOYEE_CREATED', 'EMPLOYEE',
        '0994d047-b753-4b8b-a517-905e009c827a',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-08 09:05:00+03', 'SUCCESS', '{}'::jsonb,
        '0994d047-b753-4b8b-a517-905e009c827a'
    ),
    (
        3, 'd0000003-0000-4000-8000-000000000003',
        'EMPLOYEE_CREATED', 'EMPLOYEE',
        '26bdc212-3469-4dc3-9dd6-0e1d830a2fbb',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-08 09:12:00+03', 'SUCCESS', '{}'::jsonb,
        '26bdc212-3469-4dc3-9dd6-0e1d830a2fbb'
    ),
    (
        4, 'd0000004-0000-4000-8000-000000000004',
        'ASSIGNMENT_CREATED', 'EMPLOYEE_ASSIGNMENT',
        'c7738ed9-1227-43aa-b3b8-d4aeb8e9c5ee',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-08 09:20:00+03', 'SUCCESS',
        '{"employeePublicId":"0994d047-b753-4b8b-a517-905e009c827a","houseUnitPublicId":"1e2fb330-ecd3-4575-bf90-96510bda7fbc","startDate":"2026-05-19","endDate":null}'::jsonb,
        '0994d047-b753-4b8b-a517-905e009c827a'
    ),
    (
        5, 'd0000005-0000-4000-8000-000000000005',
        'BENEFICIARY_CREATED', 'BENEFICIARY',
        '7a5bae37-8479-450b-a2e4-a8668be55530',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-08 10:00:00+03', 'SUCCESS', '{}'::jsonb, NULL
    ),
    (
        6, 'd0000006-0000-4000-8000-000000000006',
        'BENEFICIARY_CREATED', 'BENEFICIARY',
        'f4c29c31-946b-4637-a4f7-f5752f0b5832',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-08 10:15:00+03', 'SUCCESS', '{}'::jsonb, NULL
    ),
    (
        7, 'd0000007-0000-4000-8000-000000000007',
        'USER_CREATED', 'USER',
        '989bcac6-1cc3-4545-b78d-6feb355719bc',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-09 09:00:00+03', 'SUCCESS', '{}'::jsonb,
        'd1daaee1-938e-4054-b694-f638ac83d0fb'
    ),
    (
        8, 'd0000008-0000-4000-8000-000000000008',
        'EMPLOYEE_UPDATED', 'EMPLOYEE',
        'd1daaee1-938e-4054-b694-f638ac83d0fb',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-09 09:20:00+03', 'SUCCESS',
        '{"changes":[{"fieldName":"mobileNumber","oldValue":"6945678900","newValue":"6945678901"}]}'::jsonb,
        'd1daaee1-938e-4054-b694-f638ac83d0fb'
    ),
    (
        9, 'd0000009-0000-4000-8000-000000000009',
        'PLACEMENT_CREATED', 'EMPLOYEE_PLACEMENT',
        '838fb167-8f89-4c9e-8741-8e310f34b9ea',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-09 10:00:00+03', 'SUCCESS',
        '{"employeePublicId":"0994d047-b753-4b8b-a517-905e009c827a","houseUnitPublicId":"fcf24954-6de4-45ac-b63b-2be2caf83b74","startDate":"2026-08-01","endDate":null}'::jsonb,
        '0994d047-b753-4b8b-a517-905e009c827a'
    ),
    (
        10, 'd0000010-0000-4000-8000-000000000010',
        'AUTH_LOGIN_SUCCESS', 'AUTH',
        '989bcac6-1cc3-4545-b78d-6feb355719bc',
        '989bcac6-1cc3-4545-b78d-6feb355719bc',
        'd1daaee1-938e-4054-b694-f638ac83d0fb',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-09 11:00:00+03', 'SUCCESS', '{}'::jsonb, NULL
    ),
    (
        11, 'd0000011-0000-4000-8000-000000000011',
        'AUTH_PASSWORD_CHANGED', 'AUTH',
        '989bcac6-1cc3-4545-b78d-6feb355719bc',
        '989bcac6-1cc3-4545-b78d-6feb355719bc',
        'd1daaee1-938e-4054-b694-f638ac83d0fb',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-09 11:10:00+03', 'SUCCESS', '{}'::jsonb, NULL
    ),
    (
        12, 'd0000012-0000-4000-8000-000000000012',
        'AUTH_LOGOUT', 'AUTH',
        '989bcac6-1cc3-4545-b78d-6feb355719bc',
        '989bcac6-1cc3-4545-b78d-6feb355719bc',
        'd1daaee1-938e-4054-b694-f638ac83d0fb',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-09 11:20:00+03', 'SUCCESS', '{}'::jsonb, NULL
    ),
    (
        13, 'd0000013-0000-4000-8000-000000000013',
        'EMPLOYEE_CREATED', 'EMPLOYEE',
        '44f4afd2-0006-45c0-afe6-194c2c82141e',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-10 08:40:00+03', 'SUCCESS', '{}'::jsonb,
        '44f4afd2-0006-45c0-afe6-194c2c82141e'
    ),
    (
        14, 'd0000014-0000-4000-8000-000000000014',
        'ASSIGNMENT_CREATED', 'EMPLOYEE_ASSIGNMENT',
        'bc2af45c-0255-4c6f-a21b-e6f97bca004a',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-10 08:50:00+03', 'SUCCESS',
        '{"employeePublicId":"44f4afd2-0006-45c0-afe6-194c2c82141e","houseUnitPublicId":"b1386dee-c7fb-462a-b6b7-ad8346569f10","startDate":"2026-06-20","endDate":"2026-07-20"}'::jsonb,
        '44f4afd2-0006-45c0-afe6-194c2c82141e'
    ),
    (
        15, 'd0000015-0000-4000-8000-000000000015',
        'PLACEMENT_CREATED', 'EMPLOYEE_PLACEMENT',
        '7ab22ff5-61b7-4b0b-9e31-18af8b810c49',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-10 09:00:00+03', 'SUCCESS',
        '{"employeePublicId":"44f4afd2-0006-45c0-afe6-194c2c82141e","houseUnitPublicId":"fcf24954-6de4-45ac-b63b-2be2caf83b74","startDate":"2026-06-20","endDate":"2026-07-23"}'::jsonb,
        '44f4afd2-0006-45c0-afe6-194c2c82141e'
    ),
    (
        16, 'd0000016-0000-4000-8000-000000000016',
        'EMPLOYEE_TERMINATED', 'EMPLOYEE',
        '310a14e9-e58c-4a95-bf01-49db4efaa50d',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-10 09:30:00+03', 'SUCCESS',
        '{"activeAfter":false,"terminationDate":"2026-06-17"}'::jsonb,
        '310a14e9-e58c-4a95-bf01-49db4efaa50d'
    ),
    (
        17, 'd0000017-0000-4000-8000-000000000017',
        'BENEFICIARY_CREATED', 'BENEFICIARY',
        '3cc9c95b-e6b1-4ad8-93c0-43d0b34f27f1',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-11 08:30:00+03', 'SUCCESS', '{}'::jsonb, NULL
    ),
    (
        18, 'd0000018-0000-4000-8000-000000000018',
        'BENEFICIARY_CREATED', 'BENEFICIARY',
        '78e3131d-7292-4a3e-96f0-a8fc7b1fa401',
        '11111111-1111-1111-1111-111111111111',
        '550e8400-e29b-41d4-a716-446655440000',
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-11 08:40:00+03', 'SUCCESS', '{}'::jsonb, NULL
    ),
    (
        19, 'd0000019-0000-4000-8000-000000000019',
        'AUTH_PASSWORD_RESET', 'AUTH',
        '11111111-1111-1111-1111-111111111111',
        NULL, NULL,
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-11 10:00:00+03', 'SUCCESS', '{}'::jsonb, NULL
    ),
    (
        20, 'd0000020-0000-4000-8000-000000000020',
        'AUTH_REFRESH_TOKEN_REUSE_DETECTED', 'AUTH',
        '11111111-1111-1111-1111-111111111111',
        NULL, NULL,
        '127.0.0.1', 'Merimna Demo Seed',
        '2026-09-11 10:15:00+03', 'SUCCESS',
        '{"refreshTokenPublicId":"aaaaaaaa-bbbb-4ccc-8ddd-eeeeeeeeeeee","replacedByTokenPublicId":"ffffffff-1111-4222-8333-444444444444"}'::jsonb,
        NULL
    );

-- ---------------------------------------------------------------------
-- Advance identity sequences after explicit IDs
-- ---------------------------------------------------------------------

SELECT pg_catalog.setval('house_units_id_seq', 3, true);
SELECT pg_catalog.setval('employee_positions_id_seq', 6, true);
SELECT pg_catalog.setval('employees_id_seq', 13, true);
SELECT pg_catalog.setval('beneficiaries_id_seq', 4, true);
SELECT pg_catalog.setval('allergies_id_seq', 3, true);
SELECT pg_catalog.setval('medications_id_seq', 5, true);
SELECT pg_catalog.setval('legal_representatives_id_seq', 3, true);
SELECT pg_catalog.setval('employee_assignments_id_seq', 8, true);
SELECT pg_catalog.setval('employee_placements_id_seq', 4, true);
SELECT pg_catalog.setval('users_id_seq', 4, true);
SELECT pg_catalog.setval('audit_logs_id_seq', 20, true);

COMMIT;
