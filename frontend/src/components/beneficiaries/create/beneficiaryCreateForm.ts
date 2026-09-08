import type { DefaultValues } from 'react-hook-form'

import type { BeneficiaryCreateDTO } from '@/api/types'
import type { BeneficiaryCreateFormValues } from './BeneficiaryCreateSchema'

export const beneficiaryCreateDefaultValues: DefaultValues<BeneficiaryCreateFormValues> =
{
    firstName: '',
    lastName: '',
    amka: '',
    dateOfBirth: '',
    houseUnitPublicId: '',

    permanentAddress: {
        street: '',
        streetNumber: '',
        city: '',
        zipCode: '',
    },

    emergencyContact: {
        firstName: '',
        lastName: '',
        relationshipType: undefined,
        landlinePhone: '',
        mobileNumber: '',
        email: '',

        address: {
            street: '',
            streetNumber: '',
            city: '',
            zipCode: '',
        },
    },
}

function normalizeRequired(value: string) {
    return value.trim()
}

function normalizeOptional(value?: string) {
    const normalized = value?.trim()

    return normalized ? normalized : undefined
}

export function toBeneficiaryCreateRequest(
    values: BeneficiaryCreateFormValues
): BeneficiaryCreateDTO {
    return {
        firstName: normalizeRequired(values.firstName),
        lastName: normalizeRequired(values.lastName),
        amka: normalizeRequired(values.amka),
        dateOfBirth: values.dateOfBirth,
        houseUnitPublicId: values.houseUnitPublicId,

        permanentAddress: {
            street: normalizeRequired(values.permanentAddress.street),
            streetNumber: normalizeOptional(
                values.permanentAddress.streetNumber
            ),
            city: normalizeRequired(values.permanentAddress.city),
            zipCode: normalizeRequired(values.permanentAddress.zipCode),
        },

        emergencyContact: {
            firstName: normalizeRequired(values.emergencyContact.firstName),
            lastName: normalizeRequired(values.emergencyContact.lastName),
            relationshipType: values.emergencyContact.relationshipType,
            landlinePhone: normalizeOptional(
                values.emergencyContact.landlinePhone
            ),
            mobileNumber: normalizeOptional(
                values.emergencyContact.mobileNumber
            ),
            email: normalizeOptional(values.emergencyContact.email),

            address: {
                street: normalizeRequired(
                    values.emergencyContact.address.street
                ),
                streetNumber: normalizeOptional(
                    values.emergencyContact.address.streetNumber
                ),
                city: normalizeRequired(
                    values.emergencyContact.address.city
                ),
                zipCode: normalizeRequired(
                    values.emergencyContact.address.zipCode
                ),
            },
        },
    }
}
