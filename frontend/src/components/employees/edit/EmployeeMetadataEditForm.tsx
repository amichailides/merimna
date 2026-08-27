import axios from 'axios'
import { useState } from 'react'
import { type Path, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'

import { updateEmployee } from '@/api/employeeApi'
import type {
    EmployeeDetailsDTO,
    ValidationErrorResponse,
} from '@/api/types'
import {
    FloatingPanelBody,
    FloatingPanelFooter,
    FloatingPanelHeader,
    useFloatingPanel,
} from '@/components/ui/floating-panel'
import { applyServerValidationErrors } from '@/lib/applyServerValidationErrors'
import { EmployeeMetadataContactSection } from './EmployeeMetadataContactSection'
import { EmployeeMetadataEmergencyContactSection } from './EmployeeMetadataEmergencyContactSection'
import { EmployeeMetadataEmploymentDatesSection } from './EmployeeMetadataEmploymentDatesSection'
import {
    employeeMetadataEditSchema,
    type EmployeeMetadataEditFormValues,
} from './EmployeeMetadataEditSchema'
import { buildEmployeeMetadataUpdatePayload } from './employeeMetadataPayload'

type Props = {
    employee: EmployeeDetailsDTO
    onEmployeeUpdated?: () => void | Promise<void>
}

const employeeMetadataFormPaths = new Set<
    Path<EmployeeMetadataEditFormValues>
>([
    'contactEmail',
    'mobileNumber',
    'address.street',
    'address.streetNumber',
    'address.city',
    'address.zipCode',
    'emergencyContactName',
    'emergencyContactPhoneNumber',
    'hireDate',
    'dateOfBirth',
])

function isEmployeeMetadataFormPath(
    path: string
): path is Path<EmployeeMetadataEditFormValues> {
    return employeeMetadataFormPaths.has(
        path as Path<EmployeeMetadataEditFormValues>
    )
}

export function EmployeeMetadataEditForm({
    employee,
    onEmployeeUpdated,
}: Props) {
    const { closeFloatingPanel } = useFloatingPanel()
    const [submitError, setSubmitError] = useState<string | null>(null)

    const form = useForm<EmployeeMetadataEditFormValues>({
        resolver: zodResolver(employeeMetadataEditSchema),
        defaultValues: {
            contactEmail: employee.contactEmail ?? '',
            mobileNumber: employee.mobileNumber ?? '',
            address: {
                street: employee.address?.street ?? '',
                streetNumber: employee.address?.streetNumber ?? '',
                city: employee.address?.city ?? '',
                zipCode: employee.address?.zipCode ?? '',
            },
            emergencyContactName:
                employee.emergencyContactName ?? '',
            emergencyContactPhoneNumber:
                employee.emergencyContactPhoneNumber ?? '',
            hireDate: employee.hireDate ?? '',
            dateOfBirth: employee.dateOfBirth ?? '',
        },
    })

    const { errors, isDirty, isSubmitting } = form.formState

    async function handleSubmit(
        values: EmployeeMetadataEditFormValues
    ) {
        form.clearErrors()
        setSubmitError(null)

        const payload = buildEmployeeMetadataUpdatePayload(
            values,
            employee
        )

        if (Object.keys(payload).length === 0) {
            closeFloatingPanel()
            return
        }

        try {
            await updateEmployee(employee.publicId, payload)
            await onEmployeeUpdated?.()
            closeFloatingPanel()
        } catch (error) {
            if (axios.isAxiosError<ValidationErrorResponse>(error)) {
                const errorResponse = error.response?.data
                const validationErrors =
                    errorResponse?.validationErrors

                if (validationErrors) {
                    const {
                        fieldErrorApplied,
                        unknownFieldError,
                    } = applyServerValidationErrors({
                        form,
                        validationErrors,
                        isFormPath: isEmployeeMetadataFormPath,
                    })

                    if (
                        fieldErrorApplied &&
                        !unknownFieldError
                    ) {
                        return
                    }
                }

                setSubmitError(
                    errorResponse?.detail ??
                    'Could not update the employee. Please review the form and try again.'
                )

                return
            }

            setSubmitError(
                'Could not connect to the server. Please try again.'
            )
        }
    }

    return (
        <form
            className="min-h-[520px]"
            onSubmit={form.handleSubmit(handleSubmit)}
            noValidate
        >
            <FloatingPanelHeader className="border-b border-slate-100 px-4 py-3">
                <div>
                    <div className="text-[13px] font-medium text-slate-800">
                        Edit employee details
                    </div>

                    <p className="mt-0.5 text-[12px] font-normal text-slate-400">
                        Update this employee’s profile information.
                    </p>
                </div>
            </FloatingPanelHeader>

            <FloatingPanelBody className="space-y-5 px-4 py-4">
                <EmployeeMetadataContactSection
                    register={form.register}
                    errors={errors}
                />

                <EmployeeMetadataEmergencyContactSection
                    register={form.register}
                    errors={errors}
                />

                <EmployeeMetadataEmploymentDatesSection
                    register={form.register}
                    errors={errors}
                />

                {submitError && (
                    <div
                        role="alert"
                        className="rounded-lg border border-red-200 bg-red-50 px-3 py-2 text-[12px] text-red-700"
                    >
                        {submitError}
                    </div>
                )}
            </FloatingPanelBody>

            <FloatingPanelFooter className="mt-auto justify-end gap-2 border-t border-slate-100 px-4 py-3">
                <button
                    type="button"
                    onClick={closeFloatingPanel}
                    className="rounded-lg px-3 py-1.5 text-[13px] font-medium text-slate-500 hover:text-slate-800"
                >
                    Cancel
                </button>

                <button
                    type="submit"
                    disabled={isSubmitting || !isDirty}
                    className="rounded-lg bg-teal-600 px-3 py-1.5 text-[13px] font-medium text-white hover:bg-teal-700 disabled:cursor-not-allowed disabled:opacity-60"
                >
                    {isSubmitting
                        ? 'Saving...'
                        : 'Save changes'}
                </button>
            </FloatingPanelFooter>
        </form>
    )
}
