import {
    AlertCircle,
    Loader2,
} from 'lucide-react'

import type { EmployeeActivityDTO } from '@/api/types'
import {
    Timeline,
    TimelineContent,
    TimelineDate,
    TimelineHeader,
    TimelineIndicator,
    TimelineItem,
    TimelineSeparator,
    TimelineTitle,
} from '@/components/reui/timeline'
import {
    formatActivitySubtitle,
    formatActivityTimestamp,
} from './employeeActivityFormatters'

type AuditAction = NonNullable<EmployeeActivityDTO['action']>

const ACTION_META: Partial<Record<AuditAction, { label: string }>> = {
    EMPLOYEE_CREATED: { label: 'Employee created' },
    EMPLOYEE_UPDATED: { label: 'Employee updated' },
    EMPLOYEE_TERMINATED: { label: 'Employment terminated' },
    EMPLOYEE_REACTIVATED: { label: 'Employee reactivated' },
    ASSIGNMENT_CREATED: { label: 'Assignment created' },
    ASSIGNMENT_TERMINATED: { label: 'Assignment terminated' },
    ASSIGNMENT_CANCELLED: { label: 'Assignment cancelled' },
    PLACEMENT_CREATED: { label: 'Placement started' },
    PLACEMENT_TERMINATED: { label: 'Placement ended' },
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
                <div className="flex items-center gap-2">

                    <h2 className="text-[13px] font-medium text-slate-700">
                        Recent activity
                    </h2>
                </div>

                <p className="mt-1 text-[12px] text-slate-400">
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
                <Timeline defaultValue={0} className="mt-4">
                    {activities.map((activity, index) => {
                        const action = activity.action

                        const meta = action
                            ? ACTION_META[action] ?? {
                                label: humanizeAction(action),
                            }
                            : {
                                label: 'Activity recorded',
                            }

                        const subtitle = formatActivitySubtitle(activity)

                        return (
                            <TimelineItem
                                key={activity.publicId}
                                step={index + 1}
                                className="ms-7"
                            >
                                <TimelineHeader>
                                    <TimelineTitle className="text-[14.5px] font-semibold leading-5 text-slate-950/80">
                                        {meta.label}
                                    </TimelineTitle>

                                    {subtitle && (
                                        <TimelineContent className="text-[13px] leading-5 text-slate-500">
                                            {subtitle}
                                        </TimelineContent>
                                    )}

                                    <TimelineDate className="mb-0 text-[12px] font-normal leading-5 text-slate-400">
                                        {formatActivityTimestamp(activity.occurredAt)}
                                    </TimelineDate>
                                </TimelineHeader>

                                <TimelineIndicator
                                    className={[
                                        'size-2 border mt-[5px]',
                                        index === 0
                                            ? 'border-teal-500 bg-teal-500'
                                            : 'border-slate-300 bg-white',
                                    ].join(' ')}
                                />

                                <TimelineSeparator className="w-px bg-slate-100 h-[calc(100%-6.5px)] translate-y-[14px]" />
                            </TimelineItem>
                        )
                    })}
                </Timeline>
            )}
        </section>
    )
}