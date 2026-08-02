import { Controller, type Control } from 'react-hook-form'

import type { EmployeeOnboardingFormValues } from './employeeOnboardingForm'

type SystemAccessSectionProps = {
    control: Control<EmployeeOnboardingFormValues>
}

export function SystemAccessSection({
    control,
}: SystemAccessSectionProps) {
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
        </section>
    )
}