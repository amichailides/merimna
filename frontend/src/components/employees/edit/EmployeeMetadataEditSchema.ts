import { z } from 'zod'

// Mirrors backend validation for EmployeeUpdateDTO / AddressUpdateDTO.
// Manually maintained for frontend UX; see issue #38.
// Sources of truth (backend):
//   - ValidEmail -> ValidationPatterns.EMAIL
//   - ValidMobile -> ValidationPatterns.MOBILE
//   - ValidGreekLatinText(extended=false) -> ValidationPatterns.GREEK_LATIN_TEXT
//   - ValidGreekLatinText(extended=true) -> ValidationPatterns.GREEK_LATIN_EXTENDED
//   - AddressUpdateDTO.streetNumber -> ValidationPatterns.STREET_NUMBER
//   - AddressUpdateDTO.zipCode -> ValidationPatterns.POSTAL_CODE

const EMAIL = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/
const MOBILE = /^(\+?\d{1,4})?\d{7,15}$/
const GREEK_LATIN_TEXT = /^[A-Za-zΑ-Ωα-ωΆ-ώ\s-]+$/
const GREEK_LATIN_EXTENDED =
    /^[A-Za-zΑ-Ωα-ωΆ-ώ0-9\s\-._,()!\:/&]+$/
const STREET_NUMBER = /^[0-9]+[A-Za-zΑ-Ωα-ωΆ-ώ\s/-]*$/
const POSTAL_CODE = /^[A-Za-z0-9\s-]{3,10}$/
const ISO_DATE = /^\d{4}-\d{2}-\d{2}$/

function todayAsLocalDate(): string {
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const day = String(now.getDate()).padStart(2, '0')

    return `${year}-${month}-${day}`
}

const requiredText = (message: string) =>
    z.string().refine((value) => value.trim().length > 0, {
        message,
    })

const email = requiredText('Email is required').refine(
    (value) => EMAIL.test(value.trim()),
    'Enter a valid email address'
)

const mobile = (label: string) =>
    requiredText(`${label} is required`).refine(
        (value) => MOBILE.test(value.trim()),
        `${label} must be a valid phone number`
    )

const greekLatinText = (
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

const streetNumber = z.string().refine(
    (value) =>
        value === '' ||
        (value.trim().length > 0 &&
            STREET_NUMBER.test(value)),
    'Street number is invalid'
)

const zipCode = requiredText('Postal code is required').refine(
    (value) => POSTAL_CODE.test(value),
    'Postal code is invalid'
)

const hireDate = requiredText('Hire date is required')
    .refine(
        (value) => ISO_DATE.test(value),
        'Invalid hire date format'
    )
    .refine(
        (value) => value <= todayAsLocalDate(),
        'Hire date cannot be in the future'
    )

const dateOfBirth = requiredText('Date of birth is required')
    .refine(
        (value) => ISO_DATE.test(value),
        'Invalid date of birth format'
    )
    .refine(
        (value) => value < todayAsLocalDate(),
        'Date of birth must be in the past'
    )

const addressSchema = z.object({
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

export const employeeMetadataEditSchema = z.object({
    contactEmail: email,

    mobileNumber: mobile('Mobile number'),

    address: addressSchema,

    emergencyContactName: requiredText(
        'Emergency contact name is required'
    ),

    emergencyContactPhoneNumber: mobile(
        'Emergency contact phone number'
    ),

    hireDate,

    dateOfBirth,
})

export type EmployeeMetadataEditFormValues =
    z.infer<typeof employeeMetadataEditSchema>