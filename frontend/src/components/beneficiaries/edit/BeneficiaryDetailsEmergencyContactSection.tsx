import type { FieldErrors, UseFormRegister } from 'react-hook-form'

import type { BeneficiaryDetailsEditFormValues } from './BeneficiaryDetailsEditSchema'

type Props = {
    register: UseFormRegister<BeneficiaryDetailsEditFormValues>
    errors: FieldErrors<BeneficiaryDetailsEditFormValues>
}

const RELATIONSHIP_OPTIONS = [
    { value: 'PARENT', label: 'Parent' },
    { value: 'SIBLING', label: 'Sibling' },
    { value: 'OTHER_RELATIVE', label: 'Other relative' },
    { value: 'FRIEND', label: 'Friend' },
    { value: 'SOCIAL_WORKER', label: 'Social worker' },
    { value: 'OTHER', label: 'Other' },
] as const

function ErrorMessage({ message }: { message?: string }) {
    if (!message) {
        return null
    }

    return (
        <p className="text-[11px] text-red-500">
            {message}
        </p>
    )
}

export function BeneficiaryDetailsEmergencyContactSection({
    register,
    errors,
}: Props) {
    const emergencyErrors = errors.emergencyContact

    return (
        <section className="space-y-3 border-t border-slate-100 pt-4">
            <h3 className="text-[12px] font-medium text-slate-700">
                Emergency contact
            </h3>

            <div className="grid grid-cols-2 gap-3">
                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        First name
                    </span>

                    <input
                        {...register(
                            'emergencyContact.firstName'
                        )}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            emergencyErrors?.firstName?.message
                        }
                    />
                </label>

                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Last name
                    </span>

                    <input
                        {...register(
                            'emergencyContact.lastName'
                        )}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            emergencyErrors?.lastName?.message
                        }
                    />
                </label>
            </div>

            <label className="block space-y-1.5">
                <span className="text-[11px] text-slate-400">
                    Relationship
                </span>

                <select
                    {...register(
                        'emergencyContact.relationshipType'
                    )}
                    className="h-9 w-full rounded-lg border border-slate-200 bg-white px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                >
                    {RELATIONSHIP_OPTIONS.map((option) => (
                        <option
                            key={option.value}
                            value={option.value}
                        >
                            {option.label}
                        </option>
                    ))}
                </select>

                <ErrorMessage
                    message={
                        emergencyErrors?.relationshipType?.message
                    }
                />
            </label>

            <div className="grid grid-cols-2 gap-3">
                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Mobile
                    </span>

                    <input
                        type="tel"
                        {...register(
                            'emergencyContact.mobileNumber'
                        )}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            emergencyErrors?.mobileNumber?.message
                        }
                    />
                </label>

                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Landline
                    </span>

                    <input
                        type="tel"
                        {...register(
                            'emergencyContact.landlinePhone'
                        )}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            emergencyErrors?.landlinePhone?.message
                        }
                    />
                </label>
            </div>

            <label className="block space-y-1.5">
                <span className="text-[11px] text-slate-400">
                    Email
                </span>

                <input
                    type="email"
                    {...register('emergencyContact.email')}
                    className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                />

                <ErrorMessage
                    message={emergencyErrors?.email?.message}
                />
            </label>

            <div className="pt-1 text-[11px] font-medium text-slate-500">
                Contact address
            </div>

            <div className="grid grid-cols-[1fr_auto] gap-3">
                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Street
                    </span>

                    <input
                        {...register(
                            'emergencyContact.address.street'
                        )}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            emergencyErrors?.address?.street?.message
                        }
                    />
                </label>

                <label className="w-24 space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Number
                    </span>

                    <input
                        {...register(
                            'emergencyContact.address.streetNumber'
                        )}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            emergencyErrors?.address?.streetNumber
                                ?.message
                        }
                    />
                </label>
            </div>

            <div className="grid grid-cols-2 gap-3">
                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        City
                    </span>

                    <input
                        {...register(
                            'emergencyContact.address.city'
                        )}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            emergencyErrors?.address?.city?.message
                        }
                    />
                </label>

                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Postal code
                    </span>

                    <input
                        {...register(
                            'emergencyContact.address.zipCode'
                        )}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            emergencyErrors?.address?.zipCode?.message
                        }
                    />
                </label>
            </div>
        </section>
    )
}
