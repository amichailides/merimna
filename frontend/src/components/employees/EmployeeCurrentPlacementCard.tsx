import type { EmployeeDetailsDTO } from '@/api/types'
import { HelpTooltip } from '@/components/ui/help-tooltip'
import { formatDate } from '@/lib/formatDate'
import { MapPin } from 'lucide-react'

type EmployeeCurrentPlacementCardProps = {
    placement: EmployeeDetailsDTO['activePlacement']
}

export function EmployeeCurrentPlacementCard({
    placement,
}: EmployeeCurrentPlacementCardProps) {
    if (!placement) {
        return null
    }

    return (
        <section className="space-y-2.5">
            <div className="flex items-center gap-2">
                <MapPin size={13} className="shrink-0 text-slate-400" />

                <h3 className="text-[13px] font-medium text-slate-800">
                    Currently working at
                </h3>

                <span className="rounded-full bg-teal-50 px-2 py-0.5 text-[11px] font-medium text-teal-700">
                    Temporary placement
                </span>

                <HelpTooltip content="Temporary placement is the employee's current working location, even when their official home unit is different." />
            </div>

            <div className="space-y-1">
                <p className="text-[13px] font-medium text-slate-900">
                    {placement.houseUnitDisplayName}
                </p>

                <p className="text-[13px] leading-5 text-slate-500">
                    {placement.reasonDisplayName}
                    <span className="mx-2 text-slate-300">·</span>
                    {formatDate(placement.startDate)}
                    <span className="mx-2 text-slate-300">→</span>
                    {placement.endDate
                        ? formatDate(placement.endDate)
                        : 'Open-ended'}
                </p>
            </div>
        </section>
    )
}