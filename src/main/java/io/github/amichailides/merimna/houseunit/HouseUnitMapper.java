package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitReadOnlyDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitUpdateDTO;
import org.springframework.stereotype.Component;

import java.util.Objects;

@Component
public class HouseUnitMapper {

    public HouseUnitReadOnlyDTO toDTO(HouseUnit entity) {
        if (entity == null) return null;

        return HouseUnitReadOnlyDTO.builder()
                .code(entity.getCode())
                .displayName(entity.getDisplayName())
                .address(entity.getAddress())
                .build();
    }

    public HouseUnit toEntity(HouseUnitCreateDTO dto, String code) {
        if (dto == null) return null;

        return HouseUnit.builder()
                .code(code)
                .displayName(dto.displayName())
                .address(dto.address())
                .build();
    }

    public void updateEntity(HouseUnit existing, HouseUnitUpdateDTO dto) {
        Objects.requireNonNull(existing, "HouseUnit must not be null");
        Objects.requireNonNull(dto, "HouseUnitUpdateDTO must not be null");

        if (dto.code() != null) existing.setCode(dto.code());
        if (dto.displayName() != null) existing.setDisplayName(dto.displayName());
        if (dto.address() != null) existing.setAddress(dto.address());
    }
}
