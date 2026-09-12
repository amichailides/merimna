package io.github.amichailides.merimna.validation.groups;

import jakarta.validation.GroupSequence;
import jakarta.validation.groups.Default;

/**
 * Defines the sequential execution order of validation groups using a waterfall strategy.
 *
 * <p>Validation is executed group by group and stops at the first group that
 * produces constraint violations.</p>
 *
 * <ul>
 *   <li>{@link Default}: The standard Bean Validation group. In Merimna, this
 *   group is not intentionally used for application DTO validation rules; it is
 *   kept first as a fallback for constraints without an explicit group.</li>
 *
 *   <li>{@link FirstOrder}: Validates required input and basic structural guards,
 *   such as missing nested objects, {@code @NotNull}, {@code @NotBlank},
 *   {@code @OptionalNotBlank}, and class-level presence rules like
 *   {@code @AtLeastOnePhonePresent}.</li>
 *
 *   <li>{@link SecondOrder}: Validates value correctness after the basic input
 *   guards have passed, including patterns and custom validators such as
 *   {@code @ValidFirstName}, {@code @ValidEmail}, {@code @ValidAmka},
 *   {@code @ValidAfm}, and {@code @ValidGreekLatinText}.</li>
 * </ul>
 */
@GroupSequence({Default.class, FirstOrder.class, SecondOrder.class})
public interface ValidationGroupSequence {
}