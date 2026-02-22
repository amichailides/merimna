package io.github.amichailides.merimna.validation.groups;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * Καθορίζει την ιεραρχική σειρά επικύρωσης (Waterfall validation).
 * <p>Η σειρά εκτέλεσης είναι:</p>
 * <ul>
 * <li>{@link Default}: Έλεγχος ύπαρξης και δομής (π.χ. {@code @NotNull}, {@code @NotBlank})</li>
 * <li>{@link FirstOrder}: Μορφολογικοί έλεγχοι και περιορισμοί μεγέθους ({@code @Size})</li>
 * <li>{@link SecondOrder}: Επιχειρηματική λογική, σύνθετα patterns και custom validators</li>
 * </ul>
 */
@GroupSequence({Default.class, FirstOrder.class, SecondOrder.class})
public interface ValidationGroupSequence {
}