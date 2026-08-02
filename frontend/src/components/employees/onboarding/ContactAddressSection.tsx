import { Controller, type Control } from 'react-hook-form'

import type { EmployeeOnboardingFormValues } from './employeeOnboardingForm'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'
import { Input } from '@/components/ui/input'

type ContactAddressSectionProps = {
    control: Control<EmployeeOnboardingFormValues>
}

const inputClassName = `
    h-9 rounded-none border-0 border-b border-slate-200
    bg-transparent px-0 shadow-none
    focus-visible:border-teal-600
    focus-visible:ring-0
    aria-invalid:border-red-500
`

export function ContactAddressSection({
    control,
}: ContactAddressSectionProps) {
    return (
        <section className="space-y-5">
            <div className="border-b border-slate-100 pb-2">
                <h2 className="text-[14px] font-medium text-slate-900">
                    Contact & address
                </h2>
            </div>

            <div className="grid gap-x-6 gap-y-5 sm:grid-cols-2">
                <Controller
                    control={control}
                    name="contactEmail"
                    rules={{ required: 'Email is required' }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Email
                            </FieldLabel>

                            <Input
                                {...field}
                                id={field.name}
                                type="email"
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
                    name="mobileNumber"
                    rules={{ required: 'Mobile number is required' }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Mobile number
                            </FieldLabel>

                            <Input
                                {...field}
                                id={field.name}
                                type="tel"
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
                    name="street"
                    rules={{ required: 'Street is required' }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Street
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
                    name="streetNumber"
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Street number
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
                    name="city"
                    rules={{ required: 'City is required' }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                City
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
                    name="zipCode"
                    rules={{ required: 'Postal code is required' }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Postal code
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
                    name="emergencyContactName"
                    rules={{ required: 'Emergency contact name is required' }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Emergency contact name
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
                    name="emergencyContactPhoneNumber"
                    rules={{ required: 'Emergency contact phone is required' }}
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Emergency contact phone
                            </FieldLabel>

                            <Input
                                {...field}
                                id={field.name}
                                type="tel"
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