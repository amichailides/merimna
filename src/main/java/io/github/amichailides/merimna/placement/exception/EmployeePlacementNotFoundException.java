package io.github.amichailides.merimna.placement.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseApplicationException;

import java.util.Map;
import java.util.UUID;

public class EmployeePlacementNotFoundException extends BaseApplicationException {
    public EmployeePlacementNotFoundException(UUID placementPublicId) {
        super(ErrorCode.EMPLOYEE_PLACEMENT_NOT_FOUND, Map.of(
                "placementPublicId", placementPublicId
        ));
    }
}
