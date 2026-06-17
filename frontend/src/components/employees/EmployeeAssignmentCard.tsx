import type { EmployeeDetailsDTO } from '@/api/types'
import { EmployeeInfoCard } from './EmployeeInfoCard'
import { InfoItem } from './InfoItem'
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
                <p className="text-[13px] text-slate-400 pl-5">No active assignment</p>
            ) : (
                <div className="space-y-3 pl-5">
                    <div className="min-h-[40px]">
                        <p className="text-[13px] font-semibold text-slate-950">
                            {activeAssignment.houseUnitDisplayName}
                        </p>
                        {isCurrentWorkingUnit && (
                            <p className="text-[12px] text-slate-500 mt-0.5">
                                Current working unit
                            </p>
                        )}
                    </div>
                    <div className="grid grid-cols-2 gap-3">
                        <InfoItem label="Start date" value={formatDate(activeAssignment.startDate)} />
                        <InfoItem label="End date" value={formatDate(activeAssignment.endDate)} />
                    </div>
                </div>
            )}
        </EmployeeInfoCard>
    )
}