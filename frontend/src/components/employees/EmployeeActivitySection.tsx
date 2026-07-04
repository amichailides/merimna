import { useState } from 'react'
import { AlertCircle, ArrowRight, ChevronDown, Loader2 } from 'lucide-react'

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
    formatActivityDateLabel,
    formatActivitySubtitle,
    formatActivityTimeOnly,
    formatActivityTitle,
    getActivityDetails,
} from './employeeActivityFormatters'

interface Props {
    activities: EmployeeActivityDTO[]
    loading: boolean
    error: string | null
}

interface ActivityRowProps {
    activity: EmployeeActivityDTO
    step: number
}

function EmployeeActivityRow({ activity, step }: ActivityRowProps) {
    const [expanded, setExpanded] = useState(false)

    const details = getActivityDetails(activity)
    const hasDetails = details.length > 0
    const subtitle = formatActivitySubtitle(activity)

    return (
        <TimelineItem step={step} className="ms-7">
            <TimelineHeader>
                <button
                    type="button"
                    onClick={() => hasDetails && setExpanded(current => !current)}
                    className={[
                        'flex w-full items-start justify-between gap-6 rounded-md px-2 py-1.5 -mx-2 -my-1.5 text-left transition-colors',
                        hasDetails
                            ? 'cursor-pointer hover:bg-teal-50/40'
                            : 'cursor-default',
                    ].join(' ')}
                    aria-expanded={hasDetails ? expanded : undefined}
                >
                    <div className="min-w-0">
                        <TimelineTitle className="text-[13px] font-medium leading-5 text-slate-800">
                            {formatActivityTitle(activity)}
                        </TimelineTitle>

                        {subtitle && (
                            <TimelineContent className="mt-0.5 text-[12px] leading-5 text-slate-500">
                                {subtitle}
                            </TimelineContent>
                        )}
                    </div>

                    <div className="flex shrink-0 items-center gap-2">
                        <TimelineDate className="mb-0 min-w-[2.5rem] text-right text-[12px] font-normal leading-5 tabular-nums text-slate-400">
                            {formatActivityTimeOnly(activity.occurredAt)}
                        </TimelineDate>

                        {hasDetails ? (
                            <ChevronDown
                                size={14}
                                className={[
                                    'size-[14px] shrink-0 text-slate-300 transition-transform',
                                    expanded ? 'rotate-180' : '',
                                ].join(' ')}
                            />
                        ) : (
                            <div className="size-[14px] shrink-0" aria-hidden="true" />
                        )}
                    </div>
                </button>

                {expanded && hasDetails && (
                    <div className="mt-3 space-y-2.5">
                        {details.map(detail => (
                            <div key={detail.label} className="text-[12px] leading-5">
                                <div className="font-medium text-slate-500">
                                    {detail.label}
                                </div>

                                {detail.value !== undefined && (
                                    <div className="mt-0.5 text-slate-600">
                                        {detail.value}
                                    </div>
                                )}

                                {(detail.before !== undefined || detail.after !== undefined) && (
                                    <div className="mt-0.5 flex items-center gap-2">
                                        <span className="text-slate-400 line-through">
                                            {detail.before ?? '—'}
                                        </span>
                                        <ArrowRight size={12} className="shrink-0 text-slate-300" />
                                        <span className="font-medium text-slate-700">
                                            {detail.after ?? '—'}
                                        </span>
                                    </div>
                                )}
                            </div>
                        ))}
                    </div>
                )}
            </TimelineHeader>

            <TimelineIndicator className="size-2 border border-slate-300 bg-white mt-[5px]" />

            <TimelineSeparator className="w-px bg-slate-100 h-[calc(100%-6.5px)] translate-y-[14px]" />
        </TimelineItem>
    )
}

function groupActivitiesByDate(activities: EmployeeActivityDTO[]) {
    const groups: { dateLabel: string; activities: EmployeeActivityDTO[] }[] = []

    for (const activity of activities) {
        const label = formatActivityDateLabel(activity.occurredAt)
        const current = groups.at(-1)

        if (current && current.dateLabel === label) {
            current.activities.push(activity)
        } else {
            groups.push({ dateLabel: label, activities: [activity] })
        }
    }

    return groups
}

export function EmployeeActivitySection({ activities, loading, error }: Props) {
    const groups = groupActivitiesByDate(activities)

    return (
        <section className="max-w-xl space-y-4">
            <div>
                <h2 className="text-[13px] font-medium text-slate-700">Activity</h2>
                <p className="mt-1 text-[12px] text-slate-400">
                    Audit history for profile, assignment, and placement changes.
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
                <div className="space-y-6">
                    {groups.map(group => (
                        <div key={group.dateLabel}>
                            <div className="mb-2 text-[11px] font-medium uppercase tracking-wide text-slate-400">
                                {group.dateLabel}
                            </div>

                            <Timeline>
                                {group.activities.map((activity, index) => (
                                    <EmployeeActivityRow
                                        key={activity.publicId}
                                        activity={activity}
                                        step={index + 1}
                                    />
                                ))}
                            </Timeline>
                        </div>
                    ))}
                </div>
            )}
        </section>
    )
}