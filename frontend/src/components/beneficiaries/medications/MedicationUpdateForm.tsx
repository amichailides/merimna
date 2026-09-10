import axios from 'axios'
import { useState } from 'react'
import { type Path, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'

import { updateMedication } from '@/api/medicationApi'
import type {
    MedicationReadOnlyDTO,
    ValidationErrorResponse,
} from '@/api/types'
import {
    FloatingPanelBody,
    FloatingPanelFooter,
    FloatingPanelHeader,
    useFloatingPanel,
} from '@/components/ui/floating-panel'
import { applyServerValidationErrors } from '@/lib/applyServerValidationErrors'

import { MedicationCreateDetailsSection } from './MedicationCreateDetailsSection'
import { MedicationCreateInstructionsSection } from './MedicationCreateInstructionsSection'
import { buildMedicationUpdatePayload } from './MedicationUpdatePayload'
import {
    medicationUpdateSchema,
    type MedicationUpdateFormValues,
} from './MedicationUpdateSchema'

type Props = {
    beneficiaryPublicId: string
    medication: MedicationReadOnlyDTO
    onMedicationUpdated?: () => void | Promise<void>
}

const medicationUpdateFormPaths =
    new Set<Path<MedicationUpdateFormValues>>([
        'name',
        'dosage',
        'frequency',
        'administrationTimes',
        'instructions',
        'startedAt',
    ])

function isMedicationUpdateFormPath(
    path: string
): path is Path<MedicationUpdateFormValues> {
    return medicationUpdateFormPaths.has(
        path as Path<MedicationUpdateFormValues>
    )
}

export function MedicationUpdateForm({
    beneficiaryPublicId,
    medication,
    onMedicationUpdated,
}: Props) {
    const { closeFloatingPanel } = useFloatingPanel()
    const [submitError, setSubmitError] = useState<string | null>(null)

    const form = useForm<MedicationUpdateFormValues>({
        resolver: zodResolver(medicationUpdateSchema),
        defaultValues: {
            name: medication.name ?? '',
            dosage: medication.dosage ?? '',
            frequency: medication.frequency ?? '',
            administrationTimes:
                medication.administrationTimes ?? '',
            instructions: medication.instructions ?? '',
            startedAt: medication.startedAt ?? '',
        },
    })

    const {
        errors,
        isSubmitting,
        isDirty,
    } = form.formState

    async function handleSubmit(values: MedicationUpdateFormValues) {
        form.clearErrors()
        setSubmitError(null)

        if (!medication.publicId) {
            setSubmitError('Could not identify the medication.')
            return
        }

        const payload = buildMedicationUpdatePayload(
            values,
            medication
        )

        if (Object.keys(payload).length === 0) {
            closeFloatingPanel()
            return
        }

        try {
            await updateMedication(
                beneficiaryPublicId,
                medication.publicId,
                payload
            )

            await onMedicationUpdated?.()
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
                        isFormPath: isMedicationUpdateFormPath,
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
                    'Could not update the medication. Please review the form and try again.'
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
                        Edit medication
                    </div>

                    <p className="mt-0.5 text-[12px] font-normal text-slate-400">
                        Update this medication&apos;s details.
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
