package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitReadOnlyDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitUpdateDTO;

import java.util.List;

public interface HouseUnitService {
    List<HouseUnitReadOnlyDTO> findAll();

    HouseUnitReadOnlyDTO createHouseUnit(HouseUnitCreateDTO dto);

    HouseUnitReadOnlyDTO updateHouseUnit(String houseUnitCode, HouseUnitUpdateDTO dto);

    HouseUnitReadOnlyDTO getHouseUnitByCode(String code);
}
