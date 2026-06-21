import type { EmployeeDetailsDTO } from '@/api/types'
import { EmployeeInfoCard } from './EmployeeInfoCard'
import { InfoItem } from './InfoItem'
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
                <p className="text-[13px] text-slate-400">No active placement</p>
            ) : (
                <div className="space-y-3 pl-5">
                    <div className="min-h-[40px]">
                        <p className="text-[13px] font-medium text-slate-700">
                            {placement.houseUnitDisplayName}
                        </p>
                        <p className="text-[12px] text-slate-500 mt-0.5">
                            {placement.reasonDisplayName}
                        </p>
                    </div>
                    <div className="grid grid-cols-2 gap-3">
                        <InfoItem label="Start date" value={formatDate(placement.startDate)} />
                        <InfoItem label="End date" value={formatDate(placement.endDate)} />
                    </div>
                </div>
            )}
        </EmployeeInfoCard>
    )
}