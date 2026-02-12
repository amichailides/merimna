package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.dto.BeneficiaryReadOnlyDTO;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;
import io.github.amichailides.merimna.service.BeneficiaryService;
import jakarta.validation.Valid;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
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
    public ResponseEntity<BeneficiaryReadOnlyDTO> findById(@Positive @PathVariable Long id) {
        BeneficiaryReadOnlyDTO dto = service.findById(id);
        return ResponseEntity.ok(dto);

    }

}
