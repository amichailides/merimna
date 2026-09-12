package io.github.amichailides.merimna.beneficiary.dto;

import io.github.amichailides.merimna.validation.annotations.ValidGreekLatinText;
import io.github.amichailides.merimna.validation.groups.FirstOrder;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.PastOrPresent;

import java.time.LocalDate;

@Schema(description = "Request body used to discharge a beneficiary from active support.")
public record DischargeRequestDTO(

        @Schema(
                description = "Official discharge date. Must be today or a past date; future discharge scheduling is not supported in V1.",
                example = "2026-05-03",
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotNull(
                message = "{beneficiary.dischargeDate.required}",
                groups = FirstOrder.class)
        @PastOrPresent(
                message = "{beneficiary.dischargeDate.pastOrPresent}",
                groups = SecondOrder.class)
        LocalDate dischargeDate,

        @Schema(
                description = "Reason for discharging the beneficiary. Stored as free text in V1.",
                example = "Completion of supported living services.",
                maxLength = 500,
                requiredMode = Schema.RequiredMode.REQUIRED
        )
        @NotBlank(
                message = "{beneficiary.dischargeReason.required}",
                groups = FirstOrder.class)
        @ValidGreekLatinText(
                max = 500,
                extended = true,
                message = "{beneficiary.dischargeReason.invalid}",
                groups = SecondOrder.class)
        String dischargeReason
) {
}