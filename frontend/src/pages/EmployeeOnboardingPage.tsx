import { ArrowLeft } from 'lucide-react'
import { useForm } from 'react-hook-form'
import { useNavigate } from 'react-router-dom'

import { onboardEmployee } from '@/api/employeeApi'
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
import { Button } from '@/components/ui/button'

export function EmployeeOnboardingPage() {
    const navigate = useNavigate()
    const { positions, loading: positionsLoading } = usePositions()
    const { houseUnits, loading: houseUnitsLoading } = useHouseUnits()

    const form = useForm<EmployeeOnboardingFormValues>({
        defaultValues: employeeOnboardingDefaultValues,
    })

    async function onSubmit(values: EmployeeOnboardingFormValues) {
        const payload = toEmployeeOnboardingRequest(values)
        const response = await onboardEmployee(payload)

        if (response.employeePublicId) {
            navigate(`/employees/${response.employeePublicId}`)
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