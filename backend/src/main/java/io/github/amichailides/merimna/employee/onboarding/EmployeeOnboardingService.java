package io.github.amichailides.merimna.employee.onboarding;

import io.github.amichailides.merimna.employee.onboarding.dto.EmployeeOnboardingRequest;
import io.github.amichailides.merimna.employee.onboarding.dto.EmployeeOnboardingResponse;

public interface EmployeeOnboardingService {

    EmployeeOnboardingResponse onboard(
            EmployeeOnboardingRequest request
    );
}
