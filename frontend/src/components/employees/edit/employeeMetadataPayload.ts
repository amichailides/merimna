import type {
    EmployeeDetailsDTO,
    EmployeeUpdateDTO,
} from '@/api/types'
import type { EmployeeMetadataEditFormValues } from './EmployeeMetadataEditSchema'

type AddressPatch = NonNullable<EmployeeUpdateDTO['address']>

function buildAddressPatch(
    values: EmployeeMetadataEditFormValues['address'],
    employee: EmployeeDetailsDTO
): AddressPatch | undefined {
    const patch: AddressPatch = {}

    if (values.street !== (employee.address?.street ?? '')) {
        patch.street = values.street
    }

    const existingStreetNumber =
        employee.address?.streetNumber ?? ''

    if (
        values.streetNumber !== existingStreetNumber &&
        values.streetNumber !== ''
    ) {
        patch.streetNumber = values.streetNumber
    }

    if (values.city !== (employee.address?.city ?? '')) {
        patch.city = values.city
    }

    if (values.zipCode !== (employee.address?.zipCode ?? '')) {
        patch.zipCode = values.zipCode
    }

    return Object.keys(patch).length > 0
        ? patch
        : undefined
}

export function buildEmployeeMetadataUpdatePayload(
    values: EmployeeMetadataEditFormValues,
    employee: EmployeeDetailsDTO
): EmployeeUpdateDTO {
    const payload: EmployeeUpdateDTO = {}

    if (values.contactEmail !== employee.contactEmail) {
        payload.contactEmail = values.contactEmail
    }

    if (values.mobileNumber !== employee.mobileNumber) {
        payload.mobileNumber = values.mobileNumber
    }

    const addressPatch = buildAddressPatch(
        values.address,
        employee
    )

    if (addressPatch) {
        payload.address = addressPatch
    }

    if (
        values.emergencyContactName !==
        employee.emergencyContactName
    ) {
        payload.emergencyContactName =
            values.emergencyContactName
    }

    if (
        values.emergencyContactPhoneNumber !==
        employee.emergencyContactPhoneNumber
    ) {
        payload.emergencyContactPhoneNumber =
            values.emergencyContactPhoneNumber
    }

    if (values.hireDate !== employee.hireDate) {
        payload.hireDate = values.hireDate
    }

    if (values.dateOfBirth !== employee.dateOfBirth) {
        payload.dateOfBirth = values.dateOfBirth
    }

    return payload
}