package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.HouseUnit;
import lombok.Builder;

import java.time.LocalDate;
import java.util.List;


@Builder
public record BeneficiaryReadOnlyDTO (
    Long id,
    String firstName,
    String lastName,
    String amka,
    LocalDate dateOfBirth,
    Boolean isActive,
    HouseUnit houseUnit,
    AddressDTO permanentAddress,
    EmergencyContactDTO emergencyContact,
    List<MedicationReadOnlyDTO> medicalTreatment,
    List<AllergyReadOnlyDTO> allergies
) {}
