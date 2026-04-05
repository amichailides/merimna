package io.github.amichailides.merimna.houseunit.dto;

import lombok.Builder;

@Builder
public record HouseUnitReadOnlyDTO(
        String code,
        String displayName,
        String address,
        Integer maxCapacity
) {}
