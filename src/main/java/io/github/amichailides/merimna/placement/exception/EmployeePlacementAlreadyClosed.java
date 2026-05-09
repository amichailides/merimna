package io.github.amichailides.merimna.placement.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.time.LocalDate;
import java.util.Map;
import java.util.UUID;

public class EmployeePlacementAlreadyClosed extends BaseDomainException {
    public EmployeePlacementAlreadyClosed(UUID placementPublicId, LocalDate currentEndDate) {
        super(ErrorCode.EMPLOYEE_PLACEMENT_ALREADY_CLOSED, Map.of(
                "placementPublicId", placementPublicId,
                "currentEndDate", currentEndDate
        ));
    }
}
