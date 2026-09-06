import { z } from 'zod'

import {
    AMKA,
    EMAIL,
    GREEK_LATIN_EXTENDED,
    GREEK_LATIN_TEXT,
    ISO_DATE,
    MOBILE,
    PHONE,
    POSTAL_CODE,
    STREET_NUMBER,
} from './patterns'

// Shared Zod field builders mirroring backend Bean Validation rules.
// Manually maintained for frontend UX; backend remains the source of truth.
//
// These validators answer only:
// "Is this form value acceptable?"
//
// They do not normalize submitted values (trimming, '' -> undefined, etc.).
// Payload normalization belongs to the payload-builder layer, which is
// especially important for PATCH semantics where omitted, blank and explicit
// clearing may have different meanings.
//
// See issue #38.

export function todayAsLocalDate(): string {
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const day = String(now.getDate()).padStart(2, '0')

    return `${year}-${month}-${day}`
}

function daysInMonth(year: number, month: number): number {
    return new Date(year, month, 0).getDate()
}

function minAllowedDateOfBirth(): string {
    const now = new Date()

    const year = now.getFullYear() - 100
    const month = now.getMonth() + 1
    const day = Math.min(
        now.getDate(),
        daysInMonth(year, month)
    )

    return [
        year,
        String(month).padStart(2, '0'),
        String(day).padStart(2, '0'),
    ].join('-')
}

function isValidIsoDate(value: string): boolean {
    if (!ISO_DATE.test(value)) {
        return false
    }

    const [year, month, day] = value
        .split('-')
        .map(Number)

    const date = new Date(year, month - 1, day)

    return (
        date.getFullYear() === year &&
        date.getMonth() === month - 1 &&
        date.getDate() === day
    )
}

export const requiredText = (message: string) =>
    z.string().refine(
        (value) => value.trim().length > 0,
        { message }
    )

// Mirrors ValidationPatterns.EMAIL — used by @ValidEmail.
export const email = requiredText('Email is required').refine(
    (value) => EMAIL.test(value.trim()),
    'Enter a valid email address'
)

// Form-friendly optional variant for fields with @ValidEmail but no
// @NotBlank/@NotNull.
//
// Empty HTML inputs produce ''. We allow that at the form-validation layer.
// The payload builder is responsible for deciding whether that means omit,
// preserve or clear the value in a PATCH request.
export const optionalEmail = (label = 'Email') =>
    z
        .string()
        .optional()
        .refine(
            (value) =>
                value === undefined ||
                value === '' ||
                EMAIL.test(value.trim()),
            `${label} must be a valid email address`
        )

// Mirrors ValidationPatterns.MOBILE — used by @ValidMobile.
export const mobile = (label: string) =>
    requiredText(`${label} is required`).refine(
        (value) => MOBILE.test(value.trim()),
        `${label} must be a valid phone number`
    )

// Form-friendly optional variant for fields with @ValidMobile but no
// @NotBlank/@NotNull.
export const optionalMobile = (label: string) =>
    z
        .string()
        .optional()
        .refine(
            (value) =>
                value === undefined ||
                value === '' ||
                MOBILE.test(value.trim()),
            `${label} must be a valid phone number`
        )

// Form-friendly optional variant for @ValidLandline.
// @ValidLandline uses ValidationPatterns.PHONE rather than MOBILE.
export const optionalLandline = (label = 'Landline') =>
    z
        .string()
        .optional()
        .refine(
            (value) =>
                value === undefined ||
                value === '' ||
                PHONE.test(value.trim()),
            `${label} must be a valid phone number`
        )

// Mirrors ValidationPatterns.AMKA (digits only) plus @Size(min=11,max=11)
// declared by @ValidAmka.
export const amka = requiredText('AMKA is required')
    .refine(
        (value) => value.trim().length === 11,
        'AMKA must be 11 digits'
    )
    .refine(
        (value) => AMKA.test(value.trim()),
        'AMKA must contain only digits'
    )

// Mirrors @ValidGreekLatinText and the corresponding backend patterns.
export const greekLatinText = (
    label: string,
    {
        min = 2,
        max = 100,
        extended = false,
    }: {
        min?: number
        max?: number
        extended?: boolean
    } = {}
) =>
    requiredText(`${label} is required`)
        .refine(
            (value) =>
                value.length >= min &&
                value.length <= max,
            `${label} must be between ${min} and ${max} characters`
        )
        .refine(
            (value) =>
                (
                    extended
                        ? GREEK_LATIN_EXTENDED
                        : GREEK_LATIN_TEXT
                ).test(value),
            `${label} contains invalid characters`
        )

// Form representation of optional AddressDTO/AddressUpdateDTO street number.
// An empty HTML input is acceptable here; PATCH normalization is handled later.
export const streetNumber = z
    .string()
    .refine(
        (value) =>
            value === '' ||
            (
                value.trim().length > 0 &&
                STREET_NUMBER.test(value)
            ),
        'Street number is invalid'
    )

// Mirrors ValidationPatterns.POSTAL_CODE.
export const zipCode =
    requiredText('Postal code is required').refine(
        (value) => POSTAL_CODE.test(value),
        'Postal code is invalid'
    )

// Mirrors @ValidDateOfBirth:
// - valid ISO LocalDate
// - strictly before today
// - strictly after today minus 100 years
export const dateOfBirth = (label = 'Date of birth') =>
    requiredText(`${label} is required`)
        .refine(
            isValidIsoDate,
            `Invalid ${label.toLowerCase()}`
        )
        .refine(
            (value) => value < todayAsLocalDate(),
            `${label} must be in the past`
        )
        .refine(
            (value) => value > minAllowedDateOfBirth(),
            `${label} is not valid`
        )

// Required address form schema using the same field validation rules as the
// backend AddressDTO / AddressUpdateDTO.
//
// This is intentionally stricter than AddressUpdateDTO's PATCH shape because
// the beneficiary edit form is prefilled with the beneficiary's current
// complete address. The payload builder later decides which changed fields
// actually belong in the PATCH request.
export const addressSchema = z.object({
    street: greekLatinText('Street', {
        max: 100,
        extended: true,
    }),

    streetNumber,

    city: greekLatinText('City', {
        max: 100,
    }),

    zipCode,
})
