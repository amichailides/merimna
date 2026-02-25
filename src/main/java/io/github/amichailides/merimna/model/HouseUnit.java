package io.github.amichailides.merimna.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HouseUnit {
    UNIT_A("Στέγη Α", "Ελπίδας 10, Μαρούσι"),
    UNIT_B("Στέγη Β", "Μέγα Αλέξανδρου 2, Μαρούσι"),
    UNIT_C("Στέγη Γ", "Αγίου Μελετίου 22, Πατήσια");

    private final String displayName;
    private final String address;

}
