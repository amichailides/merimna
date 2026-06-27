import type { ReactNode } from 'react'
import {
    BriefcaseBusiness,
    MapPin,
} from 'lucide-react'

import type { EmployeeDetailsDTO } from '@/api/types'
import { formatDate } from '@/lib/formatDate'

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

function formatDateRange(startDate?: string | null, endDate?: string | null) {
    const start = startDate ? formatDate(startDate) : '—'
    const end = endDate ? formatDate(endDate) : 'Open-ended'

    return `${start} → ${end}`
}

function WorkContextItem({
    icon,
    label,
    badge,
    title,
    description,
}: {
    icon: ReactNode
    label: string
    badge?: string
    title: string
    description?: string
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

                <p className="text-[14.5px] font-semibold leading-5 text-slate-950/90">
                    {title}
                </p>

                {description && (
                    <p className="text-[13px] leading-5 text-slate-500/70">
                        {description}
                    </p>
                )}
            </div>
        </div>
    )
}

function getPlacementDescription(placement: Placement) {
    const reason = placement.reasonDisplayName ?? 'Temporary placement'
    const dates = formatDateRange(placement.startDate, placement.endDate)

    return `${reason} · ${dates}`
}

function getAssignmentDescription(assignment: Assignment) {
    return `Active assignment · ${formatDateRange(
        assignment.startDate,
        assignment.endDate
    )}`
}

export function EmployeeWorkDetailsSection({
    assignments,
    placement,
}: EmployeeWorkDetailsSectionProps) {
    const activeAssignment = getActiveAssignment(assignments)

    const currentUnitName = placement
        ? placement.houseUnitDisplayName ?? 'Unknown unit'
        : activeAssignment?.houseUnitDisplayName ?? null
    return (
        <section className="max-w-md space-y-5">
            <h2 className="text-[13px] font-medium text-slate-700">
                Work context
            </h2>

            <div className="space-y-7">
                {currentUnitName ? (
                    <WorkContextItem
                        icon={<MapPin size={14} strokeWidth={1.75} />}
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
                        icon={<MapPin size={13} strokeWidth={1.75} />}
                        label="Current work location"
                        title="No current work unit"
                        description="This employee does not have an active assignment or placement."
                    />
                )}

                <div className="pt-2">
                    {placement ? (
                        activeAssignment ? (
                            <WorkContextItem
                                icon={<BriefcaseBusiness size={13} strokeWidth={1.75} />}
                                label="Official home unit"
                                title={activeAssignment.houseUnitDisplayName ?? 'Unknown unit'}
                                description={getAssignmentDescription(activeAssignment)}
                            />
                        ) : (
                            <WorkContextItem
                                icon={<BriefcaseBusiness size={13} strokeWidth={1.75} />}
                                label="Official home unit"
                                title="No active assignment"
                                description="There is no official home unit assigned to this employee."
                            />
                        )
                    ) : (
                        <div className="flex gap-3">
                            <div className="mt-[2px] flex h-5 w-5 shrink-0 items-center justify-center text-slate-400">
                                <BriefcaseBusiness size={13} strokeWidth={1.75} />
                            </div>

                            <div className="min-w-0 space-y-1.5">
                                <p className="text-[10.5px] font-semibold uppercase tracking-[0.08em] text-slate-400">
                                    Official home unit
                                </p>

                                <p className="text-[13px] font-medium leading-5 text-slate-700">
                                    {activeAssignment?.houseUnitDisplayName ?? 'Unknown unit'}
                                </p>

                                <p className="text-[12px] leading-5 text-slate-400">
                                    Same unit as current work location.
                                </p>
                            </div>
                        </div>
                    )}
                </div>
            </div>
        </section>
    )
}