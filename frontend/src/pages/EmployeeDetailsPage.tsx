import { Link, useParams } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'

import { EmployeeProfileHeader } from '@/components/employees/EmployeeProfileHeader'
import { EmployeeAssignmentCard } from '@/components/employees/EmployeeAssignmentCard'
import { EmployeeCurrentPlacementCard } from '@/components/employees/EmployeeCurrentPlacementCard'
import { EmployeeRecentActivitySection } from '@/components/employees/EmployeeRecentActivitySection'
import { useEmployeeDetails } from '@/api/useEmployeeDetails'
import { useEmployeeActivity } from '@/api/useEmployeeActivity'

function formatAddress(address: unknown) {
    if (!address || typeof address !== 'object') {
        return '—'
    }

    const values = Object.values(address as Record<string, string | null | undefined>)
        .filter((value): value is string => Boolean(value && value.trim()))

    return values.length > 0 ? values.join(', ') : '—'
}

function DetailItem({
    label,
    value,
}: {
    label: string
    value?: string | null
}) {
    return (
        <div className="space-y-1">
            <dt className="text-[12px] text-slate-500">
                {label}
            </dt>
            <dd className="text-[13px] leading-5 text-slate-900">
                {value || '—'}
            </dd>
        </div>
    )
}

export function EmployeeDetailsPage() {
    const { publicId } = useParams<{ publicId: string }>()

    const {
        employee,
        loading,
        error,
    } = useEmployeeDetails(publicId)

    const {
        activities,
        loading: activityLoading,
        error: activityError,
    } = useEmployeeActivity(publicId)

    if (loading) {
        return (
            <div className="text-[13px] text-slate-500">
                Loading employee…
            </div>
        )
    }

    if (error || !employee) {
        return (
            <div className="text-[13px] text-slate-500">
                {error ?? 'Employee not found'}
            </div>
        )
    }

    return (
        <div className="max-w-6xl space-y-7">
            <Link
                to="/employees"
                className="inline-flex items-center gap-1.5 text-[13px] text-slate-500 transition-colors hover:text-slate-950"
            >
                <ArrowLeft size={13} />
                Back to employees
            </Link>

            <EmployeeProfileHeader employee={employee} />

            <div className="grid items-start gap-8 lg:grid-cols-[minmax(0,1fr)_18rem]">
                <main className="space-y-10 sm:pl-[4.5rem]">
                    <section className="max-w-md space-y-5">
                        <div className="border-b border-slate-100 pb-3">
                            <h2 className="text-[13px] font-medium text-slate-700">
                                Work location
                            </h2>
                        </div>

                        <div className="space-y-7">
                            {employee.activePlacement ? (
                                <>
                                    <EmployeeCurrentPlacementCard
                                        placement={employee.activePlacement}
                                    />

                                    <div className="border-t border-slate-100 pt-6">
                                        <EmployeeAssignmentCard
                                            assignments={employee.assignments}
                                            isCurrentWorkingUnit={false}
                                        />
                                    </div>
                                </>
                            ) : (
                                <EmployeeAssignmentCard
                                    assignments={employee.assignments}
                                    isCurrentWorkingUnit
                                />
                            )}
                        </div>
                    </section>

                    {employee.publicId && (
                        <section>
                            <EmployeeRecentActivitySection
                                activities={activities}
                                loading={activityLoading}
                                error={activityError}
                            />
                        </section>
                    )}
                </main>

                <aside className="border-l border-slate-100 pl-5 pt-8">
                    <div className="space-y-8">
                        <section className="space-y-4">
                            <h2 className="text-[13px] font-medium text-slate-700">
                                Contact
                            </h2>

                            <dl className="space-y-4">
                                <DetailItem
                                    label="Email"
                                    value={employee.contactEmail}
                                />

                                <DetailItem
                                    label="Mobile"
                                    value={employee.mobileNumber}
                                />

                                <DetailItem
                                    label="Address"
                                    value={formatAddress(employee.address)}
                                />
                            </dl>
                        </section>

                        <div className="border-t border-slate-100" />

                        <section className="space-y-4">
                            <h2 className="text-xs font-semibold uppercase tracking-wide text-slate-400">
                                Employment
                            </h2>

                            <dl className="space-y-4">
                                <DetailItem
                                    label="Position"
                                    value={employee.positionDisplayName}
                                />

                                <DetailItem
                                    label="Hire date"
                                    value={employee.hireDate}
                                />

                                <DetailItem
                                    label="Status"
                                    value={employee.active ? 'Active' : 'Inactive'}
                                />
                            </dl>
                        </section>
                    </div>
                </aside>
            </div>
        </div >
    )
}