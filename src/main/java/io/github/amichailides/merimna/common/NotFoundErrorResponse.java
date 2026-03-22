package io.github.amichailides.merimna.common;

import io.swagger.v3.oas.annotations.media.Schema;

@Schema(name = "NotFoundErrorResponse")
public class NotFoundErrorResponse {

    @Schema(example = "ENTITY_NOT_FOUND")
    public String type;

    @Schema(example = "Not Found")
    public String title;

    @Schema(example = "404")
    public int status;

    @Schema(example = "The requested resource was not found")
    public String detail;

    @Schema(example = "/api/beneficiaries/1")
    public String path;

    @Schema(example = "2026-03-20T11:33:15")
    public String timestamp;
}
