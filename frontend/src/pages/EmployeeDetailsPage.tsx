import { Link, useParams } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'

import { EmployeeProfileHeader } from '@/components/employees/EmployeeProfileHeader'
import { EmployeeRecentActivitySection } from '@/components/employees/EmployeeRecentActivitySection'
import { useEmployeeDetails } from '@/api/useEmployeeDetails'
import { useEmployeeActivity } from '@/api/useEmployeeActivity'
import { EmployeeWorkDetailsSection } from '@/components/employees/EmployeeWorkDetailsSection'
import { EmployeeMetadataRail } from '@/components/employees/EmployeeMetadataRail'

const PROFILE_TABS = ['Overview', 'Assignments', 'Placements', 'Activity'] as const

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

            <div>
                <EmployeeProfileHeader employee={employee} />

                <div className="mt-3 border-b border-slate-200">
                    <div className="flex items-center gap-6">
                        {PROFILE_TABS.map((tab) => {
                            const active = tab === 'Overview'

                            return (
                                <button
                                    key={tab}
                                    type="button"
                                    className={[
                                        'relative pb-3 text-sm leading-none transition-colors',
                                        active
                                            ? 'font-medium text-slate-900'
                                            : 'font-medium text-[#586579] hover:text-slate-900',
                                    ].join(' ')}
                                >
                                    {tab}

                                    {active && (
                                        <span className="absolute inset-x-0 -bottom-px h-[2px] rounded-full bg-teal-500" />
                                    )}
                                </button>
                            )
                        })}
                    </div>
                </div>
            </div>

            <div className="grid items-start gap-8 lg:grid-cols-[minmax(0,1fr)_18rem]">
                <main className="space-y-8">
                    <EmployeeWorkDetailsSection
                        assignments={employee.assignments}
                        placement={employee.activePlacement}
                    />

                    <div className="max-w-xl border-t border-slate-100" />

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

                <EmployeeMetadataRail employee={employee} />
            </div>
        </div>
    )
}