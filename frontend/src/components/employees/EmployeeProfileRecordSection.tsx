import type { EmployeeActivityDTO, EmployeeDetailsDTO } from '@/api/types'
import { Button } from '@/components/ui/button'
import { formatDate } from '@/lib/formatDate'
import {
    formatActivitySubtitle,
    formatActivityTimestamp,
} from './employeeActivityFormatters'

type Props = {
    employee: EmployeeDetailsDTO
    activities: EmployeeActivityDTO[]
    activityLoading: boolean
    activityError: string | null
}

function formatRange(startDate?: string | null, endDate?: string | null) {
    if (!startDate) return '—'

    return (
        <>
            {formatDate(startDate)}
            <span className="mx-1.5 text-slate-300">–</span>
            {endDate ? formatDate(endDate) : 'Open-ended'}
        </>
    )
}

function humanizeAction(action?: string | null) {
    if (!action) return 'Activity recorded'

    return action
        .toLowerCase()
        .replaceAll('_', ' ')
        .replace(/^./, c => c.toUpperCase())
}

export function EmployeeProfileRecordSection({
    employee,
    activities,
    activityLoading,
    activityError,
}: Props) {
    const activeAssignment = employee.assignments.find(
        assignment => assignment.status === 'ACTIVE'
    )

    const hasPlacement = Boolean(employee.activePlacement)

    const currentUnitName = employee.activePlacement?.houseUnitDisplayName
        ?? activeAssignment?.houseUnitDisplayName

    const currentMeta = employee.activePlacement
        ? (
            <>
                {employee.activePlacement.reasonDisplayName}
                <span className="mx-1.5 text-slate-300">·</span>
                {formatRange(
                    employee.activePlacement.startDate,
                    employee.activePlacement.endDate
                )}
            </>
        )
        : activeAssignment
            ? (
                <>
                    Official assignment
                    <span className="mx-1.5 text-slate-300">·</span>
                    {formatRange(activeAssignment.startDate, activeAssignment.endDate)}
                </>
            )
            : null

    return (
        <section className="max-w-2xl">
            <div className="grid grid-cols-[8.5rem_1fr] gap-x-8 gap-y-8">
                <div>
                    <p className="text-[12px] font-medium text-slate-400">
                        Current status
                    </p>
                </div>

                <div>
                    {!currentUnitName ? (
                        <p className="text-[13px] text-slate-400">
                            No current working unit
                        </p>
                    ) : (
                        <>
                            <p className="text-[14px] font-medium text-slate-900">
                                {currentUnitName}
                            </p>

                            <p className="mt-1 text-[12px] text-slate-500">
                                {currentMeta}
                            </p>
                        </>
                    )}
                </div>

                {hasPlacement && (
                    <>
                        <div>
                            <p className="text-[12px] font-medium text-slate-400">
                                Official assignment
                            </p>
                        </div>

                        <div>
                            {!activeAssignment ? (
                                <p className="text-[13px] text-slate-400">
                                    No active assignment
                                </p>
                            ) : (
                                <>
                                    <p className="text-[13px] font-medium text-slate-800">
                                        {activeAssignment.houseUnitDisplayName}
                                    </p>

                                    <p className="mt-0.5 text-[12px] text-slate-500">
                                        {formatRange(
                                            activeAssignment.startDate,
                                            activeAssignment.endDate
                                        )}
                                    </p>
                                </>
                            )}
                        </div>
                    </>
                )}

                <div>
                    <p className="text-[12px] font-medium text-slate-400">
                        Recent activity
                    </p>

                    <Button
                        variant="ghost"
                        size="sm"
                        disabled
                        className="-ml-1 mt-1 h-auto px-1 py-0 text-[11px] font-normal text-slate-400"
                    >
                        View full history
                    </Button>
                </div>

                <div>
                    {activityLoading && (
                        <p className="text-[13px] text-slate-400">
                            Loading activity…
                        </p>
                    )}

                    {!activityLoading && activityError && (
                        <p className="text-[13px] text-slate-400">
                            {activityError}
                        </p>
                    )}

                    {!activityLoading && !activityError && activities.length === 0 && (
                        <p className="text-[13px] text-slate-400">
                            No activity recorded yet.
                        </p>
                    )}

                    {!activityLoading && !activityError && activities.length > 0 && (
                        <div className="space-y-4">
                            {activities.slice(0, 3).map(activity => {
                                const subtitle = formatActivitySubtitle(activity)

                                return (
                                    <div key={activity.publicId}>
                                        <p className="text-[13px] font-medium text-slate-800">
                                            {humanizeAction(activity.action)}
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
                                )
                            })}
                        </div>
                    )}
                </div>
            </div>
        </section>
    )
}