package io.github.amichailides.merimna.common;

import io.swagger.v3.oas.annotations.media.Schema;

import java.util.List;
import java.util.Map;

@Schema(name = "ValidationErrorResponse")
public class ValidationErrorResponse {

    @Schema(example = "VALIDATION_FAILED")
    public String type;

    @Schema(example = "Bad Request")
    public String title;

    @Schema(example = "400")
    public int status;

    @Schema(example = "Παρουσιάστηκαν σφάλματα κατά την επικύρωση των στοιχείων.")
    public String detail;

    @Schema(
            description = "Field validation errors",
            example = "{\"firstName\":[\"Το όνομα μπορεί να περιέχει μόνο ελληνικούς ή λατινικούς χαρακτήρες.\",\"Το όνομα πρέπει να αποτελείται από 3 έως 20 χαρακτήρες.\"]}"
    )
    public Map<String, List<String>> validationErrors;

    @Schema(example = "/api/beneficiaries")
    public String path;

    @Schema(example = "2026-03-20T11:33:15")
    public String timestamp;
}