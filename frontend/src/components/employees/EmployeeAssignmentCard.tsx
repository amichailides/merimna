import type { EmployeeDetailsDTO } from '@/api/types'
import { EmployeeInfoCard } from './EmployeeInfoCard'
import { Briefcase } from 'lucide-react'
import { formatDate } from '@/lib/formatDate'

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
        <EmployeeInfoCard
            title="Organizational assignment"
            description="The employee's official house unit assignment."
            icon={<Briefcase size={13} className="text-slate-500" />}
        >
            {!activeAssignment ? (
                <p className="pl-5 text-[13px] text-slate-400">
                    No active assignment
                </p>
            ) : (
                <div className="space-y-3 pl-5">
                    <div className="min-h-[40px]">
                        <p className="text-[13px] font-medium text-slate-700">
                            {activeAssignment.houseUnitDisplayName}
                        </p>

                        {isCurrentWorkingUnit && (
                            <p className="mt-0.5 text-[12px] text-slate-500">
                                Current working unit
                            </p>
                        )}
                    </div>

                    <div>
                        <p className="text-[11px] font-medium uppercase tracking-wide text-slate-400">
                            Period
                        </p>

                        <p className="mt-1 text-[13px] text-slate-600">
                            {formatDate(activeAssignment.startDate)}
                            <span className="mx-2 text-slate-300">→</span>
                            {activeAssignment.endDate
                                ? formatDate(activeAssignment.endDate)
                                : 'Open-ended'}
                        </p>
                    </div>
                </div>
            )}
        </EmployeeInfoCard>
    )
}