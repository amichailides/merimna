package io.github.amichailides.merimna.dto;

import io.github.amichailides.merimna.model.HouseUnit;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Value;

import java.time.LocalDate;

@Value
@AllArgsConstructor
@Builder
public class BeneficiaryReadOnlyDTO {
    Long id;
    String firstName;
    String lastName;
    String amka;
    LocalDate dateOfBirth;
    String houseUnit;
}
