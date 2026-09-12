package io.github.amichailides.merimna.placement;

import io.github.amichailides.merimna.domain.EmployeePlacement;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import org.springframework.stereotype.Component;

import java.time.LocalDate;

@Component
public class EmployeePlacementMapper {
    public EmployeePlacementReadOnlyDTO toReadOnlyDTO(EmployeePlacement p) {
        if (p == null) return null;

        return EmployeePlacementReadOnlyDTO.builder()
                .publicId(p.getPublicId())
                .houseUnitCode(p.getHouseUnit().getCode())
                .houseUnitDisplayName(p.getHouseUnit().getDisplayName())
                .startDate(p.getStartDate())
                .endDate(p.getEndDate())
                .reason(p.getReason())
                .reasonDisplayName(p.getReason().getDisplayName())
                .active(p.isActive(LocalDate.now()))
                .build();
    }
}
