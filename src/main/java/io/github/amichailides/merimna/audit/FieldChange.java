package io.github.amichailides.merimna.audit;

public record FieldChange(
        String fieldName,
        Object oldValue,
        Object newValue
) {}