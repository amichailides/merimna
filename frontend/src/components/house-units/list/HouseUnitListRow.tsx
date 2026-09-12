import { Building2 } from 'lucide-react'

import type { HouseUnitReadOnlyDTO } from '@/api/types'

type HouseUnitListRowProps = {
    houseUnit: HouseUnitReadOnlyDTO
}

export function HouseUnitListRow({
    houseUnit,
}: HouseUnitListRowProps) {
    return (
        <div className="group flex items-center gap-3.5 py-3 border-b border-slate-100 last:border-b-0 rounded-md transition-all duration-150 hover:bg-teal-50/50 hover:translate-x-0.5">
            <div className="h-[34px] w-[34px] rounded-md flex items-center justify-center flex-shrink-0 bg-teal-50 text-teal-700 transition-transform duration-150 group-hover:scale-105">
                <Building2 className="h-4 w-4" />
            </div>

            <div className="min-w-0 w-56">
                <div className="flex items-center gap-2">
                    <p className="text-[13px] font-medium text-slate-900 truncate transition-colors duration-150 group-hover:text-teal-700">
                        {houseUnit.displayName}
                    </p>

                    <span className="rounded-md bg-slate-100 px-1.5 py-0.5 text-[10px] font-medium text-slate-500">
                        {houseUnit.code}
                    </span>
                </div>
            </div>

            <p className="flex-1 min-w-0 text-[11px] text-slate-400 truncate">
                {houseUnit.address ?? '—'}
            </p>
        </div>
    )
}
