import type {
    FieldErrors,
    UseFormRegister,
} from 'react-hook-form'

import type { MedicationCreateFormValues } from './MedicationCreateSchema'

type Props = {
    register: UseFormRegister<MedicationCreateFormValues>
    errors: FieldErrors<MedicationCreateFormValues>
}

function ErrorMessage({ message }: { message?: string }) {
    if (!message) {
        return null
    }

    return (
        <p className="text-[11px] text-red-600">
            {message}
        </p>
    )
}

const inputClassName =
    'h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300'

export function MedicationCreateDetailsSection({
    register,
    errors,
}: Props) {
    return (
        <section className="space-y-4">
            <div className="grid grid-cols-2 gap-3">
                <label className="block space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Name
                    </span>

                    <input
                        {...register('name')}
                        className={inputClassName}
                    />

                    <ErrorMessage message={errors.name?.message} />
                </label>

                <label className="block space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Dosage
                    </span>

                    <input
                        {...register('dosage')}
                        className={inputClassName}
                    />

                    <ErrorMessage message={errors.dosage?.message} />
                </label>

                <label className="block space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Frequency
                    </span>

                    <input
                        {...register('frequency')}
                        className={inputClassName}
                    />

                    <ErrorMessage message={errors.frequency?.message} />
                </label>

                <label className="block space-y-1.5">
                    <span className="text-[11px] text-slate-400">
                        Administration times
                    </span>

                    <input
                        {...register('administrationTimes')}
                        className={inputClassName}
                    />

                    <ErrorMessage
                        message={errors.administrationTimes?.message}
                    />
                </label>
            </div>

            <label className="block space-y-1.5">
                <span className="text-[11px] text-slate-400">
                    Start date
                </span>

                <input
                    type="date"
                    {...register('startedAt')}
                    className={inputClassName}
                />

                <ErrorMessage message={errors.startedAt?.message} />
            </label>
        </section>
    )
}
