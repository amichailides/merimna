package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundByCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Set;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class HouseUnitResolver {

    private final HouseUnitRepository repository;

    public Set<HouseUnit> resolveForEmployeeUpdate(Set<String> houseUnitCodes) {
        if (houseUnitCodes == null) {
            return null;
        }

        return houseUnitCodes.stream()
                .map(code -> repository.findByCode(code)
                        .orElseThrow(() -> new HouseUnitNotFoundByCodeException(code)))
                .collect(Collectors.toSet());
    }
}
