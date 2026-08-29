import axios from 'axios'
import {
    Mail,
} from 'lucide-react'
import { useState, type SubmitEvent } from 'react'

import {
    cancelEmployeeInvitation,
    grantEmployeeAccess,
    resendEmployeeInvitation,
} from '@/api/employeeApi'
import type { EmployeeAccessDTO } from '@/api/types'

type Props = {
    employeePublicId: string
    access: EmployeeAccessDTO | null
    loading: boolean
    error: string | null
    onAccessUpdated?: () => void | Promise<void>
}

type AccessPresentation = {
    label: string
    dotClassName: string
}

function getAccessPresentation(
    access: EmployeeAccessDTO
): AccessPresentation {
    switch (access.status) {
        case 'ACTIVE':
            return {
                label: 'Login enabled',
                dotClassName: 'bg-emerald-500',
            }

        case 'SUSPENDED':
            return {
                label: 'Login suspended',
                dotClassName: 'bg-slate-400',
            }

        case 'INVITATION_PENDING':
            return {
                label: 'Invitation pending',
                dotClassName: 'bg-amber-500',
            }

        case 'INVITATION_EXPIRED':
            return {
                label: 'Invitation expired',
                dotClassName: 'bg-red-400',
            }

        case 'NO_ACCESS':
        default:
            return {
                label: 'No system access',
                dotClassName: 'bg-slate-300',
            }
    }
}

function formatExpirationDate(value?: string) {
    if (!value) {
        return null
    }

    return new Intl.DateTimeFormat('en-GB', {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
    }).format(new Date(value))
}

