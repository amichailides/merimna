alter table user_invitations
    add column account_email varchar(255);

update user_invitations ui
set account_email = e.contact_email
from employees e
where ui.employee_id = e.id;

alter table user_invitations
    alter column account_email set not null;