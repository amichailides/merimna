import type { HouseUnitOverviewReadOnlyDTO } from '@/api/types'

type HouseUnitsOverviewProps = {
    houseUnits: HouseUnitOverviewReadOnlyDTO[]
}

export function HouseUnitsOverview({
    houseUnits,
}: HouseUnitsOverviewProps) {
    return (
        <section>
            <div className="mb-4">
                <h2 className="text-lg font-semibold text-slate-950">
                    House units
                </h2>

                <p className="mt-1 text-sm text-slate-500">
                    Current occupancy and staffing across the organization.
                </p>
            </div>

            <div className="overflow-hidden border-y border-slate-200">
                <div className="grid grid-cols-[1.4fr_1.2fr_0.9fr_1fr] bg-teal-50/40 px-4 py-2.5 text-xs font-medium uppercase tracking-wide text-slate-500">
                    <div>House unit</div>
                    <div>Occupancy</div>
                    <div>Assigned staff</div>
                    <div>Temporary placements</div>
                </div>

                {houseUnits.map((houseUnit) => {
                    const activeBeneficiaries =
                        houseUnit.activeBeneficiaries ?? 0

                    const maxCapacity =
                        houseUnit.maxCapacity ?? 0

                    const occupancyPercentage =
                        maxCapacity > 0
                            ? Math.min(
                                (activeBeneficiaries / maxCapacity) * 100,
                                100,
                            )
                            : 0

                    return (
                        <div
                            key={houseUnit.publicId}
                            className="grid grid-cols-[1.4fr_1.2fr_0.9fr_1fr] items-center border-t border-slate-100 px-4 py-4 text-sm first:border-t-0"
                        >
                            <div className="font-semibold text-slate-900">
                                {houseUnit.displayName}
                            </div>

                            <div className="pr-10">
                                <div className="flex items-center justify-between text-sm">
                                    <span className="font-medium text-slate-800">
                                        {activeBeneficiaries}
                                    </span>

                                    <span className="text-xs text-slate-400">
                                        of {maxCapacity}
                                    </span>
                                </div>

                                <div className="mt-2 h-1 overflow-hidden rounded-full bg-slate-100">
                                    <div
                                        className="h-full rounded-full bg-teal-500"
                                        style={{
                                            width: `${occupancyPercentage}%`,
                                        }}
                                    />
                                </div>
                            </div>

                            <div>
                                <div className="font-medium text-slate-800">
                                    {houseUnit.activeAssignedEmployees ?? 0}
                                </div>

                                <div className="mt-0.5 text-xs text-slate-400">
                                    active
                                </div>
                            </div>

                            <div>
                                <div className="font-medium text-slate-800">
                                    {houseUnit.activePlacedEmployees ?? 0}
                                </div>

                                <div className="mt-0.5 text-xs text-slate-400">
                                    temporary
                                </div>
                            </div>
                        </div>
                    )
                })}
            </div>
        </section>
    )
}
