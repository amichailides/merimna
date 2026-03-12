package io.github.amichailides.merimna.validation.groups;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * Ορίζει τη σειριακή εκτέλεση των validation groups με λογική waterfall.
 *
 * <p>Η επικύρωση εκτελείται ιεραρχικά και σταματά στην πρώτη ομάδα
 * που παράγει σφάλματα (short-circuit).</p>
 *
 * <ul>
 *   <li>{@link Default}: Έλεγχος constraints χωρίς ρητό group, συνήθως για
 *   βασική παρουσία υποχρεωτικών πεδίων και nested DTO objects.</li>
 *   <li>{@link FirstOrder}: Έλεγχος πληρότητας και βασικών δομικών περιορισμών
 *   (π.χ. {@code @NotBlank}, {@code @NotNull}, {@code @Size}, {@code @Past}).</li>
 *   <li>{@link SecondOrder}: Έλεγχος σύνθετων κανόνων εγκυρότητας, patterns
 *   και custom validators (π.χ. {@code @ValidFirstName}, {@code @ValidAmka}).</li>
 * </ul>
 */
@GroupSequence({Default.class, FirstOrder.class, SecondOrder.class})
public interface ValidationGroupSequence {
}