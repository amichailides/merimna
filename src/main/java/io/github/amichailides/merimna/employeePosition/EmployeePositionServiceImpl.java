package io.github.amichailides.merimna.employeePosition;

import io.github.amichailides.merimna.domain.EmployeePosition;
import io.github.amichailides.merimna.domain.EmployeePositionCode;
import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionCreateDTO;
import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionReadOnlyDTO;
import io.github.amichailides.merimna.employeePosition.exception.EmployeePositionAlreadyExistsException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
@RequiredArgsConstructor
public class EmployeePositionServiceImpl implements EmployeePositionService{
    private final EmployeePositionRepository positionRepository;
    private final EmployeePositionMapper positionMapper;

    @Override
    @Transactional
    public EmployeePositionReadOnlyDTO create(EmployeePositionCreateDTO dto) {
        EmployeePositionCode code = EmployeePositionCode.of(dto.code());

        if (positionRepository.existsByCode(code)) {
            throw new  EmployeePositionAlreadyExistsException(code.getValue());
        }

        EmployeePosition position = positionMapper.toEntity(dto, code);
        EmployeePosition saved =positionRepository.save(position);
        return positionMapper.toReadDTO(saved);
    }
}
