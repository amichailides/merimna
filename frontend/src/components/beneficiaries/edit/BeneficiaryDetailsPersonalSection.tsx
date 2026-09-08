import type { FieldErrors, UseFormRegister } from 'react-hook-form'

import type { BeneficiaryDetailsEditFormValues } from './BeneficiaryDetailsEditSchema'

type Props = {
    register: UseFormRegister<BeneficiaryDetailsEditFormValues>
    errors: FieldErrors<BeneficiaryDetailsEditFormValues>
}

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

export function BeneficiaryDetailsPersonalSection({
    register,
    errors,
}: Props) {
    return (
        <section className="space-y-3">
            <h3 className="text-[12px] font-medium text-slate-700">
                Personal
            </h3>

            <div className="grid grid-cols-2 gap-3">
                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        First name
                    </span>

                    <input
                        {...register('firstName')}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={errors.firstName?.message}
                    />
                </label>

                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Last name
                    </span>

                    <input
                        {...register('lastName')}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={errors.lastName?.message}
                    />
                </label>
            </div>

            <label className="block space-y-1.5">
                <span className="text-[11px] text-slate-400">
                    AMKA
                </span>

                <input
                    {...register('amka')}
                    inputMode="numeric"
                    className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                />

                <ErrorMessage
                    message={errors.amka?.message}
                />
            </label>

            <label className="block space-y-1.5">
                <span className="text-[11px] text-slate-400">
                    Date of birth
                </span>

                <input
                    type="date"
                    {...register('dateOfBirth')}
                    className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                />

                <ErrorMessage
                    message={errors.dateOfBirth?.message}
                />
            </label>
        </section>
    )
}
