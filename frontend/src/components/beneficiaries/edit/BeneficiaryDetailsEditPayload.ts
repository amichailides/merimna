import type {
    BeneficiaryDetailsDTO,
    BeneficiaryUpdateDTO,
} from '@/api/types'

import type { BeneficiaryDetailsEditFormValues } from './BeneficiaryDetailsEditSchema'

type AddressUpdatePayload = NonNullable<
    BeneficiaryUpdateDTO['permanentAddress']
>

type EmergencyContactUpdatePayload = NonNullable<
    BeneficiaryUpdateDTO['emergencyContact']
>

function normalizeRequired(value: string): string {
    return value.trim()
}

function normalizeOptional(value?: string): string | undefined {
    const normalized = value?.trim()

    return normalized ? normalized : undefined
}

function addChangedString<
    T extends Record<string, unknown>,
    K extends keyof T,
>(
    target: T,
    key: K,
    nextValue: string,
    currentValue?: string | null
) {
    const normalizedNext = normalizeRequired(nextValue)
    const normalizedCurrent = currentValue?.trim() ?? ''

    if (normalizedNext !== normalizedCurrent) {
        target[key] = normalizedNext as T[K]
    }
}

function addChangedOptionalString<
    T extends Record<string, unknown>,
    K extends keyof T,
>(
    target: T,
    key: K,
    nextValue: string | undefined,
    currentValue?: string | null
) {
    const normalizedNext = normalizeOptional(nextValue)
    const normalizedCurrent = normalizeOptional(currentValue ?? undefined)

    // Explicit clearing is not supported yet by the backend PATCH semantics.
    // Empty form values are therefore omitted rather than sent as a clear.
    if (
        normalizedNext !== undefined &&
        normalizedNext !== normalizedCurrent
    ) {
        target[key] = normalizedNext as T[K]
    }
}

function buildAddressUpdate(
    values: BeneficiaryDetailsEditFormValues['permanentAddress'],
    current: BeneficiaryDetailsDTO['permanentAddress']
): AddressUpdatePayload | undefined {
    const payload: AddressUpdatePayload = {}

    addChangedString(
        payload,
        'street',
        values.street,
        current?.street
    )

    addChangedOptionalString(
        payload,
        'streetNumber',
        values.streetNumber,
        current?.streetNumber
    )

    addChangedString(
        payload,
        'city',
        values.city,
        current?.city
    )

    addChangedString(
        payload,
        'zipCode',
        values.zipCode,
        current?.zipCode
    )

    return Object.keys(payload).length > 0
        ? payload
        : undefined
}

function buildEmergencyContactUpdate(
    values: BeneficiaryDetailsEditFormValues['emergencyContact'],
    current: BeneficiaryDetailsDTO['emergencyContact']
): EmergencyContactUpdatePayload | undefined {
    const payload: EmergencyContactUpdatePayload = {}

    addChangedString(
        payload,
        'firstName',
        values.firstName,
        current?.firstName
    )

    addChangedString(
        payload,
        'lastName',
        values.lastName,
        current?.lastName
    )

    if (values.relationshipType !== current?.relationshipType) {
        payload.relationshipType = values.relationshipType
    }

    addChangedOptionalString(
        payload,
        'landlinePhone',
        values.landlinePhone,
        current?.landlinePhone
    )

    addChangedOptionalString(
        payload,
        'mobileNumber',
        values.mobileNumber,
        current?.mobileNumber
    )

    addChangedOptionalString(
        payload,
        'email',
        values.email,
        current?.email
    )

    const address = buildAddressUpdate(
        values.address,
        current?.address
    )

    if (address) {
        payload.address = address
    }

    return Object.keys(payload).length > 0
        ? payload
        : undefined
}

export function buildBeneficiaryUpdatePayload(
    values: BeneficiaryDetailsEditFormValues,
    beneficiary: BeneficiaryDetailsDTO
): BeneficiaryUpdateDTO {
    const payload: BeneficiaryUpdateDTO = {}

    addChangedString(
        payload,
        'firstName',
        values.firstName,
        beneficiary.firstName
    )

    addChangedString(
        payload,
        'lastName',
        values.lastName,
        beneficiary.lastName
    )

    addChangedString(
        payload,
        'amka',
        values.amka,
        beneficiary.amka
    )

    if (values.dateOfBirth !== beneficiary.dateOfBirth) {
        payload.dateOfBirth = values.dateOfBirth
    }

    const permanentAddress = buildAddressUpdate(
        values.permanentAddress,
        beneficiary.permanentAddress
    )

    if (permanentAddress) {
        payload.permanentAddress = permanentAddress
    }

    const emergencyContact = buildEmergencyContactUpdate(
        values.emergencyContact,
        beneficiary.emergencyContact
    )

    if (emergencyContact) {
        payload.emergencyContact = emergencyContact
    }

    return payload
}
