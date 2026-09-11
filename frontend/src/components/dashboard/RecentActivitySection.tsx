import { Link } from 'react-router-dom'

import type { DashboardActivityReadOnlyDTO } from '@/api/types'
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
    formatActivityDate,
    getMetadataString,
} from './dashboardHelpers'

type RecentActivitySectionProps = {
    recentActivity: DashboardActivityReadOnlyDTO[]
}

export function RecentActivitySection({
    recentActivity,
}: RecentActivitySectionProps) {
    return (
        <section className="space-y-4">
            <div>
                <h2 className="text-base font-semibold text-slate-900">
                    Recent activity
                </h2>

                <p className="mt-1 text-sm text-slate-500">
                    Latest operational changes across the organization.
                </p>
            </div>

            {recentActivity.length === 0 ? (
                <p className="py-2 text-[13px] text-slate-400">
                    No activity recorded yet.
                </p>
            ) : (
                <Timeline defaultValue={0}>
                    {recentActivity.map((activity, index) => {
                        const target = getActivityTarget(activity)
                        const subtitle = formatActivitySubtitle(activity)

                        const content = (
                            <TimelineHeader>
                                <TimelineTitle className="text-[14.5px] font-semibold leading-5 text-slate-950/80">
                                    {formatAuditAction(activity.action)}
                                </TimelineTitle>

                                {subtitle && (
                                    <TimelineContent className="text-[13px] leading-5 text-slate-500">
                                        {subtitle}
                                    </TimelineContent>
                                )}

                                <TimelineDate className="mb-0 text-[12px] font-normal leading-5 text-slate-400">
                                    {formatActivityDate(activity.occurredAt)}
                                </TimelineDate>
                            </TimelineHeader>
                        )

                        return (
                            <TimelineItem
                                key={activity.publicId}
                                step={index + 1}
                                className="ms-7"
                            >
                                {target ? (
                                    <Link
                                        to={target}
                                        className="block rounded-sm transition-colors hover:text-teal-700"
                                    >
                                        {content}
                                    </Link>
                                ) : (
                                    content
                                )}

                                <TimelineIndicator
                                    className={[
                                        'mt-[5px] size-2 border',
                                        index === 0
                                            ? 'border-teal-500 bg-teal-500'
                                            : 'border-slate-300 bg-white',
                                    ].join(' ')}
                                />

                                <TimelineSeparator className="h-[calc(100%-6.5px)] w-px translate-y-[14px] bg-slate-100" />
                            </TimelineItem>
                        )
                    })}
                </Timeline>
            )}
        </section>
    )
}

function formatActivitySubtitle(
    activity: DashboardActivityReadOnlyDTO,
): string | null {
    const entityType = formatEntityType(activity.entityType)

    if (activity.subjectName) {
        return entityType
            ? `${entityType} · ${activity.subjectName}`
            : activity.subjectName
    }

    return entityType || null
}

function getActivityTarget(
    activity: DashboardActivityReadOnlyDTO,
): string | null {
    switch (activity.entityType) {
        case 'EMPLOYEE':
            return activity.entityPublicId
                ? `/employees/${activity.entityPublicId}`
                : null

        case 'BENEFICIARY':
            return activity.entityPublicId
                ? `/beneficiaries/${activity.entityPublicId}`
                : null

        case 'MEDICATION': {
            const beneficiaryPublicId = getMetadataString(
                activity.metadata,
                'beneficiaryPublicId',
            )

            return beneficiaryPublicId
                ? `/beneficiaries/${beneficiaryPublicId}`
                : null
        }

        case 'EMPLOYEE_ASSIGNMENT':
        case 'EMPLOYEE_PLACEMENT': {
            const employeePublicId = getMetadataString(
                activity.metadata,
                'employeePublicId',
            )

            return employeePublicId
                ? `/employees/${employeePublicId}`
                : null
        }

        default:
            return null
    }
}

function formatAuditAction(action?: string): string {
    if (!action) {
        return 'Activity'
    }

    return action
        .toLowerCase()
        .split('_')
        .map(
            (word) =>
                word.charAt(0).toUpperCase() + word.slice(1),
        )
        .join(' ')
}

function formatEntityType(entityType?: string): string {
    if (!entityType) {
        return ''
    }

    return entityType
        .toLowerCase()
        .split('_')
        .map(
            (word) =>
                word.charAt(0).toUpperCase() + word.slice(1),
        )
        .join(' ')
}
