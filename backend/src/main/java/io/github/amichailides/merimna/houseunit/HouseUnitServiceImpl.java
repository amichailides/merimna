package io.github.amichailides.merimna.houseunit;

import io.github.amichailides.merimna.domain.HouseUnit;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitCreateDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitReadOnlyDTO;
import io.github.amichailides.merimna.houseunit.dto.HouseUnitUpdateDTO;
import io.github.amichailides.merimna.houseunit.exception.HouseUnitNotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class HouseUnitServiceImpl implements HouseUnitService{

    private final HouseUnitRepository houseUnitRepository;
    private final HouseUnitMapper houseUnitMapper;
    private final HouseUnitValidator houseUnitValidator;

    @Override
    @Transactional(readOnly = true)
    public List<HouseUnitReadOnlyDTO> getAllHouseUnits() {
        return houseUnitRepository.findAll()
                .stream()
                .map(houseUnitMapper::toDTO)
                .toList();
    }

    @Override
    @Transactional
    public HouseUnitReadOnlyDTO createHouseUnit(HouseUnitCreateDTO dto) {
        String code = normalizeCode(dto.code());

        houseUnitValidator.validateForCreate(code);

        HouseUnit houseUnit = houseUnitMapper.toEntity(dto, code);
        HouseUnit saved = houseUnitRepository.save(houseUnit);
        return houseUnitMapper.toDTO(saved);
    }

    @Override
    @Transactional
    public HouseUnitReadOnlyDTO updateHouseUnit(UUID publicId, HouseUnitUpdateDTO dto) {

        HouseUnit existing = houseUnitRepository.findByPublicId(publicId)
                .orElseThrow(() -> new HouseUnitNotFoundException(publicId));

        String normalizedCode = dto.code() != null ? normalizeCode(dto.code()) : null;

        houseUnitValidator.validateForUpdate(existing,normalizedCode, dto);

        houseUnitMapper.updateEntity(existing, dto, normalizedCode);
        return houseUnitMapper.toDTO(existing);
    }

    @Transactional(readOnly = true)
    @Override
    public HouseUnitReadOnlyDTO getHouseUnit(UUID publicId) {
        return houseUnitRepository.findByPublicId(publicId)
                .map(houseUnitMapper::toDTO)
                .orElseThrow(() -> new HouseUnitNotFoundException(publicId));
    }

    private String normalizeCode(String code) {
        return code.trim().toUpperCase();
    }
}
