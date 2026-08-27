import type {
    FieldErrors,
    UseFormRegister,
} from 'react-hook-form'

import type { EmployeeMetadataEditFormValues } from './EmployeeMetadataEditSchema'

type Props = {
    register: UseFormRegister<EmployeeMetadataEditFormValues>
    errors: FieldErrors<EmployeeMetadataEditFormValues>
}

type ErrorMessageProps = {
    message?: string
}

function ErrorMessage({ message }: ErrorMessageProps) {
    if (!message) {
        return null
    }

    return (
        <p className="text-[11px] text-red-500">
            {message}
        </p>
    )
}

export function EmployeeMetadataContactSection({
    register,
    errors,
}: Props) {
    return (
        <section className="space-y-3">
            <h3 className="text-[12px] font-medium text-slate-700">
                Contact
            </h3>

            <label className="block space-y-1.5">
                <span className="text-[11px] text-slate-400">
                    Email
                </span>

                <input
                    type="email"
                    {...register('contactEmail')}
                    className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                />

                <ErrorMessage
                    message={errors.contactEmail?.message}
                />
            </label>

            <label className="block space-y-1.5">
                <span className="text-[11px] text-slate-400">
                    Mobile
                </span>

                <input
                    type="tel"
                    {...register('mobileNumber')}
                    className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                />

                <ErrorMessage
                    message={errors.mobileNumber?.message}
                />
            </label>

            <div className="space-y-2">
                <div className="text-[11px] text-slate-400">
                    Address
                </div>

                <div className="grid grid-cols-2 gap-2">
                    <label className="block space-y-1.5">
                        <span className="text-[11px] text-slate-400">
                            Street
                        </span>

                        <input
                            {...register('address.street')}
                            className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                        />

                        <ErrorMessage
                            message={
                                errors.address?.street?.message
                            }
                        />
                    </label>

                    <label className="block space-y-1.5">
                        <span className="text-[11px] text-slate-400">
                            No.
                        </span>

                        <input
                            {...register('address.streetNumber')}
                            className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                        />

                        <ErrorMessage
                            message={
                                errors.address?.streetNumber
                                    ?.message
                            }
                        />
                    </label>

                    <label className="block space-y-1.5">
                        <span className="text-[11px] text-slate-400">
                            City
                        </span>

                        <input
                            {...register('address.city')}
                            className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                        />

                        <ErrorMessage
                            message={
                                errors.address?.city?.message
                            }
                        />
                    </label>

                    <label className="block space-y-1.5">
                        <span className="text-[11px] text-slate-400">
                            ZIP
                        </span>

                        <input
                            {...register('address.zipCode')}
                            className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                        />

                        <ErrorMessage
                            message={
                                errors.address?.zipCode?.message
                            }
                        />
                    </label>
                </div>
            </div>
        </section>
    )
}
