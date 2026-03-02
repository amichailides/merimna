package io.github.amichailides.merimna.dto;

public record MedicationUpdateDTO(
        //TODO validation - groups
        String name,
        String dosage,
        String frequency,
        String administrationTimes,
        String instructions
) {}
