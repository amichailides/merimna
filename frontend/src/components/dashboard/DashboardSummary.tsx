import type { AdminDashboardSummaryReadOnlyDTO } from '@/api/types'

type DashboardSummaryProps = {
    summary: AdminDashboardSummaryReadOnlyDTO
}

export function DashboardSummary({
    summary,
}: DashboardSummaryProps) {
    return (
        <section className="border-y border-slate-200">
            <div className="grid grid-cols-5 divide-x divide-slate-100">
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
    )
}

type SummaryItemProps = {
    label: string
    value: number
}

function SummaryItem({ label, value }: SummaryItemProps) {
    return (
        <div className="px-5 py-5 first:pl-0">
            <div className="text-[26px] font-semibold leading-none tracking-tight text-slate-950">
                {value}
            </div>

            <div className="mt-2 text-[13px] text-slate-500">
                {label}
            </div>
        </div>
    )
}
