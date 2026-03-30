package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitReadOnlyDTO;
import org.springframework.stereotype.Component;

@Component
public class HouseUnitMapper {

    public HouseUnitReadOnlyDTO toDTO(HouseUnit entity) {
        if (entity == null) return null;

        return HouseUnitReadOnlyDTO.builder()
                .id(entity.getId())
                .code(entity.getCode())
                .displayName(entity.getDisplayName())
                .address(entity.getAddress())
                .build();
    }

    public HouseUnit toEntity(HouseUnitCreateDTO dto) {
        if (dto == null) return null;

        return HouseUnit.builder()
                .code(dto.code())
                .displayName(dto.displayName())
                .address(dto.address())
                .build();
    }
}
