import type { EmployeeDetailsDTO } from '@/api/types'
import { EmployeeInfoCard } from './EmployeeInfoCard'
import { InfoItem } from './InfoItem'
import { Building2 } from 'lucide-react'

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
            icon={<Building2 className="h-4 w-4" />}
        >
            {!activeAssignment ? (
                <div className="rounded-xl border border-dashed border-slate-200 bg-slate-50/80 p-5">
                    <p className="text-sm font-medium text-slate-900">
                        No organizational assignment
                    </p>

                    <p className="mt-1 text-sm text-slate-500">
                        This employee does not have an active official house unit assignment.
                    </p>
                </div>
            ) : (
                <div className="min-h-[150px] rounded-xl border border-teal-100 bg-teal-50/20 p-5">
                    <div className="min-h-[52px]">
                        <p className="text-base font-semibold text-slate-950">
                            {activeAssignment.houseUnitDisplayName}
                        </p>

                        {isCurrentWorkingUnit && (
                            <p className="mt-1 text-sm font-medium text-slate-600">
                                Current working unit
                            </p>
                        )}
                    </div>

                    <div className="mt-5 grid gap-4 sm:grid-cols-2">
                        <InfoItem
                            label="Start date"
                            value={activeAssignment.startDate}
                        />

                        <InfoItem
                            label="End date"
                            value={activeAssignment.endDate ?? '—'}
                        />
                    </div>
                </div>
            )}
        </EmployeeInfoCard>
    )
}