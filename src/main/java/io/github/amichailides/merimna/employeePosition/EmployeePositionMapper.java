package io.github.amichailides.merimna.employeePosition;

import io.github.amichailides.merimna.domain.EmployeePosition;
import io.github.amichailides.merimna.domain.EmployeePositionCode;
import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionCreateDTO;
import io.github.amichailides.merimna.employeePosition.dto.EmployeePositionReadOnlyDTO;
import org.springframework.stereotype.Component;

@Component
public class EmployeePositionMapper {

    public EmployeePositionReadOnlyDTO toReadDTO(EmployeePosition entity) {
        if (entity == null) return null;

        return EmployeePositionReadOnlyDTO.builder()
                .code(entity.getCode().getValue())
                .displayName(entity.getDisplayName())
                .requiresExclusivePlacement(entity.isRequiresExclusivePlacement())
                .build();
    }

    public EmployeePosition toEntity(EmployeePositionCreateDTO dto, EmployeePositionCode code) {

        return EmployeePosition.builder()
                .code(code)
                .displayName(dto.displayName())
                .requiresExclusivePlacement(dto.requiresExclusivePlacement())
                .build();
    }
}
