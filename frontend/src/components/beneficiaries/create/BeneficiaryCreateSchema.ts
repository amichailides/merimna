import { z } from 'zod'

import {
    addressSchema,
    amka,
    atLeastOnePhonePresent,
    dateOfBirth,
    greekLatinText,
    optionalEmail,
    optionalLandline,
    optionalMobile,
    requiredText,
} from '@/lib/validation/fields'

const RELATIONSHIP_TYPES = [
    'PARENT',
    'SIBLING',
    'OTHER_RELATIVE',
    'FRIEND',
    'SOCIAL_WORKER',
    'OTHER',
] as const

const emergencyContactSchema = atLeastOnePhonePresent(
    z.object({
        firstName: greekLatinText('First name', { min: 2, max: 20 }),
        lastName: greekLatinText('Last name', { min: 2, max: 20 }),
        relationshipType: z.enum(RELATIONSHIP_TYPES, {
            message: 'Relationship is required',
        }),
        landlinePhone: optionalLandline('Landline'),
        mobileNumber: optionalMobile('Mobile number'),
        email: optionalEmail('Email'),
        address: addressSchema,
    })
)

export const beneficiaryCreateSchema = z.object({
    firstName: greekLatinText('First name', { min: 2, max: 20 }),
    lastName: greekLatinText('Last name', { min: 2, max: 20 }),
    amka,
    dateOfBirth: dateOfBirth(),
    houseUnitPublicId: requiredText('House unit is required'),
    permanentAddress: addressSchema,
    emergencyContact: emergencyContactSchema,
})

export type BeneficiaryCreateFormValues = z.infer<typeof beneficiaryCreateSchema>
