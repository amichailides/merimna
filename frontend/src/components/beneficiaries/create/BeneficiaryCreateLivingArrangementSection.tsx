import { Controller, type Control } from 'react-hook-form'

import type { HouseUnitReadOnlyDTO } from '@/api/types'
import type { BeneficiaryCreateFormValues } from './BeneficiaryCreateSchema'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'

type BeneficiaryCreateLivingArrangementSectionProps = {
    control: Control<BeneficiaryCreateFormValues>
    houseUnits: HouseUnitReadOnlyDTO[]
    houseUnitsLoading: boolean
}

const selectClassName = `
    h-9 w-full rounded-none border-0 border-b border-slate-200
    bg-transparent px-0 text-[13px] shadow-none

    focus:border-b-teal-600
    focus:outline-none
    focus:ring-0

    aria-invalid:border-0
    aria-invalid:border-b
    aria-invalid:border-b-red-500

    disabled:cursor-not-allowed
    disabled:text-slate-400
`

export function BeneficiaryCreateLivingArrangementSection({
    control,
    houseUnits,
    houseUnitsLoading,
}: BeneficiaryCreateLivingArrangementSectionProps) {
    return (
        <section className="space-y-5">
            <div className="border-b border-slate-100 pb-2">
                <h2 className="text-[14px] font-medium text-slate-900">
                    Living arrangement
                </h2>
            </div>

            <div className="grid gap-x-6 gap-y-5 sm:grid-cols-2">
                <Controller
                    control={control}
                    name="houseUnitPublicId"
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                House unit
                            </FieldLabel>

                            <select
                                {...field}
                                id={field.name}
                                disabled={houseUnitsLoading}
                                aria-invalid={fieldState.invalid}
                                className={selectClassName}
                            >
                                <option value="">
                                    {houseUnitsLoading
                                        ? 'Loading house units...'
                                        : 'Select house unit'}
                                </option>

                                {houseUnits.map((houseUnit) => (
                                    <option
                                        key={houseUnit.publicId}
                                        value={houseUnit.publicId}
                                    >
                                        {houseUnit.displayName ??
                                            houseUnit.code ??
                                            houseUnit.publicId}
                                    </option>
                                ))}
                            </select>

                            {fieldState.invalid && (
                                <FieldError errors={[fieldState.error]} />
                            )}
                        </Field>
                    )}
                />
            </div>
        </section>
    )
}
