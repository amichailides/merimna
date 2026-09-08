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

export function BeneficiaryDetailsAddressSection({
    register,
    errors,
}: Props) {
    return (
        <section className="space-y-3 border-t border-slate-100 pt-4">
            <h3 className="text-[12px] font-medium text-slate-700">
                Permanent address
            </h3>

            <div className="grid grid-cols-[1fr_auto] gap-3">
                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Street
                    </span>

                    <input
                        {...register('permanentAddress.street')}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            errors.permanentAddress?.street?.message
                        }
                    />
                </label>

                <label className="w-24 space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Number
                    </span>

                    <input
                        {...register(
                            'permanentAddress.streetNumber'
                        )}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            errors.permanentAddress?.streetNumber
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
                        {...register('permanentAddress.city')}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            errors.permanentAddress?.city?.message
                        }
                    />
                </label>

                <label className="space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Postal code
                    </span>

                    <input
                        {...register('permanentAddress.zipCode')}
                        className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                    />

                    <ErrorMessage
                        message={
                            errors.permanentAddress?.zipCode?.message
                        }
                    />
                </label>
            </div>
        </section>
    )
}
