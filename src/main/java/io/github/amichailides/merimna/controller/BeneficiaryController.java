package io.github.amichailides.merimna.controller;

import io.github.amichailides.merimna.common.ConflictErrorResponse;
import io.github.amichailides.merimna.common.PageResponse;
import io.github.amichailides.merimna.common.ValidationErrorResponse;
import io.github.amichailides.merimna.dto.*;
import io.github.amichailides.merimna.model.HouseUnit;
import io.github.amichailides.merimna.service.BeneficiaryService;
import io.github.amichailides.merimna.validation.annotations.ValidAmka;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.constraints.Positive;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
import io.github.amichailides.merimna.specification.BeneficiarySpecifications;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.net.URI;

@Tag(name = "Beneficiaries", description = "Operations related to beneficiaries")
@RestController
@Validated
@RequestMapping("/beneficiaries")
@RequiredArgsConstructor
public class BeneficiaryController {
    private final BeneficiaryService service;

    /**
     * Η επικύρωση ενεργοποιείται μέσω του {@link ValidationGroupSequence} για short-circuiting λογική.
     */
    @Operation(summary = "Create beneficiary")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Beneficiary created successfully"),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Conflict",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConflictErrorResponse.class)
                    )
            )
    })
    @PostMapping
    public ResponseEntity<BeneficiaryReadOnlyDTO> create(
            @Validated(ValidationGroupSequence.class) @RequestBody BeneficiarySaveDTO dto) {

        BeneficiaryReadOnlyDTO beneficiary = service.save(dto);
        return ResponseEntity
                .created(buildLocationUri(beneficiary.id()))
                .body(beneficiary);
    }

    @PatchMapping("/{id}")
    public ResponseEntity<BeneficiaryReadOnlyDTO> updateBeneficiary(
            @PathVariable Long id,
            @Validated(ValidationGroupSequence.class) @RequestBody BeneficiaryUpdateDTO dto) {

        BeneficiaryReadOnlyDTO beneficiary = service.updateBeneficiary(id, dto);
        return ResponseEntity.ok(beneficiary);
    }

    @GetMapping("/{id}")
    public ResponseEntity<BeneficiaryReadOnlyDTO> getById(
            @Positive @PathVariable Long id) {

        BeneficiaryReadOnlyDTO beneficiary = service.findById(id);
        return ResponseEntity.ok(beneficiary);
    }

    @GetMapping("/by-amka/{amka}")
    public ResponseEntity<BeneficiaryReadOnlyDTO> getByAmka(
            @ValidAmka @PathVariable String amka) {

        return ResponseEntity.ok(service.findByAmka(amka));
    }

    /**
     * * TODO: Future Refactoring
     * -------------------------
     * 1. Αν προστεθούν πολλά φίλτρα (π.χ. ημερομηνίες, ηλικίες),
     * μετατροπή των Params σε BeneficiarySearchDTO.
     * 2. Προσθήκη επιπλέον κριτηρίων αναζήτησης (π.χ. ενεργοί/ανενεργοί ωφελούμενοι).
     */
    @Operation(summary = "Get all beneficiaries")
    @io.swagger.v3.oas.annotations.responses.ApiResponse(
            responseCode = "200",
            useReturnTypeSchema = true
    )
    @GetMapping
    public ResponseEntity<PageResponse<BeneficiaryReadOnlyDTO>> getAllBeneficiaries(
            @RequestParam(name = "includeInactive", defaultValue = "false") boolean includeInactive,
            @RequestParam(name = "houseUnit", required = false) HouseUnit houseUnit, // Προαιρετικό φίλτρο
            @ParameterObject Pageable pageable ) {

        Page<BeneficiaryReadOnlyDTO> page = service.findAllBeneficiaries(includeInactive, houseUnit, pageable);
        return ResponseEntity.ok(PageResponse.<BeneficiaryReadOnlyDTO>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build()
        );
    }

    @PostMapping("/{id}/discharge")
    public ResponseEntity<BeneficiaryReadOnlyDTO> discharge(@PathVariable Long id) {

        BeneficiaryReadOnlyDTO updated = service.discharge(id);
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
    public ResponseEntity<PageResponse<BeneficiaryReadOnlyDTO>> search(
            @RequestParam(required = false) String term,
            @PageableDefault(size = 5, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<BeneficiaryReadOnlyDTO> page  = service.search(term, pageable);

        return ResponseEntity.ok(PageResponse.<BeneficiaryReadOnlyDTO>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build()
        );
    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}



