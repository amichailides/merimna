import { z } from 'zod'

import {
    greekLatinText,
    isValidIsoDate,
    optionalGreekLatinText,
    requiredText,
    todayAsLocalDate,
} from '@/lib/validation/fields'

export const medicationCreateSchema = z.object({
    name: greekLatinText('Name', {
        min: 1,
    }),

    dosage: greekLatinText('Dosage', {
        min: 1,
        extended: true,
    }),

    frequency: greekLatinText('Frequency', {
        min: 1,
        extended: true,
    }),

    administrationTimes: greekLatinText('Administration times', {
        min: 1,
        extended: true,
    }),

    instructions: optionalGreekLatinText('Instructions', {
        max: 500,
        extended: true,
    }),

    startedAt: requiredText('Start date is required')
        .refine(
            isValidIsoDate,
            'Invalid start date'
        )
        .refine(
            (value) => value <= todayAsLocalDate(),
            'Start date cannot be in the future'
        ),
})

export type MedicationCreateFormValues =
    z.infer<typeof medicationCreateSchema>