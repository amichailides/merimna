package io.github.amichailides.merimna.model;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalTime;
import java.util.List;

@Getter @Setter
@AllArgsConstructor
@NoArgsConstructor
public class Medication {
    private String Name;
    private String dosage;
    private String frequency;
    private List<LocalTime> administrationTimes;
    private String instructions;
}
