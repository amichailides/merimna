package io.github.amichailides.merimna.houseunit;


import io.github.amichailides.merimna.beneficiary.BeneficiaryRepository;
import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitUpdateDTO;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitAlreadyExistsException;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitCapacityExceededException;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitFullException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

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

    public void validateForUpdate(HouseUnit existing, String normalizedCode,HouseUnitUpdateDTO dto) {
        validateCodeUniquenessForUpdate(existing, normalizedCode);
        validateMaxCapacity(existing, dto.maxCapacity());
    }

    private void validateCodeUniqueness(String normalizedCode) {
        if (houseUnitRepository.existsByCode(normalizedCode)) {
            throw new HouseUnitAlreadyExistsException(normalizedCode);
        }
    }

    private void validateCodeUniquenessForUpdate(HouseUnit existing, String newCode) {
        if (newCode != null &&
                houseUnitRepository.existsByCodeAndPublicIdNot(newCode, existing.getPublicId())) {

            throw new HouseUnitAlreadyExistsException(newCode);
        }
    }

    private void validateMaxCapacity(HouseUnit existing, Integer newMaxCapacity) {
        if (newMaxCapacity == null) return;

        long count = beneficiaryRepository.countByHouseUnitAndIsActiveTrue(existing);

        if (newMaxCapacity < count) {
            throw new HouseUnitCapacityExceededException(existing.getCode(), count);
        }
    }
}
