import { z } from 'zod'
import { Controller, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { acceptInvitation } from '@/api/authApi'

import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'

const acceptInvitationSchema = z.object({
    username: z.string().min(1, 'Username is required'),
    password: z.string().min(1, 'Password is required'),
})

type AcceptInvitationFormValues = z.infer<
    typeof acceptInvitationSchema
>

const inputClassName = `
    h-10 rounded-lg border-slate-200 bg-white
    text-[13px] text-slate-900 shadow-none
    focus-visible:border-teal-600
    focus-visible:ring-teal-100
    aria-invalid:border-red-500
`

export function AcceptInvitationPage() {
    const [searchParams] = useSearchParams()
    const token = searchParams.get('token')
    const navigate = useNavigate()

    const form = useForm<AcceptInvitationFormValues>({
        resolver: zodResolver(acceptInvitationSchema),
        defaultValues: {
            username: '',
            password: '',
        },
    })

    async function onSubmit(values: AcceptInvitationFormValues) {
        if (!token) {
            return
        }

        await acceptInvitation({
            token,
            username: values.username,
            password: values.password,
        })

        navigate('/login', { replace: true })
    }

    return (
        <main className="min-h-screen bg-[#f4f8f7] px-6 py-10">
            <div className="mx-auto flex min-h-[calc(100vh-5rem)] max-w-6xl items-center justify-center">
                <div className="w-full max-w-sm">
                    <div className="mb-8">
                        <p className="font-logo text-[22px] font-semibold tracking-[-0.02em] text-teal-700">
                            Merimna
                        </p>

                        <p className="mt-1 text-[13px] text-slate-500">
                            Supported living management platform
                        </p>
                    </div>

                    <div className="rounded-2xl border border-teal-100 bg-white px-6 py-6 shadow-[0_1px_2px_rgba(15,23,42,0.04)]">
                        <div className="mb-5">
                            <h1 className="text-[18px] font-medium tracking-[-0.01em] text-slate-950">
                                Accept invitation
                            </h1>

                            <p className="mt-1 text-[13px] leading-5 text-slate-500">
                                Choose your username and password to activate
                                your account.
                            </p>
                        </div>

                        <form
                            onSubmit={form.handleSubmit(onSubmit)}
                            noValidate
                            className="space-y-4"
                        >
                            <Controller
                                control={form.control}
                                name="username"
                                render={({ field, fieldState }) => (
                                    <Field>
                                        <FieldLabel
                                            htmlFor={field.name}
                                            className="text-[12px] font-medium text-slate-700"
                                        >
                                            Username
                                        </FieldLabel>

                                        <Input
                                            {...field}
                                            id={field.name}
                                            type="text"
                                            autoComplete="username"
                                            aria-invalid={fieldState.invalid}
                                            className={inputClassName}
                                        />

                                        {fieldState.invalid && (
                                            <FieldError
                                                errors={[fieldState.error]}
                                            />
                                        )}
                                    </Field>
                                )}
                            />

                            <Controller
                                control={form.control}
                                name="password"
                                render={({ field, fieldState }) => (
                                    <Field>
                                        <FieldLabel
                                            htmlFor={field.name}
                                            className="text-[12px] font-medium text-slate-700"
                                        >
                                            Password
                                        </FieldLabel>

                                        <Input
                                            {...field}
                                            id={field.name}
                                            type="password"
                                            autoComplete="new-password"
                                            aria-invalid={fieldState.invalid}
                                            className={inputClassName}
                                        />

                                        {fieldState.invalid && (
                                            <FieldError
                                                errors={[fieldState.error]}
                                            />
                                        )}
                                    </Field>
                                )}
                            />

                            <Button
                                type="submit"
                                className="mt-2 h-10 w-full rounded-lg bg-teal-700 text-[13px] font-medium text-white shadow-none hover:bg-teal-800"
                            >
                                Activate account
                            </Button>
                        </form>
                    </div>

                    <p className="mt-4 text-center text-[12px] text-slate-400">
                        Demo environment · Merimna
                    </p>
                </div>
            </div>
        </main>
    )
}