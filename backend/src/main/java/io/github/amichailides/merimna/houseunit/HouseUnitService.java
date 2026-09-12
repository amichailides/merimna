package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitReadOnlyDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitUpdateDTO;

import java.util.List;
import java.util.UUID;

public interface HouseUnitService {
    List<HouseUnitReadOnlyDTO> getAllHouseUnits();

    HouseUnitReadOnlyDTO createHouseUnit(HouseUnitCreateDTO dto);

    HouseUnitReadOnlyDTO updateHouseUnit(UUID publicId, HouseUnitUpdateDTO dto);

    HouseUnitReadOnlyDTO getHouseUnit(UUID publicId);
}
