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

const ACTION_META: Partial<Record<AuditAction, { label: string; icon: typeof Activity }>> = {
    EMPLOYEE_CREATED: { label: 'Employee created', icon: UserPlus },
    EMPLOYEE_UPDATED: { label: 'Employee updated', icon: UserCog },
    EMPLOYEE_TERMINATED: { label: 'Employment terminated', icon: UserMinus },
    EMPLOYEE_REACTIVATED: { label: 'Employee reactivated', icon: RefreshCw },
    ASSIGNMENT_CREATED: { label: 'Assignment created', icon: Building2 },
    ASSIGNMENT_TERMINATED: { label: 'Assignment terminated', icon: Building2 },
    ASSIGNMENT_CANCELLED: { label: 'Assignment cancelled', icon: Building2 },
    PLACEMENT_CREATED: { label: 'Placement started', icon: ArrowRightLeft },
    PLACEMENT_TERMINATED: { label: 'Placement ended', icon: ArrowRightLeft },
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
        <section className="max-w-xl space-y-4">
            <div>
                <div className="flex items-center justify-between gap-4">
                    <div className="flex items-center gap-2">
                        <Clock size={13} className="text-slate-400" />

                        <h2 className="text-[13px] font-medium text-slate-900">
                            Recent activity
                        </h2>
                    </div>

                    <Button
                        variant="ghost"
                        size="sm"
                        disabled
                        className="h-auto px-1 py-0 text-[11px] font-normal text-slate-400"
                    >
                        View full history
                    </Button>
                </div>

                <p className="mt-1 pl-5 text-[11px] text-slate-400">
                    Latest changes and updates related to this employee.
                </p>
            </div>

            {loading && (
                <div className="flex items-center gap-2 py-2 text-slate-400">
                    <Loader2 size={13} className="animate-spin" />
                    <span className="text-[13px]">Loading activity…</span>
                </div>
            )}

            {!loading && error && (
                <div className="flex items-center gap-2 py-2 text-slate-400">
                    <AlertCircle size={13} />
                    <span className="text-[13px]">{error}</span>
                </div>
            )}

            {!loading && !error && activities.length === 0 && (
                <p className="py-2 text-[13px] text-slate-400">
                    No activity recorded yet.
                </p>
            )}

            {!loading && !error && activities.length > 0 && (
                <div className="mt-3 space-y-3 pl-5">
                    {activities.map(activity => {
                        const action = activity.action

                        const meta = action
                            ? ACTION_META[action] ?? {
                                label: humanizeAction(action),
                                icon: Activity,
                            }
                            : {
                                label: 'Activity recorded',
                                icon: Activity,
                            }

                        const Icon = meta.icon
                        const subtitle = formatActivitySubtitle(activity)

                        return (
                            <div
                                key={activity.publicId}
                                className="grid grid-cols-[1rem_minmax(0,1fr)] items-start gap-3"
                            >
                                <Icon
                                    size={12}
                                    className="mt-0.5 text-slate-400"
                                />

                                <div className="min-w-0">
                                    <p className="text-[13px] font-medium text-slate-900">
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
            )}
        </section>
    )
}