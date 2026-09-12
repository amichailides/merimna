package io.github.amichailides.merimna.placement.event;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

final class EmployeePlacementEventMetadata {

    private EmployeePlacementEventMetadata() {
    }

    static Map<String, Object> of(
            UUID employeePublicId,
            UUID houseUnitPublicId,
            String houseUnitCode,
            String houseUnitDisplayName,
            LocalDate startDate,
            LocalDate endDate,
            String reasonCode,
            String reasonDisplayName
    ) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("employeePublicId", employeePublicId);
        metadata.put("houseUnitPublicId", houseUnitPublicId);
        metadata.put("houseUnitCode", houseUnitCode);
        metadata.put("houseUnitDisplayName", houseUnitDisplayName);
        metadata.put("startDate", startDate != null ? startDate.toString() : null);
        metadata.put("endDate", endDate != null ? endDate.toString() : null);
        metadata.put("reasonCode", reasonCode);
        metadata.put("reasonDisplayName", reasonDisplayName);
        return metadata;
    }
}