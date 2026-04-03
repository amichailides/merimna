package io.github.amichailides.merimna.employee.dto;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.address.dto.AddressUpdateDTO;
import io.github.amichailides.merimna.domain.EmployeePosition;
import io.github.amichailides.merimna.validation.ValidationPatterns;
import io.github.amichailides.merimna.validation.annotations.ValidFirstName;
import io.github.amichailides.merimna.validation.annotations.ValidLastName;
import io.github.amichailides.merimna.validation.annotations.ValidMobile;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.PastOrPresent;
import jakarta.validation.constraints.Pattern;
import lombok.Builder;

import java.time.LocalDate;
import java.util.Set;

@Builder
public record EmployeeUpdateDTO(
        @Schema(description = "Employee first name", example = "Γεώργιος")
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @Schema(description = "Employee last name", example = "Παπαδόπουλος")
        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @Schema(description = "Employee email address", example = "g.papadopoulos@merimna.gr")
        @Email(message = "{email.invalid}", groups = SecondOrder.class)
        String email,

        @Schema(description = "Employee mobile number", example = "+306942318223")
        @ValidMobile(groups = SecondOrder.class)
        String mobileNumber,

        @Schema(description = "Employee position", example = "CAREGIVER")
        EmployeePosition position,

        @Schema(description = "Assigned house unit codes", example = "[\"UNIT_A\", \"UNIT_B\"]")
        Set<
                @Pattern(
                        regexp = ValidationPatterns.HOUSE_UNIT_CODE,
                        message = "{employee.houseUnitCode.invalid}",
                        groups = SecondOrder.class
                )
                        String> houseUnitCodes,

        @Schema(description = "Employee hire date", example = "2026-02-23")
        @PastOrPresent(message = "{employee.hireDate.pastOrPresent}", groups = SecondOrder.class)
        LocalDate hireDate,

        @Schema(description = "Employee residential address")
        @Valid
        AddressUpdateDTO address
) {}
