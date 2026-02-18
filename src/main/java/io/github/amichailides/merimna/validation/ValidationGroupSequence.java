package io.github.amichailides.merimna.validation;

import jakarta.validation.GroupSequence;
import io.github.amichailides.merimna.dto.BeneficiarySaveDTO;

// Αυτό το interface ορίζει τη ΣΕΙΡΑ: 1. FirstOrder, 2. SecondOrder, 3. Τα υπόλοιπα
@GroupSequence({FirstOrder.class, SecondOrder.class})
public interface ValidationGroupSequence {
}