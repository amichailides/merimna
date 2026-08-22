import type { EmployeeOnboardingRequest } from '@/api/types'

export type EmployeeOnboardingFormValues = {
    employee: {
        firstName: string
        lastName: string
        dateOfBirth: string
        contactEmail: string
        mobileNumber: string
        address: {
            street: string
            streetNumber: string
            city: string
            zipCode: string
        }
        positionCode: string
        hireDate: string
        emergencyContactName: string
        emergencyContactPhoneNumber: string
    }

    initialAssignment: {
        houseUnitPublicId: string
        startDate: string
        endDate: string
    }

    grantSystemAccess: boolean

    systemAccess: {
        accountEmail: string
    }
}

export const employeeOnboardingDefaultValues: EmployeeOnboardingFormValues = {
    employee: {
        firstName: '',
        lastName: '',
        dateOfBirth: '',
        contactEmail: '',
        mobileNumber: '',
        address: {
            street: '',
            streetNumber: '',
            city: '',
            zipCode: '',
        },
        positionCode: '',
        hireDate: '',
        emergencyContactName: '',
        emergencyContactPhoneNumber: '',
    },

    initialAssignment: {
        houseUnitPublicId: '',
        startDate: '',
        endDate: '',
    },

    grantSystemAccess: true,

    systemAccess: {
        accountEmail: '',
    },
}

export function toEmployeeOnboardingRequest(
    values: EmployeeOnboardingFormValues
): EmployeeOnboardingRequest {
    return {
        employee: {
            ...values.employee,
            address: {
                ...values.employee.address,
                streetNumber:
                    values.employee.address.streetNumber || undefined,
            },
        },
        initialAssignment: {
            ...values.initialAssignment,
            endDate: values.initialAssignment.endDate || undefined,
        },
        systemAccess: values.grantSystemAccess
            ? {
                accountEmail: values.systemAccess.accountEmail,
            }
            : undefined,
    }
}