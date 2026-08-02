import { Controller, type Control } from 'react-hook-form'

import type {
    EmployeePositionReadOnlyDTO,
    HouseUnitReadOnlyDTO,
} from '@/api/types'
import type { EmployeeOnboardingFormValues } from './employeeOnboardingForm'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'
import { Input } from '@/components/ui/input'
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select'

type EmploymentAssignmentSectionProps = {
    control: Control<EmployeeOnboardingFormValues>
    positions: EmployeePositionReadOnlyDTO[]
    positionsLoading: boolean
    houseUnits: HouseUnitReadOnlyDTO[]
    houseUnitsLoading: boolean
}

const inputClassName = `
    h-9 rounded-none border-0 border-b border-slate-200
    bg-transparent px-0 shadow-none

    focus-visible:border-b-teal-600
    focus-visible:ring-0

    aria-invalid:border-0
    aria-invalid:border-b
    aria-invalid:border-b-red-500
    aria-invalid:ring-0
`

const selectTriggerClassName = `
    h-9 rounded-none border-0 border-b border-slate-200
    bg-transparent px-0 shadow-none

    focus:ring-0
    focus-visible:border-b-teal-600

    aria-invalid:border-0
    aria-invalid:border-b
    aria-invalid:border-b-red-500
    aria-invalid:ring-0
`

export function EmploymentAssignmentSection({
    control,
    positions,
    positionsLoading,
    houseUnits,
    houseUnitsLoading,
}: EmploymentAssignmentSectionProps) {
    return (
        <section className="space-y-5">
            <div className="border-b border-slate-100 pb-2">
                <h2 className="text-[14px] font-medium text-slate-900">
                    Employment & initial assignment
                </h2>
            </div>

            <div className="grid gap-x-6 gap-y-5 sm:grid-cols-2">
                <Controller
                    control={control}
                    name="employee.positionCode"
                    rules={{ required: 'Position is required' }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel>Position</FieldLabel>

                            <Select
                                value={field.value}
                                onValueChange={field.onChange}
                                disabled={positionsLoading}
                            >
                                <SelectTrigger
                                    aria-invalid={fieldState.invalid}
                                    className={selectTriggerClassName}
                                >
                                    <SelectValue
                                        placeholder={
                                            positionsLoading
                                                ? 'Loading positions...'
                                                : 'Select position'
                                        }
                                    />
                                </SelectTrigger>

                                <SelectContent>
                                    {positions.map((position) => {
                                        if (!position.code) {
                                            return null
                                        }

                                        return (
                                            <SelectItem
                                                key={position.code}
                                                value={position.code}
                                            >
                                                {position.displayName ??
                                                    position.code}
                                            </SelectItem>
                                        )
                                    })}
                                </SelectContent>
                            </Select>

                            {fieldState.invalid && (
                                <FieldError errors={[fieldState.error]} />
                            )}
                        </Field>
                    )}
                />

                <Controller
                    control={control}
                    name="employee.hireDate"
                    rules={{ required: 'Hire date is required' }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Hire date
                            </FieldLabel>

                            <Input
                                {...field}
                                id={field.name}
                                type="date"
                                aria-invalid={fieldState.invalid}
                                className={inputClassName}
                            />

                            {fieldState.invalid && (
                                <FieldError errors={[fieldState.error]} />
                            )}
                        </Field>
                    )}
                />

                <Controller
                    control={control}
                    name="initialAssignment.houseUnitPublicId"
                    rules={{ required: 'House unit is required' }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel>House unit</FieldLabel>

                            <Select
                                value={field.value}
                                onValueChange={field.onChange}
                                disabled={houseUnitsLoading}
                            >
                                <SelectTrigger
                                    aria-invalid={fieldState.invalid}
                                    className={selectTriggerClassName}
                                >
                                    <SelectValue
                                        placeholder={
                                            houseUnitsLoading
                                                ? 'Loading house units...'
                                                : 'Select house unit'
                                        }
                                    />
                                </SelectTrigger>

                                <SelectContent>
                                    {houseUnits.map((houseUnit) => {
                                        if (!houseUnit.publicId) {
                                            return null
                                        }

                                        return (
                                            <SelectItem
                                                key={houseUnit.publicId}
                                                value={houseUnit.publicId}
                                            >
                                                {houseUnit.displayName ??
                                                    houseUnit.code ??
                                                    houseUnit.publicId}
                                            </SelectItem>
                                        )
                                    })}
                                </SelectContent>
                            </Select>

                            {fieldState.invalid && (
                                <FieldError errors={[fieldState.error]} />
                            )}
                        </Field>
                    )}
                />

                <Controller
                    control={control}
                    name="initialAssignment.startDate"
                    rules={{
                        required: 'Assignment start date is required',
                    }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Assignment start date
                            </FieldLabel>

                            <Input
                                {...field}
                                id={field.name}
                                type="date"
                                aria-invalid={fieldState.invalid}
                                className={inputClassName}
                            />

                            {fieldState.invalid && (
                                <FieldError errors={[fieldState.error]} />
                            )}
                        </Field>
                    )}
                />

                <Controller
                    control={control}
                    name="initialAssignment.endDate"
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Assignment end date
                            </FieldLabel>

                            <Input
                                {...field}
                                id={field.name}
                                type="date"
                                aria-invalid={fieldState.invalid}
                                className={inputClassName}
                            />

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
