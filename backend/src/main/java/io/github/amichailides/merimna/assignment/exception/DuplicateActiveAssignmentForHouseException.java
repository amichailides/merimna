package io.github.amichailides.merimna.assignment.exception;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.exception.BaseDomainException;

import java.util.Map;
import java.util.UUID;

public class DuplicateActiveAssignmentForHouseException extends BaseDomainException {
    public DuplicateActiveAssignmentForHouseException(UUID employeePublicId, UUID houseUnitPublicId) {
        super(ErrorCode.ASSIGNMENT_DUPLICATE_ACTIVE_FOR_HOUSE, Map.of(
                "employeePublicId", employeePublicId,
                "houseUnitPublicId", houseUnitPublicId
        ));
    }
}
