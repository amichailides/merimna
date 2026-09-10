import { z } from 'zod'

import { medicationCreateSchema } from './MedicationCreateSchema'

// Form validation is identical to create: both forms require complete,
// valid values for all domain-required fields.
//
// PATCH optionality belongs to the payload layer, where unchanged fields
// are omitted from MedicationUpdateDTO.
export const medicationUpdateSchema = medicationCreateSchema

export type MedicationUpdateFormValues =
    z.infer<typeof medicationUpdateSchema>
