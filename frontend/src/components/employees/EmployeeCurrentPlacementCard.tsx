import type { EmployeeDetailsDTO } from '@/api/types'
import { EmployeeInfoCard } from './EmployeeInfoCard'
import { InfoItem } from './InfoItem'
import { Building2 } from 'lucide-react'

type EmployeeCurrentPlacementCardProps = {
    placement: EmployeeDetailsDTO['activePlacement']
}

export function EmployeeCurrentPlacementCard({
    placement,
}: EmployeeCurrentPlacementCardProps) {
    return (
        <EmployeeInfoCard
            title="Current placement"
            description="Where the employee is currently placed."
            icon={<Building2 className="h-4 w-4" />}
        >
            {!placement ? (
                <div className="min-h-[150px] rounded-xl border border-dashed border-slate-200 bg-slate-50/60 p-5 flex items-center">
                    <div>
                        <p className="text-sm font-medium text-slate-900">
                            No active placement
                        </p>

                        <p className="mt-1 text-sm text-slate-500">
                            No temporary placement is active for this employee.
                        </p>
                    </div>
                </div>
            ) : (
                <div className="min-h-[150px] rounded-xl border border-teal-100 bg-teal-50/20 p-5">
                    <div className="min-h-[52px]">
                        <p className="text-base font-semibold text-slate-950">
                            {placement.houseUnitDisplayName}
                        </p>

                        <p className="mt-1 text-sm font-medium text-slate-600">
                            {placement.reasonDisplayName}
                        </p>
                    </div>

                    <div className="mt-5 grid gap-4 sm:grid-cols-2">
                        <InfoItem label="Start date" value={placement.startDate} />
                        <InfoItem label="End date" value={placement.endDate ?? '—'} />
                    </div>
                </div>
            )}
        </EmployeeInfoCard>
    )
}