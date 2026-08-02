import type { EmployeeOnboardingRequest } from '@/api/types'

export type EmployeeOnboardingFormValues = {
    firstName: string
    lastName: string
    dateOfBirth: string
    contactEmail: string
    mobileNumber: string
    street: string
    streetNumber: string
    city: string
    zipCode: string
    positionCode: string
    hireDate: string
    houseUnitPublicId: string
    assignmentStartDate: string
    assignmentEndDate: string
    emergencyContactName: string
    emergencyContactPhoneNumber: string
    grantSystemAccess: boolean
}

export const employeeOnboardingDefaultValues: EmployeeOnboardingFormValues = {
    firstName: '',
    lastName: '',
    dateOfBirth: '',
    contactEmail: '',
    mobileNumber: '',
    street: '',
    streetNumber: '',
    city: '',
    zipCode: '',
    positionCode: '',
    hireDate: '',
    houseUnitPublicId: '',
    assignmentStartDate: '',
    assignmentEndDate: '',
    emergencyContactName: '',
    emergencyContactPhoneNumber: '',
    grantSystemAccess: true,
}

export function toEmployeeOnboardingRequest(
    values: EmployeeOnboardingFormValues
): EmployeeOnboardingRequest {
    return {
        employee: {
            firstName: values.firstName,
            lastName: values.lastName,
            dateOfBirth: values.dateOfBirth,
            contactEmail: values.contactEmail,
            mobileNumber: values.mobileNumber,
            address: {
                street: values.street,
                streetNumber: values.streetNumber || undefined,
                city: values.city,
                zipCode: values.zipCode,
            },
            positionCode: values.positionCode,
            hireDate: values.hireDate,
            emergencyContactName: values.emergencyContactName,
            emergencyContactPhoneNumber:
                values.emergencyContactPhoneNumber,
        },
        initialAssignment: {
            houseUnitPublicId: values.houseUnitPublicId,
            startDate: values.assignmentStartDate,
            endDate: values.assignmentEndDate || undefined,
        },
        grantSystemAccess: values.grantSystemAccess,
    }
}