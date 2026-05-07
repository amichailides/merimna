package io.github.amichailides.merimna.houseunit;


import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.exception.ConflictValidationException;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitUpdateDTO;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitCapacityExceededException;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitFullException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.LinkedHashMap;
import java.util.Map;

@Component
@RequiredArgsConstructor
public class HouseUnitValidator {
    private final HouseUnitRepository houseUnitRepository;
    private final BeneficiaryRepository beneficiaryRepository;

    public void validateForCreate(String code) {
        validateCodeUniqueness(code);
    }

    public void validateAssignmentForBeneficiary(HouseUnit houseUnit) {
        long count = beneficiaryRepository.countByHouseUnitAndIsActiveTrue(houseUnit);

        if (houseUnit.isFull(count)) {
            throw new HouseUnitFullException(houseUnit.getCode(), count);
        }
    }

    public void validateForUpdate(HouseUnit existing, String normalizedCode, HouseUnitUpdateDTO dto) {
        validateCodeUniquenessForUpdate(existing, normalizedCode);
        validateMaxCapacity(existing, dto.maxCapacity());
    }

    private void validateCodeUniqueness(String normalizedCode) {
        if (houseUnitRepository.existsByCode(normalizedCode)) {
            throw codeConflict();
        }
    }

    private void validateCodeUniquenessForUpdate(HouseUnit existing, String newCode) {
        if (newCode != null &&
                houseUnitRepository.existsByCodeAndPublicIdNot(newCode, existing.getPublicId())) {

            throw codeConflict();
        }
    }

    private ConflictValidationException codeConflict() {
        Map<String, String> conflicts = new LinkedHashMap<>();
        conflicts.put("code", ErrorCode.HOUSE_UNIT_ALREADY_EXISTS.getMessageKey());
        return new ConflictValidationException(conflicts);
    }

    private void validateMaxCapacity(HouseUnit existing, Integer newMaxCapacity) {
        if (newMaxCapacity == null) return;

        long count = beneficiaryRepository.countByHouseUnitAndIsActiveTrue(existing);

        if (newMaxCapacity < count) {
            throw new HouseUnitCapacityExceededException(existing.getCode(), count);
        }
    }
}
