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
            LocalDate startDate,
            LocalDate endDate
    ) {
        Map<String, Object> metadata = new LinkedHashMap<>();
        metadata.put("employeePublicId", employeePublicId);
        metadata.put("houseUnitPublicId", houseUnitPublicId);
        metadata.put("startDate", startDate.toString());
        metadata.put("endDate", endDate != null ? endDate.toString() : null);
        return metadata;
    }
}