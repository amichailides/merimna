package io.github.amichailides.merimna.domain;

import io.github.amichailides.merimna.employeePosition.exception.InvalidEmployeePositionCodeException;
import io.github.amichailides.merimna.validation.ValidationPatterns;
import jakarta.persistence.Column;
import jakarta.persistence.Embeddable;

import java.util.Locale;
import java.util.Objects;

@Embeddable
public class EmployeePositionCode {

    @Column(name = "code", nullable = false, unique = true, length = 50)
    private String value;

    protected EmployeePositionCode() {
        // JPA
    }

    public EmployeePositionCode(String value) {
        this.value = normalizeAndValidate(value);
    }

    public String getValue() {
        return value;
    }

    private String normalizeAndValidate(String raw) {
        if (raw == null) {
            throw new InvalidEmployeePositionCodeException("null");
        }

        String normalized = raw.trim().toUpperCase(Locale.ROOT);

        if (normalized.isBlank()) {
            throw new InvalidEmployeePositionCodeException(raw);
        }

        if (!normalized.matches(ValidationPatterns.EMPLOYEE_POSITION_CODE)) {
            throw new InvalidEmployeePositionCodeException(raw);
        }

        return normalized;
    }

    public static EmployeePositionCode of(String value) {
        return new EmployeePositionCode(value);
    }

    @Override
    public boolean equals(Object other) {
        if (this == other) return true;
        if (!(other instanceof EmployeePositionCode that)) return false;
        return Objects.equals(value, that.value);
    }

    @Override
    public int hashCode() {
        return Objects.hash(value);
    }

    @Override
    public String toString() {
        return value;
    }
}