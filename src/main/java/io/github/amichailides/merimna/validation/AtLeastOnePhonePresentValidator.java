package io.github.amichailides.merimna.validation;

import io.github.amichailides.merimna.dto.EmergencyContactDTO;
import jakarta.validation.ConstraintValidator;
import jakarta.validation.ConstraintValidatorContext;

public class AtLeastOnePhonePresentValidator
        implements ConstraintValidator<AtLeastOnePhonePresent, EmergencyContactDTO> {

    @Override
    public boolean isValid(EmergencyContactDTO dto, ConstraintValidatorContext context) {
        // Αν το DTO είναι null, ο έλεγχος θεωρείται έγκυρος (το null το πιάνει το @NotNull)
        if (dto == null) {
            return true;
        }

        // Ελέγχουμε αν υπάρχει τουλάχιστον ένα τηλέφωνο
        boolean hasPhone = dto.landlinePhone() != null && !dto.landlinePhone().isBlank();
        boolean hasMobile = dto.mobileNumber() != null && !dto.mobileNumber().isBlank();

        return hasPhone || hasMobile;
    }
}