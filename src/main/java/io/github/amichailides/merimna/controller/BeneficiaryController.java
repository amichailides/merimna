package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.model.HouseUnit;
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

import java.util.List;

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

    @GetMapping("/amka/{amka}")
    public ResponseEntity<BeneficiaryReadOnlyDTO> getByAmka(@Positive @PathVariable String amka){
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
            @RequestParam(name = "includeInactive", defaultValue = "false")  boolean includeInactive, Pageable pageable) {
        Page<BeneficiaryReadOnlyDTO> page = service.findAllBeneficiaries(includeInactive, pageable);
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

    @GetMapping("unit/{houseUnit}")
    public ResponseEntity<List<BeneficiaryReadOnlyDTO>> getByHouseUnit(@PathVariable HouseUnit houseUnit) {
        List<BeneficiaryReadOnlyDTO> byHouse = service.findAllByHouseUnit(houseUnit);
        return ResponseEntity.ok(byHouse);
    }

}
