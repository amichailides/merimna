// Mirrors io.github.amichailides.merimna.validation.ValidationPatterns (backend).
// This is the single source of truth for frontend regex patterns; keep it
// updated whenever ValidationPatterns.java changes. See issue #38.

export const EMAIL =
    /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/

export const MOBILE =
    /^(\+?\d{1,4})?\d{7,15}$/

export const PHONE =
    /^\+?[0-9\s-]{10,15}$/

export const AMKA =
    /^\d+$/

export const GREEK_LATIN_TEXT =
    /^[A-Za-zΑ-Ωα-ωΆ-ώ\s-]+$/

export const GREEK_LATIN_EXTENDED =
    /^[A-Za-zΑ-Ωα-ωΆ-ώ0-9\s\-._,()!\:/&]+$/

export const STREET_NUMBER =
    /^[0-9]+[A-Za-zΑ-Ωα-ωΆ-ώ\s/-]*$/

export const POSTAL_CODE =
    /^[A-Za-z0-9\s-]{3,10}$/

export const ISO_DATE =
    /^\d{4}-\d{2}-\d{2}$/