package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.common.error.ErrorCode;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryCreateDTO;
import io.github.amichailides.merimna.beneficiary.dto.BeneficiaryUpdateDTO;
import io.github.amichailides.merimna.exception.ConflictValidationException;
import io.github.amichailides.merimna.exception.DomainValidationException;
import io.github.amichailides.merimna.domain.Beneficiary;
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
 * και πετάει μια {@link DomainValidationException}, η οποία
 * μεταφράζεται αυτόματα από τον GlobalExceptionHandler.
 */

@Component
public class BeneficiaryValidator {

    private final BeneficiaryRepository repository;

    public BeneficiaryValidator(BeneficiaryRepository repository) {
        this.repository = repository;
    }

    public void validateForSave(BeneficiaryCreateDTO dto) {
        checkForCreateConflicts(dto);
        validateCreateBusinessRules(dto);
    }

    public void validateForDischarge(Beneficiary beneficiary) {
        Map<String, String> errors = new LinkedHashMap<>();

        // Future rules:
        // e.g. prevent discharge if there are pending requests or active obligations

        if (!errors.isEmpty()) {
            throw new DomainValidationException(errors);
        }
    }

    // On update, validate the final AMKA/DOB state, not only the patched field,
    // so partial updates cannot introduce inconsistent data.
    public void validateForUpdate(Beneficiary existing, BeneficiaryUpdateDTO dto) {
        Long id = existing.getId();
        String finalAmka = (dto.amka() != null) ? dto.amka() : existing.getAmka();
        LocalDate finalDob = (dto.dateOfBirth() != null) ? dto.dateOfBirth() : existing.getDateOfBirth();
        boolean amkaChanged = dto.amka() != null;
        boolean dobChanged = dto.dateOfBirth() != null;

        checkForUpdateConflicts(id, finalAmka, amkaChanged);
        validateUpdateBusinessRules(finalAmka, finalDob, amkaChanged, dobChanged);
    }

    private void checkForCreateConflicts(BeneficiaryCreateDTO dto) {
        Map<String, String> conflicts = new LinkedHashMap<>();

        if (repository.existsByAmka(dto.amka())) {
            conflicts.put("amka", ErrorCode.AMKA_ALREADY_EXISTS.getMessageKey());
        }

        if (!conflicts.isEmpty()) {
            throw new ConflictValidationException(conflicts);
        }
    }

    private void validateCreateBusinessRules(BeneficiaryCreateDTO dto) {
        Map<String, String> errors = new LinkedHashMap<>();

        if (!isAmkaConsistentWithDob(dto.amka(), dto.dateOfBirth())) {
            errors.put("amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey());
        }

        if (!errors.isEmpty()) {
            throw new DomainValidationException(errors);
        }
    }

    private void checkForUpdateConflicts(Long id, String finalAmka, boolean amkaChanged) {
        Map<String, String> conflicts = new LinkedHashMap<>();

        if (amkaChanged && repository.existsByAmkaAndIdNot(finalAmka, id)) {
            conflicts.put("amka", ErrorCode.AMKA_ALREADY_EXISTS.getMessageKey());
        }

        if (!conflicts.isEmpty()) {
            throw new ConflictValidationException(conflicts);
        }
    }

    private void validateUpdateBusinessRules(String finalAmka, LocalDate finalDob, boolean amkaChanged, boolean dobChanged) {
        Map<String, String> errors = new LinkedHashMap<>();

        if ((amkaChanged || dobChanged) && !isAmkaConsistentWithDob(finalAmka, finalDob)) {
            errors.put("amka", ErrorCode.AMKA_DATE_MISMATCH.getMessageKey());
        }

        if (!errors.isEmpty()) {
            throw new DomainValidationException(errors);
        }
    }


    private String formatToAmkaDate(LocalDate date) {
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("ddMMyy");
        return date.format(formatter);
    }

    /**
     * AMKA rule: first 6 digits must match date of birth (ddMMyy).
     */
    private boolean isAmkaConsistentWithDob(String amka, LocalDate dateOfBirth) {
        if (amka == null || dateOfBirth == null) return true;

        String dobPart = formatToAmkaDate(dateOfBirth);
        return amka.startsWith(dobPart);
    }

}
