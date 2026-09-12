package io.github.amichailides.merimna.employeePosition;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.domain.EmployeePosition;
import io.github.amichailides.merimna.domain.EmployeePositionCode;
import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionCreateDTO;
import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionReadOnlyDTO;
import io.github.amichailides.merimna.exception.ConflictValidationException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Service
@RequiredArgsConstructor
public class EmployeePositionServiceImpl implements EmployeePositionService {
    private final EmployeePositionRepository positionRepository;
    private final EmployeePositionMapper positionMapper;

    @Override
    @Transactional
    public EmployeePositionReadOnlyDTO create(EmployeePositionCreateDTO dto) {
        EmployeePositionCode code = EmployeePositionCode.of(dto.code());

        if (positionRepository.existsByCode(code)) {
            Map<String, String> conflicts = new LinkedHashMap<>();
            conflicts.put("code", ErrorCode.EMPLOYEE_POSITION_ALREADY_EXISTS.getMessageKey());
            throw new ConflictValidationException(conflicts);
        }

        EmployeePosition position = positionMapper.toEntity(dto, code);
        EmployeePosition saved = positionRepository.save(position);
        return positionMapper.toReadDTO(saved);
    }

    @Override
    @Transactional(readOnly = true)
    public List<EmployeePositionReadOnlyDTO> getPositions() {
        return positionRepository.findAll(Sort.by("displayName"))
                .stream()
                .map(positionMapper::toReadDTO)
                .toList();
    }
}
