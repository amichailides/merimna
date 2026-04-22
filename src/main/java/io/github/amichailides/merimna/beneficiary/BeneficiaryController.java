package io.github.amichailides.merimna.beneficiary;

import io.github.amichailides.merimna.beneficiary.dto.*;
import io.github.amichailides.merimna.common.openapi.ConflictErrorResponse;
import io.github.amichailides.merimna.common.openapi.NotFoundErrorResponse;
import io.github.amichailides.merimna.common.response.PageResponse;
import io.github.amichailides.merimna.common.openapi.ValidationErrorResponse;
import io.github.amichailides.merimna.validation.groups.ValidationGroupSequence;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Positive;
import jakarta.validation.constraints.Size;
import lombok.RequiredArgsConstructor;
import org.springdoc.core.annotations.ParameterObject;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.*;
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
            @ApiResponse(
                    responseCode = "201",
                    description = "Beneficiary created successfully",
                    useReturnTypeSchema = true
            ),
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
    @PreAuthorize("hasAuthority('BENEFICIARY_CREATE')")
    @PostMapping
    public ResponseEntity<BeneficiaryDetailsDTO> create(
            @Validated(ValidationGroupSequence.class) @RequestBody BeneficiaryCreateDTO dto) {

        BeneficiaryDetailsDTO beneficiary = service.create(dto);
        return ResponseEntity
                .created(buildLocationUri(beneficiary.publicId()))
                .body(beneficiary);
    }

    @Operation(summary = "Update Beneficiary")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Beneficiary updated successfully",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Beneficiary not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundErrorResponse.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    @PatchMapping("/{publicId}")
    public ResponseEntity<BeneficiaryDetailsDTO> updateBeneficiary(
            @Parameter(description = "Beneficiary unique identifier", example = "42")
            @PathVariable String publicId,
            @Validated(ValidationGroupSequence.class) @RequestBody BeneficiaryUpdateDTO dto) {

        BeneficiaryDetailsDTO beneficiary = service.updateBeneficiary(publicId, dto);
        return ResponseEntity.ok(beneficiary);
    }

    @Operation(summary = "Get beneficiary by ID")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Beneficiary found successfully",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Beneficiary not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundErrorResponse.class)
                    )
            )
    })
    // TODO(#9): Replace {id} path variable with public identifier (UUID/ULID).
    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping("/{publicId}")
    public BeneficiaryDetailsDTO getById(
            @Parameter(description = "Beneficiary unique identifier", example = "42")
            @PathVariable String publicId) {

        return service.findByPublicId(publicId);
    }

    // TODO(#5): Revisit discharge API design.
    // Current endpoint is kept as a pragmatic interim solution.
    // Refactor to PATCH /beneficiaries/{beneficiaryId} if discharge remains a state change,
    // or to POST /beneficiaries/{beneficiaryId}/discharges if it becomes a first-class business event.
    @Operation(summary = "Discharge beneficiary")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    description = "Beneficiary discharged successfully",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Beneficiary not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "409",
                    description = "Beneficiary already inactive",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ConflictErrorResponse.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('BENEFICIARY_DISCHARGE')")
    @PostMapping("/{publicId}/discharge")
    public ResponseEntity<BeneficiaryDetailsDTO> discharge
    (@Parameter(description = "Beneficiary unique identifier", example = "42")
     @PathVariable String publicId) {


        BeneficiaryDetailsDTO updated = service.discharge(publicId);
        return ResponseEntity.ok(updated);
    }

    @Operation(summary = "Get beneficiaries", description = "Returns a paginated list of beneficiaries. When amka is provided, q is ignored.")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            )
    })
    @PreAuthorize("hasAuthority('BENEFICIARY_READ')")
    @GetMapping
    public PageResponse<BeneficiaryListDTO> getBeneficiaries(
            @Valid @ModelAttribute BeneficiarySearchDTO criteria,
            @ParameterObject @PageableDefault(size = 5, sort = "lastName", direction = Sort.Direction.ASC) Pageable pageable) {

        Page<BeneficiaryListDTO> page = service.findBeneficiaries(criteria, pageable);

        return PageResponse.<BeneficiaryListDTO>builder()
                .content(page.getContent())
                .page(page.getNumber())
                .size(page.getSize())
                .totalElements(page.getTotalElements())
                .totalPages(page.getTotalPages())
                .build();
    }

    @Operation(summary = "Change beneficiary house unit")
    @ApiResponses({
            @ApiResponse(
                    responseCode = "200",
                    useReturnTypeSchema = true
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "Validation error",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = ValidationErrorResponse.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "Beneficiary or house unit not found",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = NotFoundErrorResponse.class)
                    )
            )
    })
    @PatchMapping("/{publicId}/house-unit/{code}")
    @PreAuthorize("hasAuthority('BENEFICIARY_UPDATE')")
    public ResponseEntity<BeneficiaryListDTO> changeHouseUnit(
            @Parameter(description = "Beneficiary ID", example = "1")
            @PathVariable String publicId,

            @Parameter(description = "House unit code", example = "UNIT_A")
            @PathVariable @NotBlank @Size(max = 20) String code) {

        BeneficiaryListDTO updated = service.changeHouseUnit(publicId, code);
        return ResponseEntity.ok(updated);
    }

    private URI buildLocationUri(Object id) {
        return ServletUriComponentsBuilder
                .fromCurrentRequest()
                .path("/{id}")
                .buildAndExpand(id)
                .toUri();
    }
}



