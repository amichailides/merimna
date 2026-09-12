import { Plus } from 'lucide-react'

import { useHouseUnits } from '@/api/useHouseUnits'
import { HouseUnitListRow } from '@/components/house-units/list/HouseUnitListRow'
import { Button } from '@/components/ui/button'

export function HouseUnitsPage() {

    const { houseUnits, loading, error } = useHouseUnits()

    const totalCapacity = houseUnits.reduce(
        (sum, houseUnit) => sum + (houseUnit.maxCapacity ?? 0),
        0
    )

    return (
        <main className="max-w-3xl space-y-4">
            <section className="flex items-start justify-between gap-4">
                <div>
                    <h1 className="text-[18px] font-medium text-slate-900">
                        House Units
                    </h1>

                    <p className="mt-0.5 text-[13px] text-slate-400">
                        Manage supported living house units.
                    </p>
                </div>

                <Button
                    type="button"
                    size="sm"
                    disabled
                    className="h-8 gap-1.5 bg-teal-700 text-white hover:bg-teal-700 text-[13px] disabled:opacity-100"
                >
                    <Plus className="h-4 w-4 shrink-0" />
                    Add house unit
                </Button>
            </section>

            {!loading && !error && houseUnits.length > 0 && (
                <p className="text-[11px] text-slate-400">
                    {houseUnits.length} house units
                    <span className="mx-1.5 text-slate-300">·</span>
                    Total capacity {totalCapacity}
                </p>
            )}

            {error ? (
                <p className="py-6 text-[13px] text-red-500">
                    {error}
                </p>
            ) : (
                <div
                    className={
                        loading
                            ? 'pointer-events-none opacity-60'
                            : undefined
                    }
                >
                    {houseUnits.length === 0 && loading ? (
                        <p className="py-6 text-[13px] text-slate-400">
                            Loading house units...
                        </p>
                    ) : houseUnits.length === 0 ? (
                        <p className="py-6 text-[13px] text-slate-400">
                            No house units found.
                        </p>
                    ) : (
                        houseUnits.map((houseUnit) => (
                            <HouseUnitListRow
                                key={houseUnit.publicId}
                                houseUnit={houseUnit}
                            />
                        ))
                    )}
                </div>
            )}
        </main>
    )
}
