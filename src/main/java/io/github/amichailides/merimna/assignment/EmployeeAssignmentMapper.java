package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.domain.EmployeeAssignment;
import org.springframework.stereotype.Component;

@Component
public class EmployeeAssignmentMapper {
    public EmployeeAssignmentReadOnlyDTO toDTO(EmployeeAssignment entity) {
        if (entity == null) return null;

        return EmployeeAssignmentReadOnlyDTO.builder()
                .id(entity.getId())
                .houseUnitCode(entity.getHouseUnit().getCode())
                .houseUnitDisplayName(entity.getHouseUnit().getDisplayName())
                .status(entity.getStatus())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }
}
