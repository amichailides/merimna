import { useAdminDashboard } from '@/api/useAdminDashboard'

import { DashboardSummary } from './DashboardSummary'
import { HouseUnitsOverview } from './HouseUnitsOverview'
import { RecentActivitySection } from './RecentActivitySection'
import { SecurityEventsSection } from './SecurityEventsSection'

export function AdminDashboard() {
    const { dashboard, loading, error } = useAdminDashboard()

    if (loading) {
        return (
            <div className="p-8 text-sm text-slate-500">
                Loading dashboard...
            </div>
        )
    }

    if (error) {
        return (
            <div className="p-8 text-sm text-red-600">
                {error}
            </div>
        )
    }

    if (!dashboard?.summary) {
        return null
    }

    const {
        summary,
        houseUnits = [],
        recentActivity = [],
        securityEvents = [],
    } = dashboard

    return (
        <div className="px-10 py-8">
            <header className="mb-8">
                <h1 className="text-2xl font-semibold tracking-tight text-slate-950">
                    Dashboard
                </h1>

                <p className="mt-1 text-sm text-slate-500">
                    Organization overview
                </p>
            </header>

            <DashboardSummary summary={summary} />

            <div className="mt-10">
                <HouseUnitsOverview houseUnits={houseUnits} />
            </div>

            <div className="mt-10 grid gap-12 lg:grid-cols-2 lg:items-start">
                <RecentActivitySection recentActivity={recentActivity} />

                <SecurityEventsSection securityEvents={securityEvents} />
            </div>
        </div>
    )
}
