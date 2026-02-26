package io.github.amichailides.merimna.dto;

public record MedicationCreateDTO(
        String name,
        String dosage,
        String frequency,
        String administrationTimes,
        String instructions
) {}