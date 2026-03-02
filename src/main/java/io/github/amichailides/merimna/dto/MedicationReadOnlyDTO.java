package io.github.amichailides.merimna.dto;

public record MedicationReadOnlyDTO(
        Long id,
        String name,
        String dosage,
        String frequency,
        String administrationTimes,
        String instructions
) {}
