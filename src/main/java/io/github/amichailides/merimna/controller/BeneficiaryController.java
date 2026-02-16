package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.service.BeneficiaryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;

@RestController
@Validated
@RequestMapping("/api/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {
    private final BeneficiaryService service;

    @PostMapping
    public ResponseEntity<BeneficiaryReadOnlyDTO> save (@Valid @RequestBody BeneficiarySaveDTO dto) {
        BeneficiaryReadOnlyDTO responseBody = service.save(dto);
        return ResponseEntity
                .status(HttpStatus.CREATED)
                .body(responseBody);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaryReadOnlyDTO> getById(@Positive @PathVariable Long id) {
        BeneficiaryReadOnlyDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);

    }

    @GetMapping("/{amka}")
    public ResponseEntity<BeneficiaryReadOnlyDTO> getByAmka(@Positive @PathVariable String amka){
        BeneficiaryReadOnlyDTO dto = service.findByAmka(amka);
        return ResponseEntity.ok(dto);
    }

    /**
     * TODO: Refactor to Global Search Pattern
     * ---------------------------------------
     * Στόχος: Ενοποίηση των φίλτρων αναζήτησης (AMKA, LastName, κλπ) σε ένα endpoint.
     * * Σχέδιο Υλοποίησης:
     * 1. Δημιουργία BeneficiarySearchDTO για bind των προαιρετικών Query Params.
     * 2. Χρήση Spring Data JPA Specifications στο Service για δυναμικά queries.
     * 3. Διατήρηση του Pageable για ομοιόμορφη σελιδοποίηση.
     * * Σημείωση: Οι μέθοδοι /{id} και /amka/{amka} θα παραμείνουν για
     * direct "List-to-Detail" πρόσβαση και επιστροφή 404 αν δεν βρεθούν.
     */
    @GetMapping
    public ResponseEntity<Page<BeneficiaryReadOnlyDTO>> getAllBeneficiaries(Pageable pageable) {
        Page<BeneficiaryReadOnlyDTO> page = service.findAllBeneficiaries(pageable);
        return ResponseEntity.ok(page);
    }

    @PatchMapping("/{id}/deactivate")
    public ResponseEntity<BeneficiaryReadOnlyDTO> deactivate (@PathVariable Long id) {
        BeneficiaryReadOnlyDTO updated = service.deactivate(id);
        return ResponseEntity.ok(updated);
    }

    @GetMapping("/search")
    public ResponseEntity<Page<BeneficiaryReadOnlyDTO>> search (@RequestParam(required = false) String term,
                                                                Pageable pageable) {
        Page<BeneficiaryReadOnlyDTO> results = service.search(term, pageable);
        return ResponseEntity.ok(results);
    }
}
