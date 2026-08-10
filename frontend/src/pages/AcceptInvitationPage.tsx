import { useState } from 'react'
import axios from 'axios'
import { z } from 'zod'
import { Controller, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { Navigate, useNavigate, useSearchParams } from 'react-router-dom'
import { useAuth } from '@/auth/useAuth'

import { acceptInvitation } from '@/api/authApi'
import type { ValidationErrorResponse } from '@/api/types'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'
import { Eye, EyeOff } from 'lucide-react'

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
    const { isAuthenticated, isAuthLoading } = useAuth()
    const [searchParams] = useSearchParams()
    const navigate = useNavigate()
    const token = searchParams.get('token')

    const [submitError, setSubmitError] = useState<string | null>(null)

    const form = useForm<AcceptInvitationFormValues>({
        resolver: zodResolver(acceptInvitationSchema),
        defaultValues: {
            username: '',
            password: '',
        },
    })

    const [showPassword, setShowPassword] = useState(false)

    if (isAuthLoading) {
        return null
    }

    if (isAuthenticated) {
        return <Navigate to="/dashboard" replace />
    }

    if (!token) {
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
                            <h1 className="text-[18px] font-medium tracking-[-0.01em] text-slate-950">
                                Invalid invitation link
                            </h1>

                            <p className="mt-2 text-[13px] leading-5 text-slate-500">
                                This invitation link is missing the required token.
                            </p>

                            <Button
                                type="button"
                                onClick={() => navigate('/login')}
                                className="mt-5 h-10 w-full rounded-lg bg-teal-700 text-[13px] font-medium text-white shadow-none hover:bg-teal-800"
                            >
                                Go to sign in
                            </Button>
                        </div>
                    </div>
                </div>
            </main>
        )
    }

    const invitationToken = token

    async function onSubmit(values: AcceptInvitationFormValues) {


        setSubmitError(null)

        try {
            await acceptInvitation({
                token: invitationToken,
                username: values.username,
                password: values.password,
            })

            navigate('/login', { replace: true })
        } catch (error) {
            if (axios.isAxiosError<ValidationErrorResponse>(error)) {
                const errorResponse = error.response?.data
                const validationErrors = errorResponse?.validationErrors

                if (validationErrors) {
                    let fieldErrorApplied = false
                    let unknownFieldError = false

                    for (const [path, messages] of Object.entries(
                        validationErrors
                    )) {
                        const message = messages[0]

                        if (!message) {
                            continue
                        }

                        if (path !== 'username' && path !== 'password') {
                            unknownFieldError = true
                            continue
                        }

                        form.setError(path, {
                            type: 'server',
                            message,
                        })

                        fieldErrorApplied = true
                    }

                    if (fieldErrorApplied && !unknownFieldError) {
                        return
                    }
                }

                setSubmitError(
                    errorResponse?.detail ??
                    'Could not activate the account. Please try again.'
                )

                return
            }

            setSubmitError(
                'Could not connect to the server. Please try again.'
            )
        }
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

                                        <div className="relative">
                                            <Input
                                                {...field}
                                                id={field.name}
                                                type={showPassword ? 'text' : 'password'}
                                                autoComplete="new-password"
                                                aria-invalid={fieldState.invalid}
                                                className={`${inputClassName} pr-10`}
                                            />

                                            <button
                                                type="button"
                                                onClick={() => setShowPassword((current) => !current)}
                                                aria-label={showPassword ? 'Hide password' : 'Show password'}
                                                aria-pressed={showPassword}
                                                className="
            absolute inset-y-0 right-0 flex w-10 items-center
            justify-center text-slate-400
            hover:text-slate-600
            focus-visible:outline-none
            focus-visible:text-teal-700
        "
                                            >
                                                {showPassword ? (
                                                    <EyeOff className="size-4" />
                                                ) : (
                                                    <Eye className="size-4" />
                                                )}
                                            </button>
                                        </div>

                                        {fieldState.invalid && (
                                            <FieldError
                                                errors={[fieldState.error]}
                                            />
                                        )}
                                    </Field>
                                )}
                            />

                            {submitError && (
                                <p
                                    role="alert"
                                    className="text-[12px] leading-5 text-red-600"
                                >
                                    {submitError}
                                </p>
                            )}

                            <Button
                                type="submit"
                                disabled={form.formState.isSubmitting}
                                className="mt-2 h-10 w-full rounded-lg bg-teal-700 text-[13px] font-medium text-white shadow-none hover:bg-teal-800 disabled:cursor-not-allowed disabled:opacity-60"
                            >
                                {form.formState.isSubmitting
                                    ? 'Activating...'
                                    : 'Activate account'}
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
