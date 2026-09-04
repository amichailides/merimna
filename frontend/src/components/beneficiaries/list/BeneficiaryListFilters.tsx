import { useState } from 'react'
import { Search, SlidersHorizontal, X } from 'lucide-react'

import { useHouseUnits } from '@/api/useHouseUnits'
import { Input } from '@/components/ui/input'
import {
    Select,
    SelectContent,
    SelectItem,
    SelectTrigger,
    SelectValue,
} from '@/components/ui/select'

type BeneficiaryListFiltersProps = {
    searchTerm: string
    includeInactive: boolean
    houseUnitPublicId: string | undefined
    onSearchTermChange: (value: string) => void
    onIncludeInactiveChange: (value: boolean) => void
    onHouseUnitPublicIdChange: (value: string | undefined) => void
    onClearFilters: () => void
}

export function BeneficiaryListFilters({
    searchTerm,
    includeInactive,
    houseUnitPublicId,
    onSearchTermChange,
    onIncludeInactiveChange,
    onHouseUnitPublicIdChange,
    onClearFilters,
}: BeneficiaryListFiltersProps) {
    const { houseUnits } = useHouseUnits()

    const activeFiltersCount = [houseUnitPublicId].filter(Boolean).length
    const [filtersOpen, setFiltersOpen] = useState(activeFiltersCount > 0)

    return (
        <div className="space-y-2">
            <div className="flex items-center gap-2">
                <div className="relative flex-1">
                    <Search className="pointer-events-none absolute left-2.5 top-1/2 h-3.5 w-3.5 -translate-y-1/2 text-slate-400" />

                    <Input
                        type="search"
                        placeholder="Search by name or AMKA..."
                        className="pl-8 h-8 text-[13px] border-slate-100 bg-slate-50 focus-visible:ring-1 focus-visible:ring-teal-500/30 focus-visible:border-teal-400"
                        value={searchTerm}
                        onChange={(e) => onSearchTermChange(e.target.value)}
                    />
                </div>

                <div className="flex h-8 items-center rounded-lg bg-slate-50 p-0.5">
                    <button
                        type="button"
                        onClick={() => onIncludeInactiveChange(false)}
                        className={`
                            h-7 w-16 rounded-md text-[12px] transition-colors
                            ${!includeInactive
                                ? 'bg-white text-slate-900 shadow-sm font-medium'
                                : 'bg-transparent text-slate-400 hover:text-slate-700'
                            }
                        `}
                    >
                        Active
                    </button>

                    <button
                        type="button"
                        onClick={() => onIncludeInactiveChange(true)}
                        className={`
                            h-7 w-16 rounded-md text-[12px] transition-colors
                            ${includeInactive
                                ? 'bg-white text-slate-900 shadow-sm font-medium'
                                : 'bg-transparent text-slate-400 hover:text-slate-700'
                            }
                        `}
                    >
                        All
                    </button>
                </div>

                <button
                    type="button"
                    onClick={() => setFiltersOpen((value) => !value)}
                    className={`
                        inline-flex items-center gap-1.5 h-8 px-3 rounded-md
                        text-[12px] transition-colors border
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
                        <span className="text-[11px] text-slate-400">
                            House unit
                        </span>

                        <Select
                            value={houseUnitPublicId ?? 'ALL_UNITS'}
                            onValueChange={(value) =>
                                onHouseUnitPublicIdChange(
                                    value === 'ALL_UNITS'
                                        ? undefined
                                        : value
                                )
                            }
                        >
                            <SelectTrigger className="h-7 text-[12px] border-slate-100 bg-slate-50 focus:ring-1 focus:ring-teal-500/30 min-w-[130px]">
                                <SelectValue placeholder="All house units" />
                            </SelectTrigger>

                            <SelectContent>
                                <SelectItem value="ALL_UNITS">
                                    All house units
                                </SelectItem>

                                {houseUnits.map((houseUnit) => {
                                    if (!houseUnit.publicId) {
                                        return null
                                    }

                                    return (
                                        <SelectItem
                                            key={houseUnit.publicId}
                                            value={houseUnit.publicId}
                                        >
                                            {houseUnit.displayName ??
                                                houseUnit.code ??
                                                houseUnit.publicId}
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
