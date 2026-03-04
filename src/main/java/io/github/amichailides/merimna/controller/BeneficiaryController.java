package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.common.ApiResponse;
import io.github.amichailides.merimna.dto.*;
import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.service.BeneficiaryService;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.context.MessageSource;
import org.springframework.context.i18n.LocaleContextHolder;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.github.amichailides.merimna.specification.BeneficiarySpecifications;

// TODO: Standardize API responses after feature completion
// - Apply pure REST style to all endpoints (remove ApiResponse wrapper)
// - Ensure all endpoints follow consistent pattern
// - Use Location header for created resources
// - Success messages in body removed; frontend handles notifications
@RestController
@Validated
@RequestMapping("/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {
    private final BeneficiaryService service;
    private final MessageSource messageSource;

    /**
     * Δημιουργεί έναν νέο ωφελούμενο.
     * Η επικύρωση ενεργοποιείται μέσω του {@link ValidationGroupSequence} για short-circuiting λογική.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BeneficiaryReadOnlyDTO>> create(
            @Validated(ValidationGroupSequence.class) @RequestBody BeneficiarySaveDTO dto) {

        BeneficiaryReadOnlyDTO beneficiary = service.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        beneficiary,
                        getMessage("beneficiary.create.success"),
                        HttpStatus.CREATED.value()));
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BeneficiaryReadOnlyDTO> updateBeneficiary(
            @PathVariable Long id,
            @Validated(ValidationGroupSequence.class) @RequestBody BeneficiaryUpdateDTO dto) {

        BeneficiaryReadOnlyDTO beneficiary = service.updateBeneficiary(id, dto);
        return ResponseEntity.ok(beneficiary);
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BeneficiaryReadOnlyDTO>> getById(
            @Positive @PathVariable Long id) {

        BeneficiaryReadOnlyDTO beneficiary = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(
                beneficiary,
                getMessage("beneficiary.fetch.success"),
                HttpStatus.OK.value()));
    }

    @GetMapping(params = "amka")
    public ResponseEntity<ApiResponse<BeneficiaryReadOnlyDTO>> getByAmka(
            @RequestParam @ValidAmka String amka) {

        BeneficiaryReadOnlyDTO beneficiary = service.findByAmka(amka);

        return ResponseEntity.ok(ApiResponse.success(
                beneficiary,
                getMessage("beneficiary.fetch.success"),
                HttpStatus.OK.value()
        ));
    }

    /**
     * * TODO: Future Refactoring
     * -------------------------
     * 1. Αν προστεθούν πολλά φίλτρα (π.χ. ημερομηνίες, ηλικίες),
     * μετατροπή των Params σε BeneficiarySearchDTO.
     * 2. Προσθήκη επιπλέον κριτηρίων αναζήτησης (π.χ. ενεργοί/ανενεργοί ωφελούμενοι).
     */
    @GetMapping
    public ResponseEntity<Page<BeneficiaryReadOnlyDTO>> getAllBeneficiaries(
            @RequestParam(name = "includeInactive", defaultValue = "false") boolean includeInactive,
            @RequestParam(name = "houseUnit", required = false) HouseUnit houseUnit, // Προαιρετικό φίλτρο
            Pageable pageable) {

        Page<BeneficiaryReadOnlyDTO> page = service.findAllBeneficiaries(includeInactive, houseUnit, pageable);
        return ResponseEntity.ok(page);
    }

    @PostMapping("/{id}/discharge")
    public ResponseEntity<ApiResponse<BeneficiaryReadOnlyDTO>> discharge(@PathVariable Long id) {

        BeneficiaryReadOnlyDTO updated = service.discharge(id);
        return ResponseEntity.ok(ApiResponse.success(
                updated,
                getMessage("beneficiary.discharge.success"),
                HttpStatus.OK.value()));
    }

    /**
     * Αναζήτηση ωφελουμένων με global search term.
     * <p>Χρησιμοποιεί {@link BeneficiarySpecifications} για αναζήτηση σε πολλαπλά πεδία.</p>
     * * <p><b>TODO: Scalability & Advanced Filtering</b><br>
     * Η τρέχουσα υλοποίηση καλύπτει το 90% των αναγκών του MVP. Η μετάβαση σε
     * {@code BeneficiarySearchDTO} θα πραγματοποιηθεί εάν:</p>
     * <ul>
     * <li>Απαιτηθούν σύνθετα φίλτρα (π.χ. "Στέγη Α' + Ηλικία > 70")</li>
     * <li>Οι {@code @RequestParam} ξεπεράσουν τις 3 (Clean Code / YAGNI)</li>
     * <li>Απαιτηθεί Advanced Reporting ή δυναμικό Export δεδομένων</li>
     * </ul>
     * <p>Η μελλοντική υλοποίηση θα βασίζεται σε Specification Builder για δυναμικά Predicates.</p>
     */
    @GetMapping("/search")
    public ResponseEntity<Page<BeneficiaryReadOnlyDTO>> search(
            @RequestParam(required = false) String term,
            @PageableDefault(size = 5, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<BeneficiaryReadOnlyDTO> results = service.search(term, pageable);
        return ResponseEntity.ok(results);
    }

    /**
     * Helper για να τραβάμε τα μηνύματα επιτυχίας.
     */
    private String getMessage(String code) {
        return messageSource.getMessage(code, null, LocaleContextHolder.getLocale());
    }
}



