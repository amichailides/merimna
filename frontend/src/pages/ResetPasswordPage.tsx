import { useState } from 'react'
import axios from 'axios'
import { z } from 'zod'
import { Eye, EyeOff } from 'lucide-react'
import { Controller, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import {
    Link,
    Navigate,
    useSearchParams,
} from 'react-router-dom'

import { useAuth } from '@/auth/useAuth'
import { resetPassword } from '@/api/authApi'
import type { ValidationErrorResponse } from '@/api/types'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'

const resetPasswordSchema = z.object({
    newPassword: z.string().min(1, 'Password is required'),
})

type ResetPasswordFormValues = z.infer<
    typeof resetPasswordSchema
>

const inputClassName = `
    h-10 rounded-lg border-slate-200 bg-white
    text-[13px] text-slate-900 shadow-none
    focus-visible:border-teal-600
    focus-visible:ring-teal-100
    aria-invalid:border-red-500
`

export function ResetPasswordPage() {
    const { isAuthenticated, isAuthLoading } = useAuth()
    const [searchParams] = useSearchParams()
    const token = searchParams.get('token')

    const [showPassword, setShowPassword] = useState(false)
    const [submitted, setSubmitted] = useState(false)
    const [submitError, setSubmitError] = useState<string | null>(null)

    const form = useForm<ResetPasswordFormValues>({
        resolver: zodResolver(resetPasswordSchema),
        defaultValues: {
            newPassword: '',
        },
    })

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
                                Invalid reset link
                            </h1>

                            <p className="mt-2 text-[13px] leading-5 text-slate-500">
                                This password reset link is missing the required token.
                            </p>

                            <Link
                                to="/forgot-password"
                                className="mt-5 block text-center text-[12px] font-medium text-teal-700 hover:text-teal-800"
                            >
                                Request a new reset link
                            </Link>
                        </div>
                    </div>
                </div>
            </main>
        )
    }

    const resetToken = token

    async function onSubmit(values: ResetPasswordFormValues) {
        setSubmitError(null)

        try {
            await resetPassword({
                token: resetToken,
                newPassword: values.newPassword,
            })

            setSubmitted(true)
        } catch (error) {
            if (axios.isAxiosError<ValidationErrorResponse>(error)) {
                const errorResponse = error.response?.data
                const validationErrors = errorResponse?.validationErrors
                const passwordMessage =
                    validationErrors?.newPassword?.[0]

                if (passwordMessage) {
                    form.setError('newPassword', {
                        type: 'server',
                        message: passwordMessage,
                    })

                    return
                }

                setSubmitError(
                    errorResponse?.detail ??
                    'Could not reset the password. Please try again.'
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
                        {submitted ? (
                            <>
                                <h1 className="text-[18px] font-medium tracking-[-0.01em] text-slate-950">
                                    Password updated
                                </h1>

                                <p className="mt-2 text-[13px] leading-5 text-slate-500">
                                    Your password has been reset successfully.
                                </p>

                                <Link
                                    to="/login"
                                    className="mt-5 block text-center text-[12px] font-medium text-teal-700 hover:text-teal-800"
                                >
                                    Back to sign in
                                </Link>
                            </>
                        ) : (
                            <>
                                <div className="mb-5">
                                    <h1 className="text-[18px] font-medium tracking-[-0.01em] text-slate-950">
                                        Reset password
                                    </h1>

                                    <p className="mt-1 text-[13px] leading-5 text-slate-500">
                                        Choose a new password for your Merimna account.
                                    </p>
                                </div>

                                <form
                                    onSubmit={form.handleSubmit(onSubmit)}
                                    noValidate
                                    className="space-y-4"
                                >
                                    <Controller
                                        control={form.control}
                                        name="newPassword"
                                        render={({ field, fieldState }) => (
                                            <Field>
                                                <FieldLabel
                                                    htmlFor={field.name}
                                                    className="text-[12px] font-medium text-slate-700"
                                                >
                                                    New password
                                                </FieldLabel>

                                                <div className="relative">
                                                    <Input
                                                        {...field}
                                                        id={field.name}
                                                        type={
                                                            showPassword
                                                                ? 'text'
                                                                : 'password'
                                                        }
                                                        autoComplete="new-password"
                                                        aria-invalid={
                                                            fieldState.invalid
                                                        }
                                                        className={`${inputClassName} pr-10`}
                                                    />

                                                    <button
                                                        type="button"
                                                        onClick={() =>
                                                            setShowPassword(
                                                                (current) =>
                                                                    !current
                                                            )
                                                        }
                                                        aria-label={
                                                            showPassword
                                                                ? 'Hide password'
                                                                : 'Show password'
                                                        }
                                                        aria-pressed={
                                                            showPassword
                                                        }
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
                                                        errors={[
                                                            fieldState.error,
                                                        ]}
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
                                        disabled={
                                            form.formState.isSubmitting
                                        }
                                        className="mt-2 h-10 w-full rounded-lg bg-teal-700 text-[13px] font-medium text-white shadow-none hover:bg-teal-800 disabled:cursor-not-allowed disabled:opacity-60"
                                    >
                                        {form.formState.isSubmitting
                                            ? 'Updating...'
                                            : 'Reset password'}
                                    </Button>
                                </form>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </main>
    )
}