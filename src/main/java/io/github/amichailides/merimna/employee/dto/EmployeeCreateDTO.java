package io.github.amichailides.merimna.employee.dto;

import io.github.amichailides.merimna.address.dto.AddressDTO;
import io.github.amichailides.merimna.validation.annotations.ValidFirstName;
import io.github.amichailides.merimna.validation.annotations.ValidLastName;
import io.github.amichailides.merimna.validation.annotations.ValidMobile;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.*;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmployeeCreateDTO(

        @Schema(description = "Employee first name", example = "Γεώργιος")
        @NotBlank(message = "{firstName.required}", groups = FirstOrder.class)
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @Schema(description = "Employee last name", example = "Παπαδόπουλος")
        @NotBlank(message = "{lastName.required}", groups = FirstOrder.class)
        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @Schema(description = "Employee email address", example = "g.papadopoulos@merimna.gr")
        @NotBlank(message = "{email.required}", groups = FirstOrder.class)
        @Email(message = "{email.invalid}", groups = SecondOrder.class)
        String email,

        @Schema(description = "Employee mobile number", example = "+306942318223")
        @NotBlank(message = "{mobile.required}", groups = FirstOrder.class)
        @ValidMobile(groups = SecondOrder.class)
        String mobileNumber,

        @Schema(description = "Employee residential address")
        @Valid
        @NotNull(message = "{address.required}")
        AddressDTO address,

        @Schema(description = "Employee position code", example = "CAREGIVER")
        @NotNull(message = "{employee.position.required}", groups = FirstOrder.class)
        //TODO @Pattern
        String positionCode,

        @Schema(description = "Employee hire date", example = "2026-02-23")
        @NotNull(message = "{employee.hireDate.required}", groups = FirstOrder.class)
        @PastOrPresent(message = "{employee.hireDate.pastOrPresent}", groups = SecondOrder.class)
        LocalDate hireDate
) {}
