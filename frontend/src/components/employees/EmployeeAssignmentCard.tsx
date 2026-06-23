import type { EmployeeDetailsDTO } from '@/api/types'
import { formatDate } from '@/lib/formatDate'
import { BriefcaseBusiness } from 'lucide-react'

type EmployeeAssignmentCardProps = {
    assignments: EmployeeDetailsDTO['assignments']
    isCurrentWorkingUnit?: boolean
}

export function EmployeeAssignmentCard({
    assignments,
    isCurrentWorkingUnit = false,
}: EmployeeAssignmentCardProps) {
    const activeAssignment = assignments.find(
        (assignment) => assignment.status === 'ACTIVE'
    )

    return (
        <section className="space-y-2.5">
            <div className="flex items-center gap-2">
                <BriefcaseBusiness size={13} className="shrink-0 text-slate-400" />

                <h3 className="text-[13px] font-medium text-slate-800">
                    Official home unit
                </h3>

                {isCurrentWorkingUnit && activeAssignment && (
                    <span className="rounded-full bg-teal-50 px-2 py-0.5 text-[11px] font-medium text-teal-700">
                        Current working location
                    </span>
                )}
            </div>

            {!activeAssignment ? (
                <p className="pl-5 text-[13px] text-slate-400">
                    No active assignment
                </p>
            ) : (
                <div className="space-y-1 pl-5">
                    <p className="text-[13px] font-medium text-slate-900">
                        {activeAssignment.houseUnitDisplayName}
                    </p>

                    <p className="text-[13px] leading-5 text-slate-500">
                        Active assignment
                        <span className="mx-2 text-slate-300">·</span>
                        {formatDate(activeAssignment.startDate)}
                        <span className="mx-2 text-slate-300">→</span>
                        {activeAssignment.endDate
                            ? formatDate(activeAssignment.endDate)
                            : 'Open-ended'}
                    </p>
                </div>
            )}
        </section>
    )
}