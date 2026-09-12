package io.github.amichailides.merimna.validation;

public final class TextNormalizer {

    private static final String WHITESPACE_SEQUENCE = "\\s+";

    private TextNormalizer() {
    }

    public static String normalize(String value) {
        if (value == null) {
            return null;
        }

        return value.trim().replaceAll(WHITESPACE_SEQUENCE, " ");
    }
}