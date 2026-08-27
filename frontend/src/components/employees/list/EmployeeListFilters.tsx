import { useState } from 'react'
import { Search, SlidersHorizontal, X } from 'lucide-react'
import { usePositions } from '@/api/usePositions'
import { useHouseUnits } from '@/api/useHouseUnits'
import { Input } from '@/components/ui/input'
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select'
import type { EmployeeSearchDTO } from '@/api/types'

type EmployeeStatusFilter = NonNullable<EmployeeSearchDTO['status']>

type EmployeeListFiltersProps = {
    searchTerm: string
    status: EmployeeStatusFilter
    positionCode: string | undefined
    houseUnitPublicId: string | undefined
    onSearchTermChange: (value: string) => void
    onStatusChange: (value: EmployeeStatusFilter) => void
    onPositionCodeChange: (value: string | undefined) => void
    onHouseUnitPublicIdChange: (value: string | undefined) => void
    onClearFilters: () => void
}

const STATUS_OPTIONS: { value: EmployeeStatusFilter; label: string }[] = [
    { value: 'ALL', label: 'All' },
    { value: 'ACTIVE', label: 'Active' },
    { value: 'INACTIVE', label: 'Inactive' },
]

export function EmployeeListFilters({
    searchTerm,
    status,
    positionCode,
    houseUnitPublicId,
    onSearchTermChange,
    onStatusChange,
    onPositionCodeChange,
    onHouseUnitPublicIdChange,
    onClearFilters,
}: EmployeeListFiltersProps) {
    const { positions } = usePositions()
    const { houseUnits } = useHouseUnits()

    const activeFiltersCount = [positionCode, houseUnitPublicId].filter(Boolean).length
    const [filtersOpen, setFiltersOpen] = useState(activeFiltersCount > 0)

    return (
        <div className="space-y-2">
            <div className="flex items-center gap-2">
                <div className="relative flex-1">
                    <Search className="pointer-events-none absolute left-2.5 top-1/2 h-3.5 w-3.5 -translate-y-1/2 text-slate-400" />
                    <Input
                        type="search"
                        placeholder="Search employees..."
                        className="pl-8 h-8 text-[13px] border-slate-100 bg-slate-50 focus-visible:ring-1 focus-visible:ring-teal-500/30 focus-visible:border-teal-400"
                        value={searchTerm}
                        onChange={(e) => onSearchTermChange(e.target.value)}
                    />
                </div>

                <div className="flex h-8 items-center rounded-lg bg-slate-50 p-0.5">
                    {STATUS_OPTIONS.map((option) => (
                        <button
                            key={option.value}
                            type="button"
                            onClick={() => onStatusChange(option.value)}
                            className={`
                                h-7 w-16 rounded-md text-[12px] transition-colors
                                ${status === option.value
                                    ? 'bg-white text-slate-900 shadow-sm font-medium'
                                    : 'bg-transparent text-slate-400 hover:text-slate-700'
                                }
                            `}
                        >
                            {option.label}
                        </button>
                    ))}
                </div>

                <button
                    type="button"
                    onClick={() => setFiltersOpen((v) => !v)}
                    className={`
                        inline-flex items-center gap-1.5 h-8 px-3 rounded-md text-[12px] transition-colors border
                        ${filtersOpen || activeFiltersCount > 0
                            ? 'border-teal-200 bg-teal-50 text-teal-700 hover:bg-teal-100'
                            : 'border-slate-100 bg-transparent text-slate-500 hover:text-slate-600 hover:bg-slate-50'
                        }
                    `}
                >
                    <SlidersHorizontal className="h-3.5 w-3.5" />
                    Filters
                    {!filtersOpen && activeFiltersCount > 0 && (
                        <span className="ml-0.5 text-teal-600 font-medium">
                            · {activeFiltersCount}
                        </span>
                    )}
                </button>
            </div>

            {filtersOpen && (
                <div className="flex items-center gap-6 pt-0.5">
                    <div className="flex items-center gap-2">
                        <span className="text-[11px] text-slate-400">Position</span>
                        <Select
                            value={positionCode ?? 'ALL_POSITIONS'}
                            onValueChange={(v) => onPositionCodeChange(v === 'ALL_POSITIONS' ? undefined : v)}
                        >
                            <SelectTrigger className="h-7 text-[12px] border-slate-100 bg-slate-50 focus:ring-1 focus:ring-teal-500/30 min-w-[130px]">
                                <SelectValue placeholder="All positions" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="ALL_POSITIONS">All positions</SelectItem>
                                {positions.map((p) => {
                                    if (!p.code) {
                                        return null
                                    }

                                    return (
                                        <SelectItem key={p.code} value={p.code}>
                                            {p.displayName ?? p.code}
                                        </SelectItem>
                                    )
                                })}
                            </SelectContent>
                        </Select>
                    </div>

                    <div className="flex items-center gap-2">
                        <span className="text-[11px] text-slate-400">House unit</span>
                        <Select
                            value={houseUnitPublicId ?? 'ALL_UNITS'}
                            onValueChange={(v) => onHouseUnitPublicIdChange(v === 'ALL_UNITS' ? undefined : v)}
                        >
                            <SelectTrigger className="h-7 text-[12px] border-slate-100 bg-slate-50 focus:ring-1 focus:ring-teal-500/30 min-w-[130px]">
                                <SelectValue placeholder="All house units" />
                            </SelectTrigger>
                            <SelectContent>
                                <SelectItem value="ALL_UNITS">All house units</SelectItem>
                                {houseUnits.map((h) => {
                                    if (!h.publicId) {
                                        return null
                                    }

                                    return (
                                        <SelectItem key={h.publicId} value={h.publicId}>
                                            {h.displayName ?? h.code ?? h.publicId}
                                        </SelectItem>
                                    )
                                })}
                            </SelectContent>
                        </Select>
                    </div>

                    {activeFiltersCount > 0 && (
                        <button
                            type="button"
                            onClick={() => {
                                onClearFilters()
                                setFiltersOpen(false)
                            }}
                            className="inline-flex items-center gap-1 text-[11px] text-slate-400 hover:text-slate-600 transition-colors"
                        >
                            <X className="h-3 w-3" />
                            Clear
                        </button>
                    )}
                </div>
            )}
        </div>
    )
}