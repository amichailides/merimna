package io.github.amichailides.merimna.placement.dto;

import io.github.amichailides.merimna.validation.SearchDateRange;
import io.github.amichailides.merimna.validation.annotations.ValidSearchDateRange;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@ValidSearchDateRange
public record DateRange(
        LocalDate from,
        LocalDate to
) implements SearchDateRange {}