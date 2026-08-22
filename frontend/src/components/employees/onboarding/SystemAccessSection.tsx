import { Controller, type Control, useWatch } from 'react-hook-form'

import type { EmployeeOnboardingFormValues } from './employeeOnboardingForm'
import { Input } from '@/components/ui/input'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'

type SystemAccessSectionProps = {
    control: Control<EmployeeOnboardingFormValues>
}

export function SystemAccessSection({
    control,
}: SystemAccessSectionProps) {
    const grantSystemAccess = useWatch({
        control,
        name: 'grantSystemAccess',
    })

    return (
        <section className="space-y-5">
            <div className="border-b border-slate-100 pb-2">
                <h2 className="text-[14px] font-medium text-slate-900">
                    System access
                </h2>
            </div>

            <Controller
                control={control}
                name="grantSystemAccess"
                render={({ field }) => (
                    <label className="flex cursor-pointer items-start gap-3">
                        <input
                            type="checkbox"
                            checked={field.value}
                            onChange={(event) =>
                                field.onChange(event.target.checked)
                            }
                            className="mt-0.5 h-4 w-4 rounded border-slate-300 accent-teal-700"
                        />

                        <span>
                            <span className="block text-[13px] font-medium text-slate-800">
                                Send account invitation
                            </span>

                            <span className="mt-0.5 block text-[12px] leading-5 text-slate-400">
                                The employee will receive an email to choose a
                                username and password.
                            </span>
                        </span>
                    </label>
                )}
            />

            {grantSystemAccess && (
                <Controller
                    control={control}
                    name="systemAccess.accountEmail"
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel
                                htmlFor={field.name}
                                className="text-[12px] font-medium text-slate-700"
                            >
                                Account email
                            </FieldLabel>

                            <Input
                                {...field}
                                id={field.name}
                                type="email"
                                autoComplete="off"
                                aria-invalid={fieldState.invalid}
                                placeholder="employee@merimna.com"
                                className="
                                    h-9 rounded-none border-0 border-b border-slate-200
                                    bg-transparent px-0 shadow-none

                                    focus-visible:border-b-teal-600
                                    focus-visible:ring-0

                                    aria-invalid:border-0
                                    aria-invalid:border-b
                                    aria-invalid:border-b-red-500
                                    aria-invalid:ring-0
                                "
                            />

                            {fieldState.invalid && (
                                <FieldError
                                    errors={[fieldState.error]}
                                />
                            )}
                        </Field>
                    )}
                />
            )}
        </section>
    )
}