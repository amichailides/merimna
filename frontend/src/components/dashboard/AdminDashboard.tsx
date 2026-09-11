import { useAdminDashboard } from '@/api/useAdminDashboard'

export function AdminDashboard() {
    const { dashboard, loading, error } = useAdminDashboard()

    if (loading) {
        return <div className="p-8 text-sm text-slate-500">Loading dashboard...</div>
    }

    if (error) {
        return <div className="p-8 text-sm text-red-600">{error}</div>
    }

    if (!dashboard?.summary) {
        return null
    }

    const { summary, houseUnits = [] } = dashboard

    return (
        <div className="px-10 py-8">
            <header className="mb-7">
                <h1 className="text-2xl font-semibold tracking-tight text-slate-950">
                    Dashboard
                </h1>

                <p className="mt-1 text-sm text-slate-500">
                    Organization overview
                </p>
            </header>

            <section className="border-y border-slate-200">
                <div className="grid grid-cols-5 divide-x divide-slate-200">
                    <SummaryItem
                        label="Employees"
                        value={summary.activeEmployees ?? 0}
                    />

                    <SummaryItem
                        label="Beneficiaries"
                        value={summary.activeBeneficiaries ?? 0}
                    />

                    <SummaryItem
                        label="House units"
                        value={summary.houseUnits ?? 0}
                    />

                    <SummaryItem
                        label="Active assignments"
                        value={summary.activeAssignments ?? 0}
                    />

                    <SummaryItem
                        label="Active placements"
                        value={summary.activePlacements ?? 0}
                    />
                </div>
            </section>

            <section className="mt-10">
                <div className="mb-4">
                    <h2 className="text-lg font-semibold text-slate-950">
                        House units
                    </h2>

                    <p className="mt-1 text-sm text-slate-500">
                        Current occupancy and staffing across the organization.
                    </p>
                </div>

                <div className="overflow-hidden border-y border-slate-200">
                    <div className="grid grid-cols-[1.5fr_1fr_1fr_1fr] bg-slate-50 px-4 py-2.5 text-xs font-medium uppercase tracking-wide text-slate-500">
                        <div>House unit</div>
                        <div>Beneficiaries</div>
                        <div>Assigned staff</div>
                        <div>Temporary placements</div>
                    </div>

                    {houseUnits.map((houseUnit) => (
                        <div
                            key={houseUnit.publicId}
                            className="grid grid-cols-[1.5fr_1fr_1fr_1fr] items-center border-t border-slate-200 px-4 py-4 text-sm"
                        >
                            <div>
                                <div className="font-medium text-slate-900">
                                    {houseUnit.displayName}
                                </div>

                            </div>

                            <div className="text-slate-700">
                                {houseUnit.activeBeneficiaries ?? 0}
                                <span className="text-slate-400">
                                    {' '}
                                    / {houseUnit.maxCapacity ?? 0}
                                </span>
                            </div>

                            <div className="text-slate-700">
                                {houseUnit.activeAssignedEmployees ?? 0}
                            </div>

                            <div className="text-slate-700">
                                {houseUnit.activePlacedEmployees ?? 0}
                            </div>
                        </div>
                    ))}
                </div>
            </section>
        </div>
    )
}

type SummaryItemProps = {
    label: string
    value: number
}

function SummaryItem({ label, value }: SummaryItemProps) {
    return (
        <div className="px-5 py-5 first:pl-0">
            <div className="text-2xl font-semibold text-slate-950">
                {value}
            </div>

            <div className="mt-1 text-sm text-slate-500">
                {label}
            </div>
        </div>
    )
}
