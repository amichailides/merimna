import { Controller, type Control } from 'react-hook-form'

import type { BeneficiaryCreateFormValues } from './BeneficiaryCreateSchema'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'
import { Input } from '@/components/ui/input'

type BeneficiaryCreateEmergencyContactSectionProps = {
    control: Control<BeneficiaryCreateFormValues>
}

const RELATIONSHIP_OPTIONS = [
    { value: 'PARENT', label: 'Parent' },
    { value: 'SIBLING', label: 'Sibling' },
    { value: 'OTHER_RELATIVE', label: 'Other relative' },
    { value: 'FRIEND', label: 'Friend' },
    { value: 'SOCIAL_WORKER', label: 'Social worker' },
    { value: 'OTHER', label: 'Other' },
] as const

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

export function BeneficiaryCreateEmergencyContactSection({
    control,
}: BeneficiaryCreateEmergencyContactSectionProps) {
    return (
        <section className="space-y-5">
            <div className="border-b border-slate-100 pb-2">
                <h2 className="text-[14px] font-medium text-slate-900">
                    Emergency contact
                </h2>
            </div>

            <div className="grid gap-x-6 gap-y-5 sm:grid-cols-2">
                <Controller
                    control={control}
                    name="emergencyContact.firstName"
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
                    name="emergencyContact.lastName"
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
                    name="emergencyContact.relationshipType"
                    render={({ field, fieldState }) => (
                        <Field className="sm:col-span-2">
                            <FieldLabel htmlFor={field.name}>
                                Relationship
                            </FieldLabel>

                            <select
                                {...field}
                                value={field.value ?? ''}
                                id={field.name}
                                aria-invalid={fieldState.invalid}
                                className={inputClassName}
                            >
                                <option value="">
                                    Select relationship
                                </option>

                                {RELATIONSHIP_OPTIONS.map((option) => (
                                    <option
                                        key={option.value}
                                        value={option.value}
                                    >
                                        {option.label}
                                    </option>
                                ))}
                            </select>

                            {fieldState.invalid && (
                                <FieldError errors={[fieldState.error]} />
                            )}
                        </Field>
                    )}
                />

                <Controller
                    control={control}
                    name="emergencyContact.mobileNumber"
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
                    name="emergencyContact.landlinePhone"
                    render={({ field, fieldState }) => (
                        <Field>
                            <FieldLabel htmlFor={field.name}>
                                Landline
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
                    name="emergencyContact.email"
                    render={({ field, fieldState }) => (
                        <Field className="sm:col-span-2">
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
            </div>

            <div className="space-y-5 pt-1">
                <div className="border-b border-slate-100 pb-2">
                    <h3 className="text-[13px] font-medium text-slate-700">
                        Address
                    </h3>
                </div>

                <div className="grid gap-x-6 gap-y-5 sm:grid-cols-2">
                    <Controller
                        control={control}
                        name="emergencyContact.address.street"
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
                        name="emergencyContact.address.streetNumber"
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
                        name="emergencyContact.address.city"
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
                        name="emergencyContact.address.zipCode"
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
                </div>
            </div>
        </section>
    )
}
