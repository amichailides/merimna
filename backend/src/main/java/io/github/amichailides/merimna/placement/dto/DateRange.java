package io.github.amichailides.merimna.placement.dto;

import io.github.amichailides.merimna.validation.SearchDateRange;
import io.github.amichailides.merimna.validation.annotations.ValidSearchDateRange;
import io.github.amichailides.merimna.validation.groups.SecondOrder;
import lombok.Builder;

import java.time.LocalDate;

@Builder
@ValidSearchDateRange(groups = SecondOrder.class)
public record DateRange(
        LocalDate from,
        LocalDate to
) implements SearchDateRange {}