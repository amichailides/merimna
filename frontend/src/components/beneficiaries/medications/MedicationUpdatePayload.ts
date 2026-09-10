import type {
    MedicationReadOnlyDTO,
    MedicationUpdateDTO,
} from '@/api/types'

import type { MedicationUpdateFormValues } from './MedicationUpdateSchema'

function normalizeRequired(value: string) {
    return value.trim()
}

function normalizeOptional(value?: string | null) {
    const normalized = value?.trim()

    return normalized ? normalized : undefined
}

export function buildMedicationUpdatePayload(
    values: MedicationUpdateFormValues,
    medication: MedicationReadOnlyDTO
): MedicationUpdateDTO {
    const payload: MedicationUpdateDTO = {}

    const name = normalizeRequired(values.name)
    const dosage = normalizeRequired(values.dosage)
    const frequency = normalizeRequired(values.frequency)
    const administrationTimes =
        normalizeRequired(values.administrationTimes)

    const instructions = normalizeOptional(values.instructions)
    const currentInstructions =
        normalizeOptional(medication.instructions)

    if (name !== medication.name) {
        payload.name = name
    }

    if (dosage !== medication.dosage) {
        payload.dosage = dosage
    }

    if (frequency !== medication.frequency) {
        payload.frequency = frequency
    }

    if (administrationTimes !== medication.administrationTimes) {
        payload.administrationTimes = administrationTimes
    }

    // Clearing existing instructions is not supported yet (see #42).
    if (
        instructions !== currentInstructions &&
        instructions !== undefined
    ) {
        payload.instructions = instructions
    }

    if (values.startedAt !== medication.startedAt) {
        payload.startedAt = values.startedAt
    }

    return payload
}
