package io.github.amichailides.merimna.employee.dto;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.domain.EmployeePosition;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.Set;

@Builder
public record EmployeeDetailsDTO(
        @Schema(description = "Unique identifier of the employee", example = "23")
        Long id,

        @Schema(description = "Employee first name", example = "Γεώργιος")
        String firstName,

        @Schema(description = "Employee last name", example = "Παπαδόπουλος")
        String lastName,

        @Schema(description = "Employee email address", example = "g.papadopoulos@merimna.gr")
        String email,

        @Schema(description = "Employee mobile number", example = "+306942318223")
        String mobileNumber,

        @Schema(description = "Employee position", example = "CAREGIVER")
        EmployeePosition position,

        @Schema(
                description = "House unit assignments for this employee",
                example = "[{ \"houseUnitCode\": \"UNIT_A\", \"assignmentType\": \"PRIMARY\" }]")
        List<EmployeeAssignmentReadOnlyDTO> assignments,

        @Schema(description = "Employee hire date", example = "2026-02-23")
        LocalDate hireDate,

        @Schema(description = "Employee residential address")
        AddressDTO address,

        @Schema(description = "Indicates whether the employee is active", example = "true")
        boolean active
) {}
