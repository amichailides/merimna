package io.github.amichailides.merimna.assignment;

import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.domain.EmployeeHouseUnitAssignment;
import org.springframework.stereotype.Component;

@Component
public class EmployeeAssignmentMapper {
    public EmployeeAssignmentReadOnlyDTO toDTO(EmployeeHouseUnitAssignment entity) {
        if (entity == null) return null;

        return EmployeeAssignmentReadOnlyDTO.builder()
                .id(entity.getId())
                .houseUnitCode(entity.getHouseUnit().getCode())
                .houseUnitDisplayName(entity.getHouseUnit().getDisplayName())
                .assignmentType(entity.getAssignmentType())
                .startDate(entity.getStartDate())
                .endDate(entity.getEndDate())
                .build();
    }
}
