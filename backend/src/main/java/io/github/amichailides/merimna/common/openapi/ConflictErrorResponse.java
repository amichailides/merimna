package io.github.amichailides.merimna.common.openapi;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "ConflictErrorResponse")
public class ConflictErrorResponse {

    @Schema(example = "CONFLICT_ERROR")
    public String type;

    @Schema(example = "Conflict")
    public String title;

    @Schema(example = "409")
    public int status;

    @Schema(example = "Resource conflict")
    public String detail;

    @Schema(example = "/api/beneficiaries")
    public String path;

    @Schema(example = "2026-03-20T11:33:15")
    public String timestamp;
}
