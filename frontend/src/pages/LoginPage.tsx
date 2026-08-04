import { z } from 'zod'
import { Controller, useForm } from 'react-hook-form'
import { zodResolver } from '@hookform/resolvers/zod'
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from '@/components/ui/card'
import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import { useNavigate } from 'react-router-dom'
import { useAuth } from '@/auth/useAuth'
import {
    Field,
    FieldError,
    FieldLabel,
} from '@/components/ui/field'

const loginSchema = z.object({
    email: z.email('Invalid email address'),
    password: z.string().min(1, 'Password is required'),
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
    const { login } = useAuth()

    const form = useForm<LoginFormValues>({
        resolver: zodResolver(loginSchema),
        defaultValues: {
            email: '',
            password: '',
        },
    })

    async function onSubmit(values: LoginFormValues) {
        await login(values)
        navigate('/dashboard', { replace: true })
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

                    <Card className="w-full rounded-2xl border-teal-100 bg-white shadow-[0_1px_2px_rgba(15,23,42,0.04)]">
                        <CardHeader className="space-y-1 px-6 pt-6 pb-5">
                            <CardTitle className="text-[18px] font-medium tracking-[-0.01em] text-slate-950">
                                Sign in
                            </CardTitle>

                            <CardDescription className="text-[13px] leading-5 text-slate-500">
                                Use your Merimna account to continue.
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
                                                autoComplete="current-password"
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
                                    Sign in
                                </Button>
                            </form>
                        </CardContent>
                    </Card>

                    <p className="mt-4 text-center text-[12px] text-slate-400">
                        Demo environment · Merimna
                    </p>
                </div>
            </div>
        </main>
    )
}