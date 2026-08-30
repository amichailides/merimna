package io.github.amichailides.merimna.employee.systemaccess;

import io.github.amichailides.merimna.employee.systemaccess.dto.EmployeeAccessDTO;

import java.util.UUID;

public interface EmployeeAccessService {

    EmployeeAccessDTO getAccessStatus(UUID employeePublicId);

    void resendInvitation(UUID employeePublicId);

    void cancelInvitation(UUID employeePublicId);

    void grantAccess(UUID employeePublicId, String accountEmail);

    // TODO: #39 add suspend/reactivate account actions.
}
