package io.github.amichailides.merimna.validation;

import java.time.LocalDate;

public interface SearchDateRange {
    LocalDate from();
    LocalDate to();
}