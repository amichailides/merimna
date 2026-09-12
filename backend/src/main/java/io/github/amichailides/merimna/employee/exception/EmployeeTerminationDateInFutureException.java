package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class EmployeeTerminationDateInFutureException extends BaseDomainException {
    public EmployeeTerminationDateInFutureException(UUID employeePublicId,
                                                    LocalDate terminationDate) {
        super(ErrorCode.EMPLOYEE_TERMINATION_DATE_IN_FUTURE, Map.of(
                "employeePublicId", employeePublicId,
                "terminationDate", terminationDate
        ));
    }
}
