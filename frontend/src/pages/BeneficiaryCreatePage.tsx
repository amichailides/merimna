import axios from 'axios'
import { ArrowLeft } from 'lucide-react'
import { useState } from 'react'
import { type Path, useForm } from 'react-hook-form'
import { Link, useNavigate } from 'react-router-dom'
import { zodResolver } from '@hookform/resolvers/zod'

import { createBeneficiary } from '@/api/beneficiaryApi'
import type { ValidationErrorResponse } from '@/api/types'
import { useHouseUnits } from '@/api/useHouseUnits'
import { BeneficiaryCreateAddressSection } from '@/components/beneficiaries/create/BeneficiaryCreateAddressSection'
import { BeneficiaryCreateEmergencyContactSection } from '@/components/beneficiaries/create/BeneficiaryCreateEmergencyContactSection'
import {
    beneficiaryCreateDefaultValues,
    toBeneficiaryCreateRequest,
} from '@/components/beneficiaries/create/beneficiaryCreateForm'
import { BeneficiaryCreateLivingArrangementSection } from '@/components/beneficiaries/create/BeneficiaryCreateLivingArrangementSection'
import { BeneficiaryCreatePersonalSection } from '@/components/beneficiaries/create/BeneficiaryCreatePersonalSection'
import {
    beneficiaryCreateSchema,
    type BeneficiaryCreateFormValues,
} from '@/components/beneficiaries/create/BeneficiaryCreateSchema'
import { Button } from '@/components/ui/button'
import { applyServerValidationErrors } from '@/lib/applyServerValidationErrors'

function isBeneficiaryCreateFormPath(
    path: string
): path is Path<BeneficiaryCreateFormValues> {
    const segments = path.split('.')
    let current: unknown = beneficiaryCreateDefaultValues

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

export function BeneficiaryCreatePage() {
    const navigate = useNavigate()
    const {
        houseUnits,
        loading: houseUnitsLoading,
    } = useHouseUnits()

    const [submitError, setSubmitError] = useState<string | null>(null)

    const form = useForm<BeneficiaryCreateFormValues>({
        resolver: zodResolver(beneficiaryCreateSchema),
        defaultValues: beneficiaryCreateDefaultValues,
    })

    async function onSubmit(values: BeneficiaryCreateFormValues) {
        form.clearErrors()
        setSubmitError(null)

        try {
            const payload = toBeneficiaryCreateRequest(values)
            const beneficiary = await createBeneficiary(payload)

            if (beneficiary.publicId) {
                navigate(`/beneficiaries/${beneficiary.publicId}`)
                return
            }

            setSubmitError(
                'The beneficiary was created, but its identifier was missing from the response.'
            )
        } catch (error) {
            if (axios.isAxiosError<ValidationErrorResponse>(error)) {
                const errorResponse = error.response?.data
                const validationErrors = errorResponse?.validationErrors

                if (validationErrors) {
                    const {
                        fieldErrorApplied,
                        unknownFieldError,
                    } = applyServerValidationErrors({
                        form,
                        validationErrors,
                        isFormPath: isBeneficiaryCreateFormPath,
                    })

                    if (fieldErrorApplied && !unknownFieldError) {
                        return
                    }
                }

                setSubmitError(
                    errorResponse?.detail ??
                    'Could not create the beneficiary. Please review the form and try again.'
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
                <Link
                    to="/beneficiaries"
                    className="
                        mb-4 inline-flex items-center gap-1.5
                        text-[12px] text-slate-500 transition-colors
                        hover:text-slate-800
                    "
                >
                    <ArrowLeft className="h-3.5 w-3.5" />
                    Back to beneficiaries
                </Link>

                <h1 className="text-[18px] font-medium text-slate-900">
                    Add beneficiary
                </h1>

                <p className="mt-0.5 text-[13px] text-slate-400">
                    Create a beneficiary and assign a house unit.
                </p>
            </div>

            <form
                onSubmit={form.handleSubmit(onSubmit)}
                noValidate
                className="mt-8 space-y-8"
            >
                <BeneficiaryCreatePersonalSection
                    control={form.control}
                />

                <BeneficiaryCreateAddressSection
                    control={form.control}
                />

                <BeneficiaryCreateEmergencyContactSection
                    control={form.control}
                />

                <BeneficiaryCreateLivingArrangementSection
                    control={form.control}
                    houseUnits={houseUnits}
                    houseUnitsLoading={houseUnitsLoading}
                />

                {submitError && (
                    <div
                        role="alert"
                        className="
                            rounded-lg border border-red-200 bg-red-50
                            px-4 py-3 text-[13px] text-red-700
                        "
                    >
                        {submitError}
                    </div>
                )}

                <div className="flex items-center justify-end border-t border-slate-100 pt-5">
                    <Button
                        type="submit"
                        disabled={
                            form.formState.isSubmitting ||
                            houseUnitsLoading
                        }
                        className="bg-teal-700 text-white hover:bg-teal-800"
                    >
                        {form.formState.isSubmitting
                            ? 'Creating beneficiary...'
                            : 'Create beneficiary'}
                    </Button>
                </div>
            </form>
        </main>
    )
}
