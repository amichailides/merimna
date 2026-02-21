package io.github.amichailides.merimna.validation.groups;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * Καθορίζει τη σειρά με την οποία εκτελούνται τα validation groups.
 * Η σειρά είναι:
 * 1. Default (Βασικοί έλεγχοι & Custom Validators χωρίς group)
 * 2. FirstOrder (Πρώτο επίπεδο λογικής)
 * 3. SecondOrder (Πιο σύνθετοι έλεγχοι)
 */
@GroupSequence({Default.class, FirstOrder.class, SecondOrder.class})
public interface ValidationGroupSequence {
}