package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.HouseUnit;
import lombok.Builder;
import java.time.LocalDate;

@Builder
public record BeneficiarySaveDTO(
        String firstName,
        String lastName,
        String amka,
        LocalDate dateOfBirth,
        HouseUnit houseUnit,
        AddressDTO permanentAddress,
        EmergencyContactDTO emergencyContact
) {}
