package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitReadOnlyDTO;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseUnitServiceImpl implements HouseUnitService{

    private final HouseUnitRepository houseUnitRepository;
    private final HouseUnitMapper houseUnitMapper;

    @Transactional(readOnly = true)
    public List<HouseUnitReadOnlyDTO> findAll() {
        return houseUnitRepository.findAll()
                .stream()
                .map(houseUnitMapper::toDTO)
                .toList();
    }

    @Transactional
    public HouseUnitReadOnlyDTO createHouseUnit(HouseUnitCreateDTO dto) {
        //TODO consider validator implementation

        if (houseUnitRepository.existsByCode(dto.code())) {
            throw new HouseUnitAlreadyExistsException(dto.code());
        }

        HouseUnit houseUnit = houseUnitMapper.toEntity(dto);
        HouseUnit saved = houseUnitRepository.save(houseUnit);
        return houseUnitMapper.toDTO(saved);
    }


}
