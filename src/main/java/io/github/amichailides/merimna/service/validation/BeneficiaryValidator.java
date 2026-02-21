package io.github.amichailides.merimna.service.validation;

import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.exception.BeneficiaryValidationException;
import io.github.amichailides.merimna.repository.BeneficiaryRepository;
import org.springframework.stereotype.Component;

import java.time.LocalDate;
import java.time.format.DateTimeFormatter;
import java.util.LinkedHashMap;
import java.util.Map;

/**
 * Validator για την οντότητα Beneficiary (Ωφελούμενος).
 * * Ο σκοπός αυτής της κλάσης είναι να συγκεντρώνει όλο το "Semantic Validation"
 * (επιχειρησιακοί κανόνες), διαχωρίζοντάς το από το "Syntactic Validation"
 * (format/regex) που γίνεται στα DTOs μέσω annotations.
 * * Κύριες αρμοδιότητες:
 * 1. Έλεγχος μοναδικότητας στοιχείων στη βάση δεδομένων (π.χ. ΑΜΚΑ).
 * 2. Έλεγχος συνδυαστικών πεδίων (Cross-field validation), όπως η συμφωνία
 * ΑΜΚΑ με την ημερομηνία γέννησης.
 * 3. Επιβολή σύνθετων κοινωνικών κανόνων (π.χ. υποχρεωτικός κηδεμόνας για ανηλίκους).
 * * Αν εντοπιστούν σφάλματα, η κλάση συγκεντρώνει όλα τα μηνύματα σε ένα Map
 * και πετάει μια {@link BeneficiaryValidationException}, η οποία
 * μεταφράζεται αυτόματα από τον GlobalExceptionHandler.
 */

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
