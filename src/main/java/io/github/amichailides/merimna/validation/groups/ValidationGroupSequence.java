package io.github.amichailides.merimna.validation.groups;

import jakarta.validation.GroupSequence;

// Αυτό το interface ορίζει τη ΣΕΙΡΑ: 1. FirstOrder, 2. SecondOrder, 3. Τα υπόλοιπα
@GroupSequence({FirstOrder.class, SecondOrder.class})
public interface ValidationGroupSequence {
}