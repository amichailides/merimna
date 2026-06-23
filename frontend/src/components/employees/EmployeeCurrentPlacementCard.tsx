import type { EmployeeDetailsDTO } from '@/api/types'
import { EmployeeInfoCard } from './EmployeeInfoCard'
import { MapPin } from 'lucide-react'
import { formatDate } from '@/lib/formatDate'

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
            icon={<MapPin size={13} className="text-slate-500" />}
        >
            {!placement ? (
                <p className="pl-5 text-[13px] text-slate-400">
                    No active placement
                </p>
            ) : (
                <div className="space-y-3 pl-5">
                    <div className="min-h-[40px]">
                        <p className="text-[13px] font-medium text-slate-700">
                            {placement.houseUnitDisplayName}
                        </p>

                        {placement.reasonDisplayName && (
                            <p className="mt-0.5 text-[12px] text-slate-500">
                                {placement.reasonDisplayName}
                            </p>
                        )}
                    </div>

                    <div>
                        <p className="text-[11px] font-medium uppercase tracking-wide text-slate-400">
                            Period
                        </p>

                        <p className="mt-1 text-[13px] text-slate-600">
                            {formatDate(placement.startDate)}
                            <span className="mx-2 text-slate-300">→</span>
                            {placement.endDate
                                ? formatDate(placement.endDate)
                                : 'Open-ended'}
                        </p>
                    </div>
                </div>
            )}
        </EmployeeInfoCard>
    )
}