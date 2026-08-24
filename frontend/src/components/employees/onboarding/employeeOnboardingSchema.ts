import { z } from 'zod'

// ---------------------------------------------------------------------------
// Mirrors backend ValidationPatterns.
// Manually maintained for frontend UX; see issue #38.
// Revisit if validation drift becomes a problem or OpenAPI/Zod codegen adopted.
// ---------------------------------------------------------------------------
const GREEK_LATIN_TEXT = /^[A-Za-zΑ-Ωα-ωΆ-ώ\s-]+$/
const GREEK_LATIN_EXTENDED = /^[A-Za-zΑ-Ωα-ωΆ-ώ0-9\s\-._,()!\:/&]+$/
const POSTAL_CODE = /^[A-Za-z0-9\s-]{3,10}$/
const STREET_NUMBER = /^[0-9]+[A-Za-zΑ-Ωα-ωΆ-ώ\s/-]*$/
const MOBILE = /^(\+?\d{1,4})?\d{7,15}$/
const EMAIL = /^[A-Za-z0-9._%+-]+@[A-Za-z0-9.-]+\.[A-Za-z]{2,}$/
const ISO_DATE = /^\d{4}-\d{2}-\d{2}$/

function todayAsLocalDate(): string {
    const now = new Date()
    const year = now.getFullYear()
    const month = String(now.getMonth() + 1).padStart(2, '0')
    const day = String(now.getDate()).padStart(2, '0')
    return `${year}-${month}-${day}`
}

// ---------------------------------------------------------------------------
// Reusable field builders — avoid re-writing the same refine chains per field.
// ---------------------------------------------------------------------------

const requiredText = (message: string) =>
    z.string().refine((value) => value.trim().length > 0, { message })

/** Mirrors @ValidFirstName / @ValidLastName (GREEK_LATIN_TEXT + @Size). */
const greekLatinText = (min: number, max: number, label: string) =>
    requiredText(`${label} is required`)
        .refine((v) => v.length >= min && v.length <= max, `${label} must be between ${min} and ${max} characters`)
        .refine((v) => GREEK_LATIN_TEXT.test(v), `${label} contains invalid characters`)

/** Mirrors @ValidMobile (ValidationPatterns.MOBILE). */
const mobile = (label: string) =>
    requiredText(`${label} is required`).refine(
        (v) => MOBILE.test(v.trim()),
        `Invalid ${label.toLowerCase()}`
    )

/** Mirrors @ValidEmail (ValidationPatterns.EMAIL). */
const email = (label: string) =>
    requiredText(`${label} is required`).refine(
        (v) => EMAIL.test(v.trim()),
        `Invalid ${label.toLowerCase()}`
    )

/** Shape-checked ISO date, required. Mirrors @NotNull on a LocalDate field. */
const requiredIsoDate = (label: string) =>
    requiredText(`${label} is required`).refine(
        (v) => ISO_DATE.test(v),
        `Invalid ${label.toLowerCase()} format`
    )

