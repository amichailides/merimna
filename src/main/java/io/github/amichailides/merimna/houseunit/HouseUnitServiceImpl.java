package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitReadOnlyDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitUpdateDTO;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitAlreadyExistsException;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundByCodeException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

@Service
@RequiredArgsConstructor
public class HouseUnitServiceImpl implements HouseUnitService{

    private final HouseUnitRepository houseUnitRepository;
    private final HouseUnitMapper houseUnitMapper;

    @Override
    @Transactional(readOnly = true)
    public List<HouseUnitReadOnlyDTO> findAll() {
        return houseUnitRepository.findAll()
                .stream()
                .map(houseUnitMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public HouseUnitReadOnlyDTO createHouseUnit(HouseUnitCreateDTO dto) {
        //TODO consider validator implementation

        if (houseUnitRepository.existsByCode(dto.code())) {
            throw new HouseUnitAlreadyExistsException(dto.code());
        }

        System.out.println("POST createHouseUnit hit");
        HouseUnit houseUnit = houseUnitMapper.toEntity(dto);
        HouseUnit saved = houseUnitRepository.save(houseUnit);
        return houseUnitMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public HouseUnitReadOnlyDTO updateHouseUnit(String houseUnitCode, HouseUnitUpdateDTO dto) {

        HouseUnit existing = houseUnitRepository.findByCode(houseUnitCode)
                .orElseThrow(() -> new HouseUnitNotFoundByCodeException(houseUnitCode));

        if (dto.code() != null && houseUnitRepository.existsByCodeAndIdNot(dto.code(), existing.getId())) {
            throw new HouseUnitAlreadyExistsException(dto.code());
        }

        houseUnitMapper.updateEntity(existing, dto);
        return houseUnitMapper.toDTO(existing);
    }

    @Transactional
    @Override
    public HouseUnitReadOnlyDTO getHouseUnitByCode(String code) {
        HouseUnit houseUnit = houseUnitRepository.findByCode(code)
                .orElseThrow(() -> new HouseUnitNotFoundByCodeException(code));

        return houseUnitMapper.toDTO(houseUnit);
    }


}
