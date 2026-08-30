import axios from 'axios'
import { z } from 'zod'
import { useState } from 'react'
import { Eye, EyeOff } from 'lucide-react'
import { Controller, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import { useTranslation } from 'react-i18next'
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { Link, useNavigate } from 'react-router-dom'
import { useAuth } from '@/auth/useAuth'
import type { ValidationErrorResponse } from '@/api/types'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'

const loginSchema = z.object({
    email: z.email('validation.email.invalid'),
    password: z.string().min(1, 'validation.password.required'),
})

type LoginFormValues = z.infer<typeof loginSchema>

const inputClassName = `
    h-10 rounded-lg border-slate-200 bg-white
    text-[13px] text-slate-900 shadow-none
    focus-visible:border-teal-600
    focus-visible:ring-teal-100
    aria-invalid:border-red-500
`

export function LoginPage() {
    const navigate = useNavigate()
    const { t } = useTranslation()
    const { login, isAuthenticated, isAuthLoading } = useAuth()
    const [showPassword, setShowPassword] = useState(false)
    const [submitErrorKey, setSubmitErrorKey] = useState<string | null>(null)

    const form = useForm<LoginFormValues>({
        resolver: zodResolver(loginSchema),
        defaultValues: {
            email: '',
            password: '',
        },
    })

    async function onSubmit(values: LoginFormValues) {
        setSubmitErrorKey(null)

        try {
            await login(values)
            navigate('/dashboard', { replace: true })
        } catch (error) {
            if (axios.isAxiosError<ValidationErrorResponse>(error)) {
                const errorType = error.response?.data?.type

                if (errorType === 'INVALID_CREDENTIALS') {
                    setSubmitErrorKey('auth.errors.invalidCredentials')
                    return
                }
            }

            setSubmitErrorKey('auth.errors.generic')
        }
    }

    if (isAuthLoading) {
        return null
    }

    if (isAuthenticated) {
        navigate('/dashboard', { replace: true })
        return null
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
                            {t('app.tagline')}
                        </p>
                    </div>

                    <Card className="w-full rounded-2xl border-teal-100 bg-white shadow-[0_1px_2px_rgba(15,23,42,0.04)]">
                        <CardHeader className="space-y-1 px-6 pt-6 pb-5">
                            <CardTitle className="text-[18px] font-medium tracking-[-0.01em] text-slate-950">
                                {t('auth.login.title')}
                            </CardTitle>

                            <CardDescription className="text-[13px] leading-5 text-slate-500">
                                {t('auth.login.description')}
                            </CardDescription>
                        </CardHeader>

                        <CardContent className="px-6 pb-6">
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
                                                {t('auth.login.email')}
                                            </FieldLabel>

                                            <Input
                                                {...field}
                                                id={field.name}
                                                type="email"
                                                autoComplete="email"
                                                aria-invalid={fieldState.invalid}
                                                className={inputClassName}
                                            />

                                            {fieldState.invalid &&
                                                fieldState.error?.message && (
                                                    <FieldError>
                                                        {t(fieldState.error.message)}
                                                    </FieldError>
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
                                                {t('auth.login.password')}
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
                                                    autoComplete="current-password"
                                                    aria-invalid={fieldState.invalid}
                                                    className={`${inputClassName} pr-10`}
                                                />

                                                <button
                                                    type="button"
                                                    onClick={() =>
                                                        setShowPassword(
                                                            (current) => !current,
                                                        )
                                                    }
                                                    aria-label={
                                                        showPassword
                                                            ? t(
                                                                'auth.login.hidePassword',
                                                            )
                                                            : t(
                                                                'auth.login.showPassword',
                                                            )
                                                    }
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

                                            {fieldState.invalid &&
                                                fieldState.error?.message && (
                                                    <FieldError>
                                                        {t(fieldState.error.message)}
                                                    </FieldError>
                                                )}
                                        </Field>
                                    )}
                                />

                                <div className="flex justify-end">
                                    <Link
                                        to="/forgot-password"
                                        className="text-[12px] font-medium text-teal-700 hover:text-teal-800"
                                    >
                                        {t('auth.login.forgotPassword')}
                                    </Link>
                                </div>

                                {submitErrorKey && (
                                    <p
                                        role="alert"
                                        className="text-[12px] text-red-600"
                                    >
                                        {t(submitErrorKey)}
                                    </p>
                                )}

                                <Button
                                    type="submit"
                                    className="mt-2 h-10 w-full rounded-lg bg-teal-700 text-[13px] font-medium text-white shadow-none hover:bg-teal-800"
                                >
                                    {t('auth.login.submit')}
                                </Button>
                            </form>
                        </CardContent>
                    </Card>

                    <p className="mt-4 text-center text-[12px] text-slate-400">
                        {t('app.demoEnvironment')}
                    </p>
                </div>
            </div>
        </main>
    )
}
