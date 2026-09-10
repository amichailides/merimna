import type {
    FieldErrors,
    UseFormRegister,
} from 'react-hook-form'

import type { MedicationCreateFormValues } from './MedicationCreateSchema'

type Props = {
    register: UseFormRegister<MedicationCreateFormValues>
    errors: FieldErrors<MedicationCreateFormValues>
}

export function MedicationCreateInstructionsSection({
    register,
    errors,
}: Props) {
    return (
        <label className="block space-y-1.5">
            <span className="text-[11px] text-slate-400">
                Instructions
            </span>

            <textarea
                {...register('instructions')}
                rows={3}
                className="
                    w-full resize-none rounded-lg border border-slate-200
                    px-3 py-2 text-[13px] text-slate-800
                    outline-none focus:border-slate-300
                "
            />

            {errors.instructions?.message && (
                <p className="text-[11px] text-red-600">
                    {errors.instructions.message}
                </p>
            )}
        </label>
    )
}
