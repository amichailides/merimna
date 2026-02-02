package io.github.amichailides.merimna.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;
import java.util.List;

@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class Beneficiary {
    private Long id;
    private String firstName;
    private String lastName;
    private String amka;
    private LocalDate dateOfBirth;
    private HouseUnit houseUnit;
    private String addressInfo;

    private EmergencyContact emergencyContact;
    private List<Medication> medicalTreatment;
}
