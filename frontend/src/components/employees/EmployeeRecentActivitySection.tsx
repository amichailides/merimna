import type { ReactNode } from 'react'
import {
    Activity,
    AlertCircle,
    ArrowRightLeft,
    Building2,
    Clock,
    Loader2,
    RefreshCw,
    UserCog,
    UserMinus,
    UserPlus,
} from 'lucide-react'

import { Button } from '@/components/ui/button'
import type { EmployeeActivityDTO } from '@/api/types'
import {
    formatActivitySubtitle,
    formatActivityTimestamp,
} from './employeeActivityFormatters'

type AuditAction = NonNullable<EmployeeActivityDTO['action']>

const ACTION_META: Partial<Record<AuditAction, { label: string; icon: ReactNode }>> = {
    EMPLOYEE_CREATED: { label: 'Employee created', icon: <UserPlus size={11} /> },
    EMPLOYEE_UPDATED: { label: 'Employee updated', icon: <UserCog size={11} /> },
    EMPLOYEE_TERMINATED: { label: 'Employment terminated', icon: <UserMinus size={11} /> },
    EMPLOYEE_REACTIVATED: { label: 'Employee reactivated', icon: <RefreshCw size={11} /> },
    ASSIGNMENT_CREATED: { label: 'Assignment created', icon: <Building2 size={11} /> },
    ASSIGNMENT_TERMINATED: { label: 'Assignment terminated', icon: <Building2 size={11} /> },
    ASSIGNMENT_CANCELLED: { label: 'Assignment cancelled', icon: <Building2 size={11} /> },
    PLACEMENT_CREATED: { label: 'Placement started', icon: <ArrowRightLeft size={11} /> },
    PLACEMENT_TERMINATED: { label: 'Placement ended', icon: <ArrowRightLeft size={11} /> },
}

function humanizeAction(action: string): string {
    return action
        .toLowerCase()
        .replaceAll('_', ' ')
        .replace(/^./, c => c.toUpperCase())
}

interface Props {
    activities: EmployeeActivityDTO[]
    loading: boolean
    error: string | null
}

export function EmployeeRecentActivitySection({ activities, loading, error }: Props) {
    return (
        <section className="max-w-xl space-y-3">
            <div className="flex items-start gap-2">
                <span className="mt-0.5 text-slate-400">
                    <Clock size={13} />
                </span>

                <div className="min-w-0">
                    <div className="flex items-center gap-3">
                        <p className="text-[13px] font-medium text-slate-900">
                            Recent activity
                        </p>

                        <Button
                            variant="ghost"
                            size="sm"
                            disabled
                            className="h-auto px-1 py-0 text-[11px] font-normal text-slate-400"
                        >
                            View full history
                        </Button>
                    </div>

                    <p className="text-[11px] text-slate-400">
                        Latest changes and updates related to this employee.
                    </p>
                </div>
            </div>

            <div>
                {loading && (
                    <div className="flex items-center gap-2 py-2 pl-[21px] text-slate-400">
                        <Loader2 size={13} className="animate-spin" />
                        <span className="text-[13px]">Loading activity…</span>
                    </div>
                )}

                {!loading && error && (
                    <div className="flex items-center gap-2 py-2 pl-[21px] text-slate-400">
                        <AlertCircle size={13} />
                        <span className="text-[13px]">{error}</span>
                    </div>
                )}

                {!loading && !error && activities.length === 0 && (
                    <p className="py-2 pl-[21px] text-[13px] text-slate-400">
                        No activity recorded yet.
                    </p>
                )}

                {!loading && !error && activities.length > 0 && (
                    <div className="relative mt-4 ml-6">
                        <div className="absolute bottom-3 left-2.5 top-3 w-px bg-slate-100" />

                        <div className="space-y-3.5">
                            {activities.map(activity => {
                                const action = activity.action

                                const meta = action
                                    ? ACTION_META[action] ?? {
                                        label: humanizeAction(action),
                                        icon: <Activity size={12} />,
                                    }
                                    : {
                                        label: 'Activity recorded',
                                        icon: <Activity size={12} />,
                                    }

                                const subtitle = formatActivitySubtitle(activity)

                                return (
                                    <div
                                        key={activity.publicId}
                                        className="relative flex items-start gap-3.5"
                                    >
                                        <div className="relative z-10 mt-0.5 flex size-5 shrink-0 items-center justify-center rounded-full border border-teal-100 bg-teal-50 text-teal-600">
                                            {meta.icon}
                                        </div>

                                        <div className="min-w-0 flex-1 pt-0.5">
                                            <p className="text-[13px] font-medium text-slate-800">
                                                {meta.label}
                                            </p>

                                            {subtitle && (
                                                <p className="mt-0.5 text-[12px] leading-5 text-slate-500">
                                                    {subtitle}
                                                </p>
                                            )}

                                            <time className="mt-0.5 block text-[11px] text-slate-400">
                                                {formatActivityTimestamp(activity.occurredAt)}
                                            </time>
                                        </div>
                                    </div>
                                )
                            })}
                        </div>
                    </div>
                )}
            </div>
        </section>
    )
}