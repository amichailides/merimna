package io.github.amichailides.merimna.validation;

public final class ValidationPatterns {
    // Greek & Latin letters, spaces, hyphens
    public static final String GREEK_LATIN_TEXT = "^[A-Za-zΑ-Ωα-ωΆ-ώ\\s-]+$";

    // Γράμματα, αριθμοί και στίξη (για οδηγίες, δόσεις, ml)
    public static final String GREEK_LATIN_EXTENDED = "^[A-Za-zΑ-Ωα-ωΆ-ώ0-9\\s\\-.,()!\\:]+$";

    // Greek zip code (5 digits)
    public static final String POSTAL_CODE = "^[A-Z0-9\\s-]{3,10}$";

    // Street number (digits + optional letters)
    public static final String STREET_NUMBER = "^[0-9]+[A-Za-zΑ-Ωα-ωΆ-ώ\\s/-]*$";

    // Τηλέφωνα (Δέχεται + στην αρχή, κενά, παύλες και 10-15 ψηφία)
    public static final String PHONE = "^\\+?[0-9\\s-]{10,15}$";
    // Επιτρέπει προαιρετικά το '+' στην αρχή και μετά μόνο ψηφία (από 7 έως 15)
    public static final String MOBILE = "^(\\+?\\d{1,4})?\\d{7,15}$";

    // ΑΜΚΑ (11 ψηφία)
    public static final String AMKA = "^\\d+$";

    // (Κεφαλαία λατινικά γράμματα, αριθμοί, παύλα ή κάτω παύλα, 2–20 χαρακτήρες, χωρίς κενά)
    public static final String HOUSE_UNIT_CODE = "^[A-Za-z0-9_-]{2,20}$";
    private ValidationPatterns() {
        throw new AssertionError("Cannot instantiate constants class");
    }
}