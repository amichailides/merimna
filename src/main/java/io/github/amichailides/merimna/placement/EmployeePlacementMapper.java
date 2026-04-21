package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.domain.EmployeePlacement;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import org.springframework.stereotype.Component;

@Component
public class EmployeePlacementMapper {
    public EmployeePlacementReadOnlyDTO toReadOnlyDTO(EmployeePlacement p) {
        if (p == null) return null;

        return EmployeePlacementReadOnlyDTO.builder()
                .id(p.getId())
                .employeeId(p.getEmployee().getId())
                .houseUnitCode(p.getHouseUnit().getCode())
                .houseUnitDisplayName(p.getHouseUnit().getDisplayName())
                .startDateTime(p.getStartDateTime())
                .endDateTime(p.getEndDateTime())
                .reason(p.getReason())
                .active(p.getEndDateTime() == null)
                .build();
    }
}
