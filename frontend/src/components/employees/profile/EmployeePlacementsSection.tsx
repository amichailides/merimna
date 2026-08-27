import type { EmployeePlacementReadOnlyDTO } from '@/api/types'
import { formatDate, formatDateRange } from '@/lib/formatDate'

type Props = {
    placements: EmployeePlacementReadOnlyDTO[]
    loading: boolean
    error: string | null
}

type PlacementStatus = 'Active' | 'Scheduled' | 'Ended' | 'Inactive'

function getTodayIsoDate() {
    const now = new Date()
    const timezoneOffset = now.getTimezoneOffset() * 60000

    return new Date(now.getTime() - timezoneOffset).toISOString().slice(0, 10)
}

function getPlacementStatus(
    placement: EmployeePlacementReadOnlyDTO,
    today: string
): PlacementStatus {
    if (placement.active === true) return 'Active'

    if (placement.startDate && placement.startDate > today) {
        return 'Scheduled'
    }

    if (placement.endDate && placement.endDate < today) {
        return 'Ended'
    }

    return 'Inactive'
}

function getPlacementStatusClass(status: PlacementStatus) {
    if (status === 'Active') {
        return 'bg-teal-50 text-teal-700'
    }

    return 'bg-slate-50 text-slate-600'
}

export function EmployeePlacementsSection({
    placements,
    loading,
    error,
}: Props) {
    if (loading) {
        return (
            <section className="max-w-xl">
                <p className="text-[13px] text-slate-500">
                    Loading placements...
                </p>
            </section>
        )
    }

    if (error) {
        return (
            <section className="max-w-xl">
                <p className="text-[13px] text-red-600">
                    {error}
                </p>
            </section>
        )
    }

    if (placements.length === 0) {
        return (
            <section className="max-w-xl">
                <div className="border-t border-slate-100 pt-4">
                    <p className="text-[13px] font-medium text-slate-700">
                        No temporary placements
                    </p>
                    <p className="mt-1 text-[13px] text-slate-500">
                        This employee has no temporary placement records.
                    </p>
                </div>
            </section>
        )
    }

    const today = getTodayIsoDate()

    const sortedPlacements = [...placements].sort((a, b) => {
        return (b.startDate ?? '').localeCompare(a.startDate ?? '')
    })

    const activePlacement = sortedPlacements.find((placement) => {
        return getPlacementStatus(placement, today) === 'Active'
    })

    const scheduledPlacements = sortedPlacements.filter((placement) => {
        return getPlacementStatus(placement, today) === 'Scheduled'
    })

    const pastPlacements = sortedPlacements.filter((placement) => {
        return getPlacementStatus(placement, today) === 'Ended'
    })

    return (
        <section className="max-w-xl space-y-9">
            <div className="space-y-3">
                <h2 className="text-[14px] font-medium text-slate-700">
                    Active placement
                </h2>

                <div className="pt-4">
                    {activePlacement ? (
                        <div className="grid grid-cols-[minmax(0,1fr)_auto] gap-4">
                            <div>
                                <p className="text-[14px] font-medium text-slate-900">
                                    {activePlacement.houseUnitDisplayName}
                                </p>

                                <dl className="mt-3 grid gap-2 text-[13px]">
                                    <div className="grid grid-cols-[7rem_minmax(0,1fr)] gap-3">
                                        <dt className="text-slate-400">Reason</dt>
                                        <dd className="text-slate-700">
                                            {activePlacement.reasonDisplayName ?? 'Unknown'}
                                        </dd>
                                    </div>

                                    <div className="grid grid-cols-[7rem_minmax(0,1fr)] gap-3">
                                        <dt className="text-slate-400">Started</dt>
                                        <dd className="text-slate-700">
                                            {formatDate(activePlacement.startDate)}
                                        </dd>
                                    </div>

                                    {activePlacement.endDate && (
                                        <div className="grid grid-cols-[7rem_minmax(0,1fr)] gap-3">
                                            <dt className="text-slate-400">
                                                Scheduled end
                                            </dt>
                                            <dd className="text-slate-700">
                                                {formatDate(activePlacement.endDate)}
                                            </dd>
                                        </div>
                                    )}
                                </dl>
                            </div>

                            <span
                                className={`h-fit rounded-full px-2 py-0.5 text-[11px] font-medium ${getPlacementStatusClass(
                                    'Active'
                                )}`}
                            >
                                Active
                            </span>
                        </div>
                    ) : (
                        <p className="text-[13px] text-slate-500">
                            No active temporary placement recorded.
                        </p>
                    )}
                </div>
            </div>

            {scheduledPlacements.length > 0 && (
                <div className="space-y-3 border-t border-slate-100 pt-9">
                    <h3 className="text-[14px] font-medium text-slate-700">
                        Scheduled placements
                    </h3>

                    <div className="divide-y divide-slate-100">
                        {scheduledPlacements.map((placement) => {
                            const status = getPlacementStatus(placement, today)

                            return (
                                <div
                                    key={placement.publicId}
                                    className="grid grid-cols-[minmax(0,1fr)_auto] gap-4 py-3"
                                >
                                    <div>
                                        <p className="text-[13px] font-medium text-slate-900">
                                            {placement.houseUnitDisplayName}
                                        </p>

                                        <p className="mt-1 text-[13px] text-slate-500">
                                            {formatDateRange(
                                                placement.startDate,
                                                placement.endDate
                                            )}
                                        </p>

                                        <p className="mt-1 text-[12px] text-slate-400">
                                            {placement.reasonDisplayName ?? 'Unknown reason'}
                                        </p>
                                    </div>

                                    <span
                                        className={`h-fit rounded-full px-2 py-0.5 text-[11px] font-medium ${getPlacementStatusClass(
                                            status
                                        )}`}
                                    >
                                        {status}
                                    </span>
                                </div>
                            )
                        })}
                    </div>
                </div>
            )}

            <div className="space-y-3 border-t border-slate-100 pt-9">
                <h3 className="text-[14px] font-medium text-slate-700">
                    Placement history
                </h3>

                {pastPlacements.length > 0 ? (
                    <div className="divide-y divide-slate-100">
                        {pastPlacements.map((placement) => {
                            const status = getPlacementStatus(placement, today)

                            return (
                                <div
                                    key={placement.publicId}
                                    className="grid grid-cols-[minmax(0,1fr)_auto] gap-4 py-3"
                                >
                                    <div>
                                        <p className="text-[13px] font-medium text-slate-900">
                                            {placement.houseUnitDisplayName}
                                        </p>

                                        <p className="mt-1 text-[13px] text-slate-500">
                                            {formatDateRange(
                                                placement.startDate,
                                                placement.endDate
                                            )}
                                        </p>

                                        <p className="mt-1 text-[12px] text-slate-400">
                                            {placement.reasonDisplayName ?? 'Unknown reason'}
                                        </p>
                                    </div>

                                    <span
                                        className={`h-fit rounded-full px-2 py-0.5 text-[11px] font-medium ${getPlacementStatusClass(
                                            status
                                        )}`}
                                    >
                                        {status}
                                    </span>
                                </div>
                            )
                        })}
                    </div>
                ) : (
                    <p className="pt-2 text-[13px] text-slate-500">
                        No previous temporary placements recorded.
                    </p>
                )}
            </div>
        </section>
    )
}