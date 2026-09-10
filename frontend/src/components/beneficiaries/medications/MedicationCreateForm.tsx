import axios from 'axios'
import { useState } from 'react'
import { type Path, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'

import { addMedication } from '@/api/medicationApi'
import type { ValidationErrorResponse } from '@/api/types'
import {
    FloatingPanelBody,
    FloatingPanelFooter,
    FloatingPanelHeader,
    useFloatingPanel,
} from '@/components/ui/floating-panel'
import { applyServerValidationErrors } from '@/lib/applyServerValidationErrors'

import { MedicationCreateDetailsSection } from './MedicationCreateDetailsSection'
import { MedicationCreateInstructionsSection } from './MedicationCreateInstructionsSection'
import { toMedicationCreateRequest } from './MedicationCreatePayload'
import {
    medicationCreateSchema,
    type MedicationCreateFormValues,
} from './MedicationCreateSchema'

type Props = {
    beneficiaryPublicId: string
    onMedicationAdded?: () => void | Promise<void>
}

const medicationCreateFormPaths =
    new Set<Path<MedicationCreateFormValues>>([
        'name',
        'dosage',
        'frequency',
        'administrationTimes',
        'instructions',
        'startedAt',
    ])

function isMedicationCreateFormPath(
    path: string
): path is Path<MedicationCreateFormValues> {
    return medicationCreateFormPaths.has(
        path as Path<MedicationCreateFormValues>
    )
}

export function MedicationCreateForm({
    beneficiaryPublicId,
    onMedicationAdded,
}: Props) {
    const { closeFloatingPanel } = useFloatingPanel()
    const [submitError, setSubmitError] = useState<string | null>(null)

    const form = useForm<MedicationCreateFormValues>({
        resolver: zodResolver(medicationCreateSchema),
        defaultValues: {
            name: '',
            dosage: '',
            frequency: '',
            administrationTimes: '',
            instructions: '',
            startedAt: '',
        },
    })

    const { errors, isSubmitting } = form.formState

    async function handleSubmit(values: MedicationCreateFormValues) {
        form.clearErrors()
        setSubmitError(null)

        const payload = toMedicationCreateRequest(values)

        try {
            await addMedication(
                beneficiaryPublicId,
                payload
            )

            await onMedicationAdded?.()
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
                        isFormPath: isMedicationCreateFormPath,
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
                    'Could not add the medication. Please review the form and try again.'
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
            onSubmit={form.handleSubmit(handleSubmit)}
            noValidate
        >
            <FloatingPanelHeader className="border-b border-slate-100 px-4 py-3">
                <div>
                    <div className="text-[13px] font-medium text-slate-800">
                        Add medication
                    </div>

                    <p className="mt-0.5 text-[12px] font-normal text-slate-400">
                        Record a medication for this beneficiary.
                    </p>
                </div>
            </FloatingPanelHeader>

            <FloatingPanelBody className="space-y-5 px-4 py-4">
                <MedicationCreateDetailsSection
                    register={form.register}
                    errors={errors}
                />

                <MedicationCreateInstructionsSection
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

            <FloatingPanelFooter className="justify-end gap-2 border-t border-slate-100 px-4 py-3">
                <button
                    type="button"
                    onClick={closeFloatingPanel}
                    className="rounded-lg px-3 py-1.5 text-[13px] font-medium text-slate-500 hover:text-slate-800"
                >
                    Cancel
                </button>

                <button
                    type="submit"
                    disabled={isSubmitting}
                    className="rounded-lg bg-teal-600 px-3 py-1.5 text-[13px] font-medium text-white hover:bg-teal-700 disabled:cursor-not-allowed disabled:opacity-60"
                >
                    {isSubmitting
                        ? 'Adding...'
                        : 'Add medication'}
                </button>
            </FloatingPanelFooter>
        </form>
    )
}
