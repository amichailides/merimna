import type { MedicationCreateDTO } from '@/api/types'
import type { MedicationCreateFormValues } from './MedicationCreateSchema'

function normalizeRequired(value: string) {
    return value.trim()
}

function normalizeOptional(value?: string) {
    const normalized = value?.trim()

    return normalized ? normalized : undefined
}

export function toMedicationCreateRequest(
    values: MedicationCreateFormValues
): MedicationCreateDTO {
    return {
        name: normalizeRequired(values.name),
        dosage: normalizeRequired(values.dosage),
        frequency: normalizeRequired(values.frequency),
        administrationTimes: normalizeRequired(
            values.administrationTimes
        ),
        instructions: normalizeOptional(values.instructions),
        startedAt: values.startedAt,
    }
}
