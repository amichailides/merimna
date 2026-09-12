package io.github.amichailides.merimna.houseunit.dto;

import lombok.Builder;

import java.util.UUID;

@Builder
public record HouseUnitReadOnlyDTO(
        UUID publicId,
        String code,
        String displayName,
        String address,
        Integer maxCapacity
) {}
