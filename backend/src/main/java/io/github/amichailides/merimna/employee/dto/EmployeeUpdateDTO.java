package io.github.amichailides.merimna.employee.dto;

import io.github.amichailides.merimna.address.dto.AddressUpdateDTO;
import io.github.amichailides.merimna.validation.annotations.*;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Past;
import jakarta.validation.constraints.PastOrPresent;
import lombok.Builder;

import java.time.LocalDate;

@Builder
public record EmployeeUpdateDTO(
        @Schema(description = "Employee first name", example = "Γεώργιος")
        @ValidFirstName(groups = SecondOrder.class)
        String firstName,

        @Schema(description = "Employee last name", example = "Παπαδόπουλος")
        @ValidLastName(groups = SecondOrder.class)
        String lastName,

        @Schema(description = "Employee date of birth", example = "1988-04-12")
        @Past(message = "{employee.dateOfBirth.past}", groups = SecondOrder.class)
        LocalDate dateOfBirth,

        @Schema(description = "Employee email address", example = "g.papadopoulos@merimna.gr")
        @ValidEmail(groups = SecondOrder.class)
        String contactEmail,

        @OptionalNotBlank(message = "{mobile.notBlank}", groups = FirstOrder.class)
        @Schema(description = "Employee mobile number", example = "+306942318223")
        @ValidMobile(groups = SecondOrder.class)
        String mobileNumber,

        @Schema(description = "Employee position code", example = "CAREGIVER")
        @OptionalNotBlank(message = "{employee.positionCode.blank}", groups = FirstOrder.class)
        String positionCode,

        @Schema(description = "Employee hire date", example = "2026-02-23")
        @PastOrPresent(message = "{employee.hireDate.pastOrPresent}", groups = SecondOrder.class)
        LocalDate hireDate,

        @Schema(description = "Emergency contact full name", example = "Μαρία Παπαδοπούλου")
        @OptionalNotBlank(message = "{employee.emergencyContactName.notBlank}", groups = FirstOrder.class)
        String emergencyContactName,

        @Schema(description = "Emergency contact phone number", example = "+306971112233")
        @OptionalNotBlank(message = "{employee.emergencyContactPhoneNumber.notBlank}", groups = FirstOrder.class)
        @ValidMobile(groups = SecondOrder.class)
        String emergencyContactPhoneNumber,

        @Schema(description = "Employee residential address")
        @Valid
        AddressUpdateDTO address
) {}