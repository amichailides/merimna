package io.github.amichailides.merimna.employee.dto;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmployeeReadOnlyDTO(
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

        @Schema(description = "Employee residential address")
        AddressDTO address,

        @Schema(description = "Employee hire date", example = "2026-02-23")
        LocalDate hireDate
) {}
