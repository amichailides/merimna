import axios from 'axios'
import { useState } from 'react'
import { type Path, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'

import { updateBeneficiary } from '@/api/beneficiaryApi'
import type {
    BeneficiaryDetailsDTO,
    ValidationErrorResponse,
} from '@/api/types'
import {
    FloatingPanelBody,
    FloatingPanelFooter,
    FloatingPanelHeader,
    useFloatingPanel,
} from '@/components/ui/floating-panel'
import { applyServerValidationErrors } from '@/lib/applyServerValidationErrors'

import { BeneficiaryDetailsAddressSection } from './BeneficiaryDetailsAddressSection'
import { BeneficiaryDetailsEmergencyContactSection } from './BeneficiaryDetailsEmergencyContactSection'
import { BeneficiaryDetailsPersonalSection } from './BeneficiaryDetailsPersonalSection'
import {
    beneficiaryDetailsEditSchema,
    type BeneficiaryDetailsEditFormValues,
} from './BeneficiaryDetailsEditSchema'
import { buildBeneficiaryUpdatePayload } from './BeneficiaryDetailsEditPayload'

type Props = {
    beneficiary: BeneficiaryDetailsDTO
    onBeneficiaryUpdated?: () => void | Promise<void>
}

const beneficiaryDetailsFormPaths =
    new Set<Path<BeneficiaryDetailsEditFormValues>>([
        'firstName',
        'lastName',
        'amka',
        'dateOfBirth',
        'permanentAddress.street',
        'permanentAddress.streetNumber',
        'permanentAddress.city',
        'permanentAddress.zipCode',
        'emergencyContact.firstName',
        'emergencyContact.lastName',
        'emergencyContact.relationshipType',
        'emergencyContact.landlinePhone',
        'emergencyContact.mobileNumber',
        'emergencyContact.email',
        'emergencyContact.address.street',
        'emergencyContact.address.streetNumber',
        'emergencyContact.address.city',
        'emergencyContact.address.zipCode',
    ])

function isBeneficiaryDetailsFormPath(
    path: string
): path is Path<BeneficiaryDetailsEditFormValues> {
    return beneficiaryDetailsFormPaths.has(
        path as Path<BeneficiaryDetailsEditFormValues>
    )
}

export function BeneficiaryDetailsEditForm({
    beneficiary,
    onBeneficiaryUpdated,
}: Props) {
    const { closeFloatingPanel } = useFloatingPanel()
    const [submitError, setSubmitError] = useState<string | null>(null)

    const emergencyContact = beneficiary.emergencyContact

    const form = useForm<BeneficiaryDetailsEditFormValues>({
        resolver: zodResolver(beneficiaryDetailsEditSchema),
        defaultValues: {
            firstName: beneficiary.firstName ?? '',
            lastName: beneficiary.lastName ?? '',
            amka: beneficiary.amka ?? '',
            dateOfBirth: beneficiary.dateOfBirth ?? '',

            permanentAddress: {
                street: beneficiary.permanentAddress?.street ?? '',
                streetNumber:
                    beneficiary.permanentAddress?.streetNumber ?? '',
                city: beneficiary.permanentAddress?.city ?? '',
                zipCode: beneficiary.permanentAddress?.zipCode ?? '',
            },

            emergencyContact: {
                firstName: emergencyContact?.firstName ?? '',
                lastName: emergencyContact?.lastName ?? '',
                relationshipType:
                    emergencyContact?.relationshipType ?? 'OTHER',
                landlinePhone:
                    emergencyContact?.landlinePhone ?? '',
                mobileNumber:
                    emergencyContact?.mobileNumber ?? '',
                email: emergencyContact?.email ?? '',

                address: {
                    street:
                        emergencyContact?.address?.street ?? '',
                    streetNumber:
                        emergencyContact?.address?.streetNumber ?? '',
                    city:
                        emergencyContact?.address?.city ?? '',
                    zipCode:
                        emergencyContact?.address?.zipCode ?? '',
                },
            },
        },
    })

    const { errors, isDirty, isSubmitting } = form.formState

    async function handleSubmit(
        values: BeneficiaryDetailsEditFormValues
    ) {
        form.clearErrors()
        setSubmitError(null)

        const payload = buildBeneficiaryUpdatePayload(
            values,
            beneficiary
        )

        if (Object.keys(payload).length === 0) {
            closeFloatingPanel()
            return
        }

        const beneficiaryPublicId = beneficiary.publicId

        if (!beneficiaryPublicId) {
            setSubmitError('Beneficiary identifier is missing.')
            return
        }

        try {
            await updateBeneficiary(
                beneficiaryPublicId,
                payload
            )

            await onBeneficiaryUpdated?.()
            closeFloatingPanel()
        } catch (error) {
            if (
                axios.isAxiosError<ValidationErrorResponse>(error)
            ) {
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
                        isFormPath:
                            isBeneficiaryDetailsFormPath,
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
                    'Could not update the beneficiary. Please review the form and try again.'
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
                        Edit beneficiary details
                    </div>

                    <p className="mt-0.5 text-[12px] text-slate-400">
                        Update personal, address, and emergency contact information.
                    </p>
                </div>
            </FloatingPanelHeader>

            <FloatingPanelBody className="max-h-[70vh] space-y-5 overflow-y-auto px-4 py-4">
                <BeneficiaryDetailsPersonalSection
                    register={form.register}
                    errors={errors}
                />

                <BeneficiaryDetailsAddressSection
                    register={form.register}
                    errors={errors}
                />

                <BeneficiaryDetailsEmergencyContactSection
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
