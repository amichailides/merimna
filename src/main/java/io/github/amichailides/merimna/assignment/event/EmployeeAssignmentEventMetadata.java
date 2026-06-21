package io.github.amichailides.merimna.assignment.event;

import java.time.LocalDate;
import java.util.LinkedHashMap;
import java.util.Map;
import java.util.UUID;

final class EmployeeAssignmentEventMetadata {

    private EmployeeAssignmentEventMetadata() {
    }

    static Map<String, Object> of(
            UUID employeePublicId,
            UUID houseUnitPublicId,
            String houseUnitCode,
            String houseUnitDisplayName,
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("employeePublicId", employeePublicId);
        metadata.put("houseUnitPublicId", houseUnitPublicId);
        metadata.put("houseUnitCode", houseUnitCode);
        metadata.put("houseUnitDisplayName", houseUnitDisplayName);
        metadata.put("startDate", startDate);
        metadata.put("endDate", endDate);
        return metadata;
    }
}