package io.github.amichailides.merimna.houseunit.dto;

import lombok.Builder;

@Builder
public record HouseUnitReadOnlyDTO(
        Long id,
        String code,
        String displayName,
        String address
) {}
