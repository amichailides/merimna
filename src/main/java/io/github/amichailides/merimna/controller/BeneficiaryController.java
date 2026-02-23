package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.common.ApiResponse;
import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.service.BeneficiaryService;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.github.amichailides.merimna.specification.BeneficiarySpecifications;

import java.util.List;

@RestController
@Validated
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {
    private final BeneficiaryService service;

    /**
     * Δημιουργεί έναν νέο ωφελούμενο.
     * Η επικύρωση ενεργοποιείται μέσω του {@link ValidationGroupSequence} για short-circuiting λογική.
     */
    @PostMapping
    public ResponseEntity<ApiResponse<BeneficiaryReadOnlyDTO>> create(
            @Validated(ValidationGroupSequence.class) @RequestBody BeneficiarySaveDTO dto) {

        BeneficiaryReadOnlyDTO responseBody = service.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(ApiResponse.success(
                        responseBody,
                        "Ο ωφελούμενος δημιουργήθηκε με επιτυχία!",
                        HttpStatus.CREATED.value()));
    }

    @GetMapping("/{id}")
    public ResponseEntity<ApiResponse<BeneficiaryReadOnlyDTO>> getById(
            @Positive @PathVariable Long id) {

        BeneficiaryReadOnlyDTO responseBody = service.findById(id);
        return ResponseEntity.ok(ApiResponse.success(
                responseBody,
                "Επιτυχής ανάκτηση στοιχείων ωφελούμενου",
                HttpStatus.OK.value()));
    }

    @GetMapping("/amka/{amka}")
    public ResponseEntity<BeneficiaryReadOnlyDTO> getByAmka(
            @Positive @PathVariable @ValidAmka String amka) {
        BeneficiaryReadOnlyDTO dto = service.findByAmka(amka);
        return ResponseEntity.ok(dto);
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
            @RequestParam(name = "includeInactive", defaultValue = "false") boolean includeInactive, Pageable pageable) {
        Page<BeneficiaryReadOnlyDTO> page = service.findAllBeneficiaries(includeInactive, pageable);
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<BeneficiaryReadOnlyDTO> deactivate(@PathVariable Long id) {
        BeneficiaryReadOnlyDTO updated = service.deactivate(id);
        return ResponseEntity.ok(updated);
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
    public ResponseEntity<Page<BeneficiaryReadOnlyDTO>> search(@RequestParam(required = false) String term,
                                                               Pageable pageable) {
        Page<BeneficiaryReadOnlyDTO> results = service.search(term, pageable);
        return ResponseEntity.ok(results);
    }

    @GetMapping("unit/{houseUnit}")
    public ResponseEntity<List<BeneficiaryReadOnlyDTO>> getByHouseUnit(@PathVariable HouseUnit houseUnit) {
        List<BeneficiaryReadOnlyDTO> byHouse = service.findAllByHouseUnit(houseUnit);
        return ResponseEntity.ok(byHouse);
    }

}
