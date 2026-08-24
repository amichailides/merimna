import { Controller, type Control } from 'react-hook-form'

import type { EmployeeOnboardingFormValues } from './employeeOnboardingForm'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'
import { Input } from '@/components/ui/input'

type PersonalDetailsSectionProps = {
    control: Control<EmployeeOnboardingFormValues>
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

export function PersonalDetailsSection({
    control,
}: PersonalDetailsSectionProps) {
    return (
        <section className="space-y-5">
            <div className="border-b border-slate-100 pb-2">
                <h2 className="text-[14px] font-medium text-slate-900">
                    Personal details
                </h2>
            </div>

            <div className="grid gap-x-6 gap-y-5 sm:grid-cols-2">
                <Controller
                    control={control}
                    name="employee.firstName"
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                First name
                            </FieldLabel>

                            <Input
                                {...field}
                                id={field.name}
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
                    name="employee.lastName"
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Last name
                            </FieldLabel>

                            <Input
                                {...field}
                                id={field.name}
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
                    name="employee.dateOfBirth"
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Date of birth
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
