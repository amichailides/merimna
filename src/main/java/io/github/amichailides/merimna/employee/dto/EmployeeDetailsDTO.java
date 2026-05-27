package io.github.amichailides.merimna.employee.dto;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.assignment.dto.EmployeeAssignmentReadOnlyDTO;
import io.github.amichailides.merimna.placement.dto.EmployeePlacementReadOnlyDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;
import java.util.UUID;

@Builder
public record EmployeeDetailsDTO(
        @Schema(
                description = "Public identifier of the employee",
                example = "550e8400-e29b-41d4-a716-446655440000",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        UUID publicId,

        @Schema(
                description = "Employee first name",
                example = "Γεώργιος",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String firstName,

        @Schema(
                description = "Employee last name",
                example = "Παπαδόπουλος",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String lastName,

        @Schema(
                description = "Employee email address",
                example = "g.papadopoulos@merimna.gr",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String contactEmail,

        @Schema(
                description = "Employee mobile number",
                example = "+306942318223",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String mobileNumber,

        @Schema(
                description = "Employee position code",
                example = "CAREGIVER",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String positionCode,

        @Schema(
                description = "Employee position display name",
                example = "Caregiver",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        String positionDisplayName,

        @Schema(
                description = "House unit assignments for this employee",
                example = "[{ \"houseUnitCode\": \"UNIT_A\", \"status\": \"ACTIVE\" }]",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        List<EmployeeAssignmentReadOnlyDTO> assignments,

        @Schema(
                description = "Optional active placement. Null when the employee has no active placement.",
                requiredMode = Schema.RequiredMode.NOT_REQUIRED,
                nullable = true
        )
        EmployeePlacementReadOnlyDTO activePlacement,

        @Schema(
                description = "Employee hire date",
                example = "2026-02-23",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        LocalDate hireDate,

        @Schema(
                description = "Employee residential address",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        AddressDTO address,

        @Schema(
                description = "Indicates whether the employee is active",
                example = "true",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        boolean active
) {}
