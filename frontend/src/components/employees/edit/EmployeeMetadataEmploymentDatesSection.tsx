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

export function EmployeeMetadataEmploymentDatesSection({
    register,
    errors,
}: Props) {
    return (
        <section className="space-y-3 border-t border-slate-100 pt-4">
            <h3 className="text-[12px] font-medium text-slate-700">
                Employment
            </h3>

            <label className="block space-y-1.5">
                <span className="text-[11px] text-slate-400">
                    Hire date
                </span>

                <input
                    type="date"
                    {...register('hireDate')}
                    className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                />

                <ErrorMessage
                    message={errors.hireDate?.message}
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