export const employeeOnboardingSchema = z
    .object({
        employee: z.object({
            // Mirrors EmployeeCreateDTO.firstName (@ValidFirstName)
            firstName: greekLatinText(2, 20, 'First name'),

            // Mirrors EmployeeCreateDTO.lastName (@ValidLastName)
            lastName: greekLatinText(2, 20, 'Last name'),

            // Mirrors EmployeeCreateDTO.dateOfBirth (@NotNull + @Past)
            dateOfBirth: requiredIsoDate('Date of birth').refine(
                (v) => v < todayAsLocalDate(),
                'Date of birth must be in the past'
            ),

            // Mirrors EmployeeCreateDTO.contactEmail (@ValidEmail)
            contactEmail: email('Email'),

            // Mirrors EmployeeCreateDTO.mobileNumber (@ValidMobile)
            mobileNumber: mobile('Mobile number'),

            address: z.object({
                // Mirrors AddressDTO.street (@ValidGreekLatinText extended=true, 2-100)
                street: requiredText('Street is required')
                    .refine((v) => v.length >= 2 && v.length <= 100, 'Street must be between 2 and 100 characters')
                    .refine((v) => GREEK_LATIN_EXTENDED.test(v), 'Street contains invalid characters'),

                // Mirrors AddressDTO.streetNumber (optional, STREET_NUMBER pattern if present)
                streetNumber: z
                    .string()
                    .refine(
                        (v) => v === '' || (v.trim().length > 0 && STREET_NUMBER.test(v)),
                        'Invalid street number'
                    ),

                // Mirrors AddressDTO.city (@ValidGreekLatinText, 2-100)
                city: greekLatinText(2, 100, 'City'),

                // Mirrors AddressDTO.zipCode (ValidationPatterns.POSTAL_CODE)
                zipCode: requiredText('Postal code is required').refine(
                    (v) => POSTAL_CODE.test(v),
                    'Invalid postal code'
                ),
            }),

            // Mirrors EmployeeCreateDTO.positionCode (@NotBlank only — no format check backend-side)
            positionCode: requiredText('Position is required'),

            // Mirrors EmployeeCreateDTO.hireDate (@NotNull + @PastOrPresent)
            hireDate: requiredIsoDate('Hire date').refine(
                (v) => v <= todayAsLocalDate(),
                'Hire date cannot be in the future'
            ),

            // Mirrors EmployeeCreateDTO.emergencyContactName (@NotBlank only)
            emergencyContactName: requiredText('Emergency contact name is required'),

            // Mirrors EmployeeCreateDTO.emergencyContactPhoneNumber (@ValidMobile)
            emergencyContactPhoneNumber: mobile('Emergency contact phone'),
        }),

        initialAssignment: z.object({
            // Mirrors EmployeeAssignmentCreateDTO.houseUnitPublicId (@NotNull)
            houseUnitPublicId: requiredText('House unit is required'),

            // Mirrors EmployeeAssignmentCreateDTO.startDate (@NotNull + @FutureOrPresent)
            startDate: requiredIsoDate('Assignment start date').refine(
                (v) => v >= todayAsLocalDate(),
                'Assignment start date cannot be in the past'
            ),

            // Mirrors EmployeeAssignmentCreateDTO.endDate (@FutureOrPresent, optional)
            endDate: z
                .string()
                .refine((v) => v === '' || ISO_DATE.test(v), 'Invalid end date format')
                .refine((v) => v === '' || v >= todayAsLocalDate(), 'Assignment end date cannot be in the past'),
        }),

        grantSystemAccess: z.boolean(),

        systemAccess: z.object({
            // Mirrors SystemAccessRequest.accountEmail — only required/validated conditionally below
            accountEmail: z.string(),
        }),
    })
    // Mirrors @ValidAssignmentDateRange (endDate >= startDate)
    .superRefine((data, ctx) => {
        const { startDate, endDate } = data.initialAssignment
        if (startDate && endDate && endDate < startDate) {
            ctx.addIssue({
                code: 'custom',
                path: ['initialAssignment', 'endDate'],
                message: 'Assignment end date cannot be before start date',
            })
        }

        // Mirrors SystemAccessRequest.accountEmail, applied only when grantSystemAccess = true
        if (data.grantSystemAccess) {
            const accountEmail = data.systemAccess.accountEmail.trim()

            if (!accountEmail) {
                ctx.addIssue({
                    code: 'custom',
                    path: ['systemAccess', 'accountEmail'],
                    message: 'Account email is required',
                })
                return
            }

            if (!EMAIL.test(accountEmail)) {
                ctx.addIssue({
                    code: 'custom',
                    path: ['systemAccess', 'accountEmail'],
                    message: 'Invalid account email',
                })
            }
        }
    })

export type EmployeeOnboardingFormValues = z.infer<typeof employeeOnboardingSchema>