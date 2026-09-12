package io.github.amichailides.merimna.employee.onboarding;

import io.github.amichailides.merimna.assignment.EmployeeAssignmentService;
import io.github.amichailides.merimna.employee.EmployeeService;
import io.github.amichailides.merimna.employee.dto.EmployeeDetailsDTO;
import io.github.amichailides.merimna.employee.onboarding.dto.EmployeeOnboardingRequest;
import io.github.amichailides.merimna.employee.onboarding.dto.EmployeeOnboardingResponse;
import io.github.amichailides.merimna.security.invitation.UserInvitationService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
@RequiredArgsConstructor
public class EmployeeOnboardingServiceImpl
        implements EmployeeOnboardingService {

    private final EmployeeService employeeService;
    private final EmployeeAssignmentService employeeAssignmentService;
    private final UserInvitationService userInvitationService;

    @Override
    @Transactional
    public EmployeeOnboardingResponse onboard(
            EmployeeOnboardingRequest request
    ) {
        EmployeeDetailsDTO employee =
                employeeService.createEmployee(request.employee());

        UUID employeePublicId = employee.publicId();

        employeeAssignmentService.create(
                employeePublicId,
                request.initialAssignment()
        );

        if (request.systemAccess() != null) {
            userInvitationService.createForEmployee(
                    employeePublicId,
                    request.systemAccess().accountEmail()
            );
        }

        return new EmployeeOnboardingResponse(employeePublicId);
    }
}
