import { z } from 'zod'

// Mirrors backend validation for EmployeeUpdateDTO (ValidFirstName / ValidLastName).
// Manually maintained for frontend UX; see issue #38.
// Source of truth: ValidationPatterns.GREEK_LATIN_TEXT (backend)
const GREEK_LATIN_TEXT = /^[A-Za-zΑ-Ωα-ωΆ-ώ\s-]+$/

const requiredText = (message: string) =>
    z.string().refine((value) => value.trim().length > 0, { message })

const greekLatinName = (label: string) =>
    requiredText(`${label} is required`)
        .refine(
            (value) => value.length >= 2 && value.length <= 20,
            `${label} must be between 2 and 20 characters`
        )
        .refine(
            (value) => GREEK_LATIN_TEXT.test(value),
            `${label} contains invalid characters`
        )

export const employeeProfileHeaderEditSchema = z.object({
    firstName: greekLatinName('First name'),
    lastName: greekLatinName('Last name'),
    positionCode: requiredText('Position is required'),
})

export type EmployeeProfileHeaderEditFormValues =
    z.infer<typeof employeeProfileHeaderEditSchema>