package io.github.amichailides.merimna.service.validation;

import io.github.amichailides.merimna.common.ErrorCode;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.dto.BeneficiaryUpdateDTO;
import io.github.amichailides.merimna.exception.BeneficiaryAlreadyExistsException;
import io.github.amichailides.merimna.exception.BeneficiaryValidationException;
import io.github.amichailides.merimna.model.Beneficiary;
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
 * 3. Επιβολή σύνθετων κοινωνικών κανόνων (π.χ. Υποχρεωτικός κηδεμόνας για ανηλίκους).
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

        String amka = dto.amka();
        LocalDate dob = dto.dateOfBirth();

        if (repository.existsByAmka(amka)) {
            errors.put("amka", ErrorCode.AMKA_ALREADY_EXISTS.getMessageKey());
        }
        else if (!isAmkaConsistentWithDob(amka, dob)) {
            errors.put("amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey());
        }

        // άλλα business checks εδώ...


        if (!errors.isEmpty()) {
            throw new BeneficiaryValidationException(errors);
        }
    }

    public void validateForDeactivate(Beneficiary beneficiary) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (beneficiary.getIsActive() != null && !beneficiary.getIsActive()) {
            errors.put("isActive", ErrorCode.BENEFICIARY_ALREADY_INACTIVE.getMessageKey());
        }

        // Εδώ μελλοντικά προσθέτουμε και άλλους κανόνες,
        // π.χ. Να μην απενεργοποιείται αν έχει εκκρεμείς αιτήσεις.

        if (!errors.isEmpty()) {
            throw new BeneficiaryValidationException(errors);
        }
    }

    // Ελέγχουμε αν το AMKA υπάρχει ήδη σε άλλον ωφελούμενο (μόνο αν άλλαξε).
    // Αν ναι, fast-fail — δεν έχει νόημα να συνεχίσουμε.
    // Αλλιώς, αν άλλαξε AMKA ή DOB, ελέγχουμε consistency μεταξύ τους,
    // γιατί ακόμα και PATCH μόνο του DOB μπορεί να οδηγήσει σε data corruption
    // (π.χ. AMKA 150580... με DOB 1992-11-20).
    public void validateForUpdate(Beneficiary existing, BeneficiaryUpdateDTO dto) {
        Map<String, String> errors = new LinkedHashMap<>();
        Long id = existing.getId();

        String finalAmka = (dto.amka() != null) ? dto.amka() : existing.getAmka();
        LocalDate finalDob = (dto.dateOfBirth() != null) ? dto.dateOfBirth() : existing.getDateOfBirth();

        boolean amkaChanged = dto.amka() != null;
        boolean dobChanged = dto.dateOfBirth() != null;

        if (amkaChanged && repository.existsByAmkaAndIdNot(finalAmka, id)) {
            errors.put("amka", ErrorCode.AMKA_ALREADY_EXISTS.getMessageKey());
        } else if ((amkaChanged || dobChanged) && !isAmkaConsistentWithDob(finalAmka, finalDob)) {
            errors.put("amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey());
        }

        if (!errors.isEmpty()) {
            throw new BeneficiaryValidationException(errors);
        }
    }

    // Helper για να πάρει τα πρώτα 6 ψηφία του ΑΜΚΑ από το Date
    private String formatToAmkaDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        return date.format(formatter);
    }

    /**
     * Ελέγχει αν τα πρώτα 6 ψηφία του ΑΜΚΑ ταυτίζονται με την ημερομηνία γέννησης.
     */
    private boolean isAmkaConsistentWithDob(String amka, LocalDate dateOfBirth) {
        if (amka == null || dateOfBirth == null) return true;

        String dobPart = formatToAmkaDate(dateOfBirth);
        return amka.startsWith(dobPart);
    }

}
