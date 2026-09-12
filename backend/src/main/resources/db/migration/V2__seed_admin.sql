-- =========================================================================
-- Seed: Admin Position + Permissions
-- =========================================================================

INSERT INTO employee_positions (id, code, display_name, requires_exclusive_placement)
VALUES (1, 'ADMIN', 'Administrator', false) ON CONFLICT (id) DO NOTHING;

INSERT INTO employee_position_permissions (position_id, permission)
VALUES (1, 'BENEFICIARY_READ'),
       (1, 'BENEFICIARY_CREATE'),
       (1, 'BENEFICIARY_UPDATE'),
       (1, 'BENEFICIARY_DISCHARGE'),
       (1, 'EMPLOYEE_READ'),
       (1, 'EMPLOYEE_CREATE'),
       (1, 'EMPLOYEE_UPDATE'),
       (1, 'EMPLOYEE_TERMINATE'),
       (1, 'EMPLOYEE_REACTIVATE'),
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
       (1, 'PLACEMENT_TERMINATE')
    ON CONFLICT DO NOTHING;

-- =========================================================================
-- Seed: Admin Employee
-- =========================================================================

INSERT INTO employees (id, public_id, position_id, first_name, last_name, contact_email, mobile_number,
                       street, street_number, city, zip_code, hire_date, is_active)
VALUES (1, '550e8400-e29b-41d4-a716-446655440000', 1, 'Νίκος', 'Παπαδόπουλος', 'nikos.papadopoulos@merimna.gr',
        '6944000001', 'Tsimiski', '43', 'Thessaloniki', '54623', CURRENT_DATE, true)
    ON CONFLICT (id) DO NOTHING;

-- =========================================================================
-- Seed: Admin User
-- =========================================================================

INSERT INTO users (id, public_id, username, email, password, role, active, employee_id)
VALUES (
           1,
           '11111111-1111-1111-1111-111111111111',
           'admin',
           'admin@merimna.local',
           '$argon2id$v=19$m=16384,t=2,p=1$7sGaCOZuHysZQGju3DJblg$iKDo2gle6oXpzOd4P/O+jR+i688dYqTj82o3pcI4kww',
           'ADMIN',
           true,
           1
       )
    ON CONFLICT (id) DO NOTHING;

SELECT setval('employees_id_seq', (SELECT MAX(id) FROM employees));
SELECT setval('users_id_seq', (SELECT MAX(id) FROM users));
SELECT setval('employee_positions_id_seq', (SELECT MAX(id) FROM employee_positions));