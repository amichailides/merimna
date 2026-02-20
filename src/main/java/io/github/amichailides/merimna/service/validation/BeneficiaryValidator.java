package io.github.amichailides.merimna.service.validation;

import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.exception.BeneficiaryValidationException;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

@Component
public class BeneficiaryValidator {

    private final BeneficiaryRepository repository;

    public BeneficiaryValidator(BeneficiaryRepository repository) {
        this.repository = repository;
    }

    public void validateForSave(BeneficiarySaveDTO dto) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (repository.existsByAmka(dto.amka())) {
            errors.put("amka", "beneficiary.amkaAlreadyExists");
        }

        // 2. Έλεγχος ΑΜΚΑ vs Ημερομηνία Γέννησης
        if (dto.amka() != null && dto.dateOfBirth() != null) {
            String dobPart = formatToAmkaDate(dto.dateOfBirth()); // π.χ. 250395
            if (!dto.amka().startsWith(dobPart)) {
                errors.put("amka", "beneficiary.amkaDateMismatch");
            }
        }

        // άλλα business checks εδώ...


        if (!errors.isEmpty()) {
            throw new BeneficiaryValidationException(errors);
        }
    }

    // Helper για να πάρει τα πρώτα 6 ψηφία του ΑΜΚΑ από το Date
    private String formatToAmkaDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        return date.format(formatter);
    }

}
