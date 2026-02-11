package io.github.amichailides.merimna.validation;

public final class ValidationPatterns {
    // Greek & Latin letters, spaces, hyphens
    public static final String GREEK_LATIN_TEXT = "^[A-Za-zΑ-Ωα-ωΆ-ώ\\s-]+$";

    // Greek zip code (5 digits)
    public static final String POSTAL_CODE = "^[A-Z0-9\\s-]{3,10}$";

    // Street number (digits + optional letters)
    public static final String STREET_NUMBER = "^[0-9]+[A-Za-zΑ-Ωα-ωΆ-ώ\\s/-]*$";

    // Τηλέφωνα (Δέχεται + στην αρχή, κενά, παύλες και 10-15 ψηφία)
    public static final String PHONE = "^\\+?[0-9\\s-]{10,15}$";
    public static final String MOBILE = "^(\\+\\d{1,3})?[0-9\\s-]{10,15}$";

    private ValidationPatterns() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}