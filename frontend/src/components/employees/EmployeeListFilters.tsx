import { Search, SlidersHorizontal } from 'lucide-react'

import { Button } from '@/components/ui/button'
import { Input } from '@/components/ui/input'
import type { EmployeeSearchDTO } from '@/api/types'

type EmployeeStatusFilter = NonNullable<EmployeeSearchDTO['status']>

type EmployeeListFiltersProps = {
    searchTerm: string
    status: EmployeeStatusFilter
    onSearchTermChange: (value: string) => void
    onStatusChange: (value: EmployeeStatusFilter) => void
}

const STATUS_OPTIONS: { value: EmployeeStatusFilter; label: string }[] = [
    { value: 'ALL', label: 'All' },
    { value: 'ACTIVE', label: 'Active' },
    { value: 'INACTIVE', label: 'Inactive' },
]

export function EmployeeListFilters({
    searchTerm,
    status,
    onSearchTermChange,
    onStatusChange,
}: EmployeeListFiltersProps) {
    return (
        <div className="flex items-center gap-2">
            <div className="relative flex-1 max-w-xs">
                <Search className="pointer-events-none absolute left-2.5 top-1/2 h-3.5 w-3.5 -translate-y-1/2 text-slate-400" />
                <Input
                    type="search"
                    placeholder="Search employees..."
                    className="pl-8 h-8 text-[13px] border-slate-100 bg-slate-50 focus-visible:ring-1 focus-visible:ring-teal-500/30 focus-visible:border-teal-400"
                    value={searchTerm}
                    onChange={(e) => onSearchTermChange(e.target.value)}
                />
            </div>

            <div className="flex items-center rounded-md border border-slate-100 overflow-hidden">
                {STATUS_OPTIONS.map((option) => (
                    <button
                        key={option.value}
                        type="button"
                        onClick={() => onStatusChange(option.value)}
                        className={`
                            px-3 h-8 text-[13px] transition-colors border-r border-slate-100 last:border-r-0
                            ${status === option.value
                                ? 'bg-slate-200 text-slate-900 font-medium'
                                : 'bg-white text-slate-500 hover:bg-slate-50 hover:text-slate-700'
                            }
                        `}
                    >
                        {option.label}
                    </button>
                ))}
            </div>

            <Button
                type="button"
                variant="outline"
                size="sm"
                disabled
                className="h-8 gap-1.5 text-[13px] border-slate-100 text-slate-400"
            >
                <SlidersHorizontal className="h-3.5 w-3.5" />
                Filters
            </Button>
        </div>
    )
}