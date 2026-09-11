import type { DashboardSecurityEventReadOnlyDTO } from '@/api/types'

import {
    formatActivityDate,
    getMetadataString,
} from './dashboardHelpers'

type SecurityEventsSectionProps = {
    securityEvents: DashboardSecurityEventReadOnlyDTO[]
}

export function SecurityEventsSection({
    securityEvents,
}: SecurityEventsSectionProps) {
    return (
        <section className="space-y-4">
            <div>
                <h2 className="text-base font-semibold text-slate-900">
                    Security events
                </h2>

                <p className="mt-1 text-sm text-slate-500">
                    Recent authentication and account security events.
                </p>
            </div>

            {securityEvents.length === 0 ? (
                <p className="py-2 text-[13px] text-slate-400">
                    No security events recorded.
                </p>
            ) : (
                <div className="divide-y divide-slate-100">
                    {securityEvents.map((event) => (
                        <SecurityEventRow
                            key={event.publicId}
                            event={event}
                        />
                    ))}
                </div>
            )}
        </section>
    )
}

type SecurityEventRowProps = {
    event: DashboardSecurityEventReadOnlyDTO
}

function SecurityEventRow({ event }: SecurityEventRowProps) {
    const detail = getSecurityEventDetail(event)

    return (
        <div className="flex gap-3 py-3.5">
            <div
                className={[
                    'mt-[6px] size-2 shrink-0 rounded-full',
                    getSecurityIndicatorClass(event.action),
                ].join(' ')}
            />

            <div className="flex min-w-0 flex-1 items-start justify-between gap-4">
                <div className="min-w-0">
                    <div className="text-[14px] font-semibold leading-5 text-slate-900">
                        {formatSecurityAction(event.action)}
                    </div>

                    {detail && (
                        <div className="mt-0.5 text-[12px] leading-5 text-slate-500">
                            {detail}
                        </div>
                    )}
                </div>

                <div className="shrink-0 pt-0.5 text-right text-[12px] leading-5 text-slate-400">
                    {formatActivityDate(event.occurredAt)}
                </div>
            </div>
        </div>
    )
}

function getSecurityEventDetail(
    event: DashboardSecurityEventReadOnlyDTO,
): string | null {
    switch (event.action) {
        case 'AUTH_LOGIN_FAILED': {
            const attemptedEmail = getMetadataString(
                event.metadata,
                'attemptedEmail',
            )

            return attemptedEmail
                ? `Attempted login: ${attemptedEmail}`
                : null
        }

        default:
            return null
    }
}

function getSecurityIndicatorClass(action?: string): string {
    switch (action) {
        case 'AUTH_LOGIN_FAILED':
        case 'AUTH_REFRESH_TOKEN_REUSE_DETECTED':
            return 'bg-amber-500'

        case 'AUTH_PASSWORD_CHANGED':
        case 'AUTH_PASSWORD_RESET':
            return 'bg-teal-500'

        default:
            return 'bg-slate-300'
    }
}

function formatSecurityAction(action?: string): string {
    switch (action) {
        case 'AUTH_LOGIN_FAILED':
            return 'Failed login attempt'

        case 'AUTH_REFRESH_TOKEN_REUSE_DETECTED':
            return 'Refresh token reuse detected'

        case 'AUTH_PASSWORD_CHANGED':
            return 'Password changed'

        case 'AUTH_PASSWORD_RESET':
            return 'Password reset'

        default:
            return 'Security event'
    }
}
