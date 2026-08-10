import { useState } from 'react'
import axios from 'axios'
import { z } from 'zod'
import { Controller, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { Link, Navigate } from 'react-router-dom'

import { useAuth } from '@/auth/useAuth'
import { forgotPassword } from '@/api/authApi'
import type { ValidationErrorResponse } from '@/api/types'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'

const forgotPasswordSchema = z.object({
    email: z.email('Invalid email address'),
})

type ForgotPasswordFormValues = z.infer<
    typeof forgotPasswordSchema
>

const inputClassName = `
    h-10 rounded-lg border-slate-200 bg-white
    text-[13px] text-slate-900 shadow-none
    focus-visible:border-teal-600
    focus-visible:ring-teal-100
    aria-invalid:border-red-500
`

export function ForgotPasswordPage() {
    const { isAuthenticated, isAuthLoading } = useAuth()

    const [submitted, setSubmitted] = useState(false)
    const [submitError, setSubmitError] = useState<string | null>(null)

    const form = useForm<ForgotPasswordFormValues>({
        resolver: zodResolver(forgotPasswordSchema),
        defaultValues: {
            email: '',
        },
    })

    if (isAuthLoading) {
        return null
    }

    if (isAuthenticated) {
        return <Navigate to="/dashboard" replace />
    }

    async function onSubmit(values: ForgotPasswordFormValues) {
        setSubmitError(null)

        try {
            await forgotPassword({
                email: values.email,
            })

            setSubmitted(true)
        } catch (error) {
            if (axios.isAxiosError<ValidationErrorResponse>(error)) {
                const errorResponse = error.response?.data
                const validationErrors = errorResponse?.validationErrors
                const emailMessage = validationErrors?.email?.[0]

                if (emailMessage) {
                    form.setError('email', {
                        type: 'server',
                        message: emailMessage,
                    })

                    return
                }

                setSubmitError(
                    errorResponse?.detail ??
                    'Could not process the request. Please try again.'
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
                                Forgot password
                            </h1>

                            <p className="mt-1 text-[13px] leading-5 text-slate-500">
                                Enter your email address and we'll send you
                                instructions to reset your password.
                            </p>
                        </div>

                        {submitted ? (
                            <p className="text-[13px] leading-5 text-slate-600">
                                If an account exists for that email address,
                                password reset instructions have been sent.
                            </p>
                        ) : (
                            <form
                                onSubmit={form.handleSubmit(onSubmit)}
                                noValidate
                                className="space-y-4"
                            >
                                <Controller
                                    control={form.control}
                                    name="email"
                                    render={({ field, fieldState }) => (
                                        <Field>
                                            <FieldLabel
                                                htmlFor={field.name}
                                                className="text-[12px] font-medium text-slate-700"
                                            >
                                                Email
                                            </FieldLabel>

                                            <Input
                                                {...field}
                                                id={field.name}
                                                type="email"
                                                autoComplete="email"
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
                                        ? 'Sending...'
                                        : 'Send reset instructions'}
                                </Button>
                            </form>
                        )}
                    </div>

                    <div className="mt-4 text-center">
                        <Link
                            to="/login"
                            className="text-[12px] font-medium text-teal-700 hover:text-teal-800"
                        >
                            Back to sign in
                        </Link>
                    </div>

                    <p className="mt-4 text-center text-[12px] text-slate-400">
                        Demo environment · Merimna
                    </p>
                </div>
            </div>
        </main>
    )
}