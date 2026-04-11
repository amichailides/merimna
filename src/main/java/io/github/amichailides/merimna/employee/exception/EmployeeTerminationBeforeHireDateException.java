package io.github.amichailides.merimna.employee.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;

public class EmployeeTerminationBeforeHireDateException extends BaseDomainException {
    public EmployeeTerminationBeforeHireDateException(LocalDate hireDate, LocalDate terminationDate) {
        super(ErrorCode.EMPLOYEE_TERMINATION_BEFORE_HIRE_DATE, terminationDate, hireDate);
    }
}
