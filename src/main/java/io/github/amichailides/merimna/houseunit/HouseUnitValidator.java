package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

@Component
@RequiredArgsConstructor
public class HouseUnitValidator {
    private final HouseUnitRepository houseUnitRepository;

    public void validateForCreate(String code) {
        validateCodeUniqueness(code);
    }

    private void validateCodeUniqueness(String code) {
        if (houseUnitRepository.existsByCode(code)) {
            throw new HouseUnitAlreadyExistsException(code);
        }
    }
}
