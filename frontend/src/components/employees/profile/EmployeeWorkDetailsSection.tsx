import type { ReactNode } from 'react'
import {
    BriefcaseBusiness,
    MapPin,
} from 'lucide-react'

import type { EmployeeDetailsDTO } from '@/api/types'
import { formatDateRange } from '@/lib/formatDate'

type Assignment = EmployeeDetailsDTO['assignments'][number]
type Placement = NonNullable<EmployeeDetailsDTO['activePlacement']>

type EmployeeWorkDetailsSectionProps = {
    assignments: EmployeeDetailsDTO['assignments']
    placement?: EmployeeDetailsDTO['activePlacement']
}

function getActiveAssignment(assignments: Assignment[]) {
    return assignments.find((assignment) => assignment.status === 'ACTIVE')
        ?? null
}

function WorkContextItem({
    icon,
    label,
    badge,
    title,
    description,
    muted = false,
}: {
    icon: ReactNode
    label: string
    badge?: string
    title: string
    description?: string
    muted?: boolean
}) {
    return (
        <div className="flex gap-3">
            <div className="mt-[1px] flex h-3 w-4 shrink-0 items-center justify-center text-slate-400">
                {icon}
            </div>

            <div className="min-w-0 space-y-1.5">
                <div className="flex flex-wrap items-center gap-2">
                    <p className="text-[11px] font-semibold uppercase tracking-[0.04em] text-slate-500">
                        {label}
                    </p>

                    {badge && (
                        <span className="rounded-full bg-teal-50 px-2 py-0.5 text-[11px] font-medium leading-none text-teal-700">
                            {badge}
                        </span>
                    )}
                </div>

                <p
                    className={
                        muted
                            ? 'text-[14px] font-normal leading-5 text-slate-400'
                            : 'text-[14px] font-semibold leading-5 text-slate-950/80'
                    }
                >
                    {title}
                </p>

                {description && (
                    <p className="text-[13px] leading-5 text-slate-500">
                        {description}
                    </p>
                )}
            </div>
        </div>
    )
}

function getPlacementDescription(placement: Placement) {
    const reason = placement.reasonDisplayName ?? 'Temporary placement'
    const dates = formatDateRange(
        placement.startDate,
        placement.endDate,
        'Open-ended',
    )

    return `${reason} · ${dates}`
}

function getAssignmentDescription(assignment: Assignment) {
    return `Active assignment · ${formatDateRange(
        assignment.startDate,
        assignment.endDate,
        'Open-ended',
    )}`
}

export function EmployeeWorkDetailsSection({
    assignments,
    placement,
}: EmployeeWorkDetailsSectionProps) {
    const activeAssignment = getActiveAssignment(assignments)
    const hasActiveAssignment = activeAssignment !== null

    const currentUnitName = placement
        ? placement.houseUnitDisplayName ?? 'Unknown unit'
        : activeAssignment?.houseUnitDisplayName ?? null

    return (
        <section className="max-w-xl space-y-5">
            <h2 className="text-[13px] font-medium text-slate-700">
                Work context
            </h2>

            <div className="space-y-6">
                {currentUnitName ? (
                    <WorkContextItem
                        icon={<MapPin size={15} strokeWidth={1.75} />}
                        label="Current work location"
                        badge={placement ? 'Temporary placement' : 'Official assignment'}
                        title={currentUnitName}
                        description={
                            placement
                                ? getPlacementDescription(placement)
                                : activeAssignment
                                    ? getAssignmentDescription(activeAssignment)
                                    : undefined
                        }
                    />
                ) : (
                    <WorkContextItem
                        icon={<MapPin size={15} strokeWidth={1.75} />}
                        label="Current work location"
                        title="No current work unit"
                        description="This employee does not have an active assignment or placement."
                        muted
                    />
                )}

                {placement ? (
                    activeAssignment ? (
                        <WorkContextItem
                            icon={<BriefcaseBusiness size={15} strokeWidth={1.75} />}
                            label="Official home unit"
                            title={activeAssignment.houseUnitDisplayName ?? 'Unknown unit'}
                            description={getAssignmentDescription(activeAssignment)}
                        />
                    ) : (
                        <WorkContextItem
                            icon={<BriefcaseBusiness size={15} strokeWidth={1.75} />}
                            label="Official home unit"
                            title="No active assignment"
                            description="There is no official home unit assigned to this employee."
                            muted
                        />
                    )
                ) : (
                    <WorkContextItem
                        icon={<BriefcaseBusiness size={15} strokeWidth={1.75} />}
                        label="Official home unit"
                        title={
                            hasActiveAssignment
                                ? activeAssignment.houseUnitDisplayName ?? 'Unknown unit'
                                : 'No active assignment'
                        }
                        description={
                            hasActiveAssignment
                                ? 'Same unit as current work location.'
                                : undefined
                        }
                        muted={!hasActiveAssignment}
                    />
                )}
            </div>
        </section>
    )
}