export function EmployeeSystemAccessSection({
    employeePublicId,
    access,
    loading,
    error,
    onAccessUpdated,
}: Props) {
    const [actionError, setActionError] =
        useState<string | null>(null)

    const [accountEmail, setAccountEmail] = useState('')

    const [isGranting, setIsGranting] = useState(false)
    const [isResending, setIsResending] = useState(false)
    const [isCancelling, setIsCancelling] = useState(false)

    async function handleGrant(
        event: SubmitEvent<HTMLFormElement>
    ) {
        event.preventDefault()

        setActionError(null)
        setIsGranting(true)

        try {
            await grantEmployeeAccess(
                employeePublicId,
                {
                    accountEmail: accountEmail.trim(),
                }
            )

            setAccountEmail('')
            await onAccessUpdated?.()
        } catch (error) {
            const detail = axios.isAxiosError<{
                detail?: string
            }>(error)
                ? error.response?.data?.detail
                : undefined

            setActionError(
                detail ??
                'Could not grant system access. Please try again.'
            )
        } finally {
            setIsGranting(false)
        }
    }

    async function handleResend() {
        setActionError(null)
        setIsResending(true)

        try {
            await resendEmployeeInvitation(employeePublicId)
            await onAccessUpdated?.()
        } catch (error) {
            const detail = axios.isAxiosError<{
                detail?: string
            }>(error)
                ? error.response?.data?.detail
                : undefined

            setActionError(
                detail ??
                'Could not resend the invitation. Please try again.'
            )
        } finally {
            setIsResending(false)
        }
    }

    async function handleCancel() {
        setActionError(null)
        setIsCancelling(true)

        try {
            await cancelEmployeeInvitation(employeePublicId)
            await onAccessUpdated?.()
        } catch (error) {
            const detail = axios.isAxiosError<{
                detail?: string
            }>(error)
                ? error.response?.data?.detail
                : undefined

            setActionError(
                detail ??
                'Could not cancel the invitation. Please try again.'
            )
        } finally {
            setIsCancelling(false)
        }
    }

    if (loading) {
        return (
            <section className="space-y-2">
                <h3 className="text-[12px] font-medium text-slate-700">
                    System access
                </h3>

                <p className="text-[12px] text-slate-400">
                    Loading access information…
                </p>
            </section>
        )
    }

    if (error) {
        return (
            <section className="space-y-2">
                <h3 className="text-[12px] font-medium text-slate-700">
                    System access
                </h3>

                <p className="text-[12px] text-red-500">
                    {error}
                </p>
            </section>
        )
    }

    if (!access) {
        return null
    }

    const presentation = getAccessPresentation(access)

    const expirationDate = formatExpirationDate(
        access.invitationExpiresAt
    )

    return (
        <section className="space-y-3">
            <h3 className="text-[12px] font-medium text-slate-700">
                System access
            </h3>

            <div className="space-y-2">
                <div className="flex items-center gap-1.5">
                    <span
                        className={[
                            'h-1.5 w-1.5 shrink-0 rounded-full',
                            presentation.dotClassName,
                        ].join(' ')}
                    />

                    <span className="text-[12px] font-medium text-slate-600">
                        {presentation.label}
                    </span>
                </div>

                {access.accountEmail && (
                    <div className="flex items-center gap-1.5 text-[12px] text-slate-500">
                        <Mail
                            size={13}
                            strokeWidth={2}
                            className="text-slate-400"
                        />

                        <span>{access.accountEmail}</span>
                    </div>
                )}

                {access.status === 'NO_ACCESS' && (
                    <p className="text-[12px] text-slate-400">
                        No user account is linked to this employee.
                    </p>
                )}

                {expirationDate &&
                    (access.status === 'INVITATION_PENDING' ||
                        access.status ===
                        'INVITATION_EXPIRED') && (
                        <p className="text-[11px] text-slate-400">
                            Expires {expirationDate}
                        </p>
                    )}
            </div>

            {access.status === 'NO_ACCESS' && (
                <form
                    onSubmit={(event) =>
                        void handleGrant(event)
                    }
                    className="space-y-2 pt-1"
                >
                    <input
                        type="email"
                        required
                        value={accountEmail}
                        onChange={(event) =>
                            setAccountEmail(
                                event.target.value
                            )
                        }
                        placeholder="Account email"
                        className="h-8 w-full rounded-md border border-slate-200 px-2.5 text-[12px] text-slate-700 outline-none transition-colors placeholder:text-slate-400 focus:border-slate-400"
                    />

                    <button
                        type="submit"
                        disabled={
                            isGranting ||
                            !accountEmail.trim()
                        }
                        className="rounded-lg border border-slate-200 px-2.5 py-1.5 text-[12px] font-medium text-slate-600 transition-colors hover:bg-slate-50 hover:text-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
                    >
                        {isGranting
                            ? 'Granting access...'
                            : 'Grant system access'}
                    </button>
                </form>
            )}

            {access.status === 'INVITATION_PENDING' && (
                <div className="flex items-center gap-2 pt-1">
                    <button
                        type="button"
                        disabled={
                            isResending || isCancelling
                        }
                        onClick={() => void handleResend()}
                        className="rounded-lg border border-slate-200 px-2.5 py-1.5 text-[12px] font-medium text-slate-600 transition-colors hover:bg-slate-50 hover:text-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
                    >
                        {isResending
                            ? 'Resending...'
                            : 'Resend invitation'}
                    </button>

                    <button
                        type="button"
                        disabled={
                            isResending || isCancelling
                        }
                        onClick={() => void handleCancel()}
                        className="rounded-lg px-2.5 py-1.5 text-[12px] font-medium text-red-500 transition-colors hover:bg-red-50 disabled:cursor-not-allowed disabled:opacity-60"
                    >
                        {isCancelling
                            ? 'Cancelling...'
                            : 'Cancel'}
                    </button>
                </div>
            )}

            {access.status === 'INVITATION_EXPIRED' && (
                <button
                    type="button"
                    disabled={isResending}
                    onClick={() => void handleResend()}
                    className="rounded-lg border border-slate-200 px-2.5 py-1.5 text-[12px] font-medium text-slate-600 transition-colors hover:bg-slate-50 hover:text-slate-800 disabled:cursor-not-allowed disabled:opacity-60"
                >
                    {isResending
                        ? 'Resending...'
                        : 'Resend invitation'}
                </button>
            )}

            {actionError && (
                <p
                    role="alert"
                    className="text-[12px] text-red-500"
                >
                    {actionError}
                </p>
            )}
        </section>
    )
}
