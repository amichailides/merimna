package io.github.amichailides.merimna.employee.systemaccess.dto;

import io.github.amichailides.merimna.validation.annotations.ValidEmail;
import jakarta.validation.constraints.NotBlank;

public record GrantEmployeeAccessRequest(

        @NotBlank
        @ValidEmail
        String accountEmail

) {}