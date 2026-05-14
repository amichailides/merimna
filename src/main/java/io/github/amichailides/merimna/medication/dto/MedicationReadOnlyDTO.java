package io.github.amichailides.merimna.medication.dto;

import java.util.UUID;

public record MedicationReadOnlyDTO(
        UUID publicId,
        String name,
        String dosage,
        String frequency,
        String administrationTimes,
        String instructions
) {}
