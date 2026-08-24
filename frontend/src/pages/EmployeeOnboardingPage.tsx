import axios from 'axios'
import { ArrowLeft } from 'lucide-react'
import { useState } from 'react'
import { type Path, useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'
import { zodResolver } from '@hookform/resolvers/zod'

import { onboardEmployee } from '@/api/employeeApi'
import type { ValidationErrorResponse } from '@/api/types'
import { useHouseUnits } from '@/api/useHouseUnits'
import { usePositions } from '@/api/usePositions'
import { ContactAddressSection } from '@/components/employees/onboarding/ContactAddressSection'
import { EmploymentAssignmentSection } from '@/components/employees/onboarding/EmploymentAssignmentSection'
import {
    employeeOnboardingDefaultValues,
    toEmployeeOnboardingRequest,
    type EmployeeOnboardingFormValues,
} from '@/components/employees/onboarding/employeeOnboardingForm'
import { PersonalDetailsSection } from '@/components/employees/onboarding/PersonalDetailsSection'
import { SystemAccessSection } from '@/components/employees/onboarding/SystemAccessSection'
import { employeeOnboardingSchema } from '@/components/employees/onboarding/employeeOnboardingSchema'
import { Button } from '@/components/ui/button'

function isOnboardingFormPath(
    path: string
): path is Path<EmployeeOnboardingFormValues> {
    const segments = path.split('.')
    let current: unknown = employeeOnboardingDefaultValues

    for (const segment of segments) {
        if (
            typeof current !== 'object' ||
            current === null ||
            !(segment in current)
        ) {
            return false
        }

        current = (current as Record<string, unknown>)[segment]
    }

    return true
}

export function EmployeeOnboardingPage() {
    const navigate = useNavigate()
    const { positions, loading: positionsLoading } = usePositions()
    const { houseUnits, loading: houseUnitsLoading } = useHouseUnits()
    const [submitError, setSubmitError] = useState<string | null>(null)

    // TODO(#38): Add live validation based on backend/OpenAPI constraints.
    const form = useForm<EmployeeOnboardingFormValues>({
        resolver: zodResolver(employeeOnboardingSchema),
        defaultValues: employeeOnboardingDefaultValues,
    })

    async function onSubmit(values: EmployeeOnboardingFormValues) {
        form.clearErrors()
        setSubmitError(null)

        try {
            const payload = toEmployeeOnboardingRequest(values)
            const response = await onboardEmployee(payload)

            if (response.employeePublicId) {
                navigate(`/employees/${response.employeePublicId}`)
            }
        } catch (error) {
            if (axios.isAxiosError<ValidationErrorResponse>(error)) {
                const errorResponse = error.response?.data
                const validationErrors = errorResponse?.validationErrors

                if (validationErrors) {
                    let fieldErrorApplied = false
                    let unknownFieldError = false

                    for (const [path, messages] of Object.entries(
                        validationErrors
                    )) {
                        const message = messages[0]

                        if (!message) {
                            continue
                        }

                        if (!isOnboardingFormPath(path)) {
                            unknownFieldError = true
                            continue
                        }

                        form.setError(path, {
                            type: 'server',
                            message,
                        })

                        fieldErrorApplied = true
                    }

                    if (fieldErrorApplied && !unknownFieldError) {
                        return
                    }
                }

                setSubmitError(
                    errorResponse?.detail ??
                    'Could not create the employee. Please review the form and try again.'
                )

                return
            }

            setSubmitError(
                'Could not connect to the server. Please try again.'
            )
        }
    }

    return (
        <main className="max-w-3xl">
            <div>
                <button
                    type="button"
                    onClick={() => navigate('/employees')}
                    className="mb-4 inline-flex items-center gap-1.5 text-[12px] text-slate-500 transition-colors hover:text-slate-800"
                >
                    <ArrowLeft className="h-3.5 w-3.5" />
                    Back to employees
                </button>

                <h1 className="text-[18px] font-medium text-slate-900">
                    Add employee
                </h1>

                <p className="mt-0.5 text-[13px] text-slate-400">
                    Create an employee, initial assignment and system invitation.
                </p>
            </div>

            <form
                onSubmit={form.handleSubmit(onSubmit)}
                noValidate
                className="mt-8 space-y-8"
            >
                <PersonalDetailsSection control={form.control} />

                <ContactAddressSection control={form.control} />

                <EmploymentAssignmentSection
                    control={form.control}
                    positions={positions}
                    positionsLoading={positionsLoading}
                    houseUnits={houseUnits}
                    houseUnitsLoading={houseUnitsLoading}
                />

                <SystemAccessSection control={form.control} />

                {submitError && (
                    <div
                        role="alert"
                        className="rounded-lg border border-red-200 bg-red-50 px-4 py-3 text-[13px] text-red-700"
                    >
                        {submitError}
                    </div>
                )}

                <div className="flex items-center justify-end border-t border-slate-100 pt-5">
                    <Button
                        type="submit"
                        disabled={form.formState.isSubmitting}
                        className="bg-teal-700 text-white hover:bg-teal-800"
                    >
                        {form.formState.isSubmitting
                            ? 'Creating employee...'
                            : 'Create employee'}
                    </Button>
                </div>
            </form>
        </main>
    )
}