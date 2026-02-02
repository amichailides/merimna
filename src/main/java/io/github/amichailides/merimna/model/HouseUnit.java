package io.github.amichailides.merimna.model;

import lombok.AllArgsConstructor;
import lombok.Getter;

@Getter
@AllArgsConstructor
public enum HouseUnit {
    UNIT_A("Στέγη Α", "Ελπίδας 10, Μαρούσι"),
    UNIT_B("Στέγη Β", "Μέγα Αλέξανδρου 2, Μαρουσι"),
    UNIT_C("Στέγη Γ", "Αγίου Μελετίου 22, Πατησια");

    private final String displayName;
    private final String address;

}
