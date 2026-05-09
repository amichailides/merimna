package io.github.amichailides.merimna.placement.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class EmployeePlacementInvalidEndDate extends BaseDomainException {

    public EmployeePlacementInvalidEndDate(LocalDate startDate, LocalDate endDate) {
        super(ErrorCode.EMPLOYEE_PLACEMENT_INVALID_END_DATE, Map.of(
                "startDate", startDate,
                "endDate", endDate
        ));
    }

    public EmployeePlacementInvalidEndDate(UUID placementPublicId,
                                           LocalDate startDate,
                                           LocalDate endDate) {
        super(ErrorCode.EMPLOYEE_PLACEMENT_INVALID_END_DATE, Map.of(
                "placementPublicId", placementPublicId,
                "startDate", startDate,
                "endDate", endDate
        ));
    }
}
