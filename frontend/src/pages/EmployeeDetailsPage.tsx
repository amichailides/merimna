import { Link, useParams } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'

import { EmployeeProfileHeader } from '@/components/employees/EmployeeProfileHeader'
import { EmployeeAssignmentCard } from '@/components/employees/EmployeeAssignmentCard'
import { EmployeeCurrentPlacementCard } from '@/components/employees/EmployeeCurrentPlacementCard'
import { EmployeeRecentActivitySection } from '@/components/employees/EmployeeRecentActivitySection'
import { useEmployeeDetails } from '@/api/useEmployeeDetails'
import { useEmployeeActivity } from '@/api/useEmployeeActivity'

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
        <div className="max-w-4xl space-y-6">
            <Link
                to="/employees"
                className="inline-flex items-center gap-1.5 text-[13px] text-slate-500 transition-colors hover:text-slate-950"
            >
                <ArrowLeft size={13} />
                Back to employees
            </Link>

            <div className="relative min-h-[38rem]">
                {/* <div className="absolute left-[4.5rem] top-[4.25rem] bottom-0 hidden border-l border-slate-100 sm:block" /> */}

                <EmployeeProfileHeader employee={employee} />

                <div className="pt-6 sm:ml-[4.5rem]">
                    <div className="pl-5 sm:pl-6">
                        <div className="grid max-w-3xl items-start gap-8 lg:grid-cols-2">
                            <EmployeeAssignmentCard
                                assignments={employee.assignments}
                                isCurrentWorkingUnit={!employee.activePlacement}
                            />

                            {employee.activePlacement && (
                                <EmployeeCurrentPlacementCard
                                    placement={employee.activePlacement}
                                />
                            )}

                            {employee.publicId && (
                                <div className="rounded-lg bg-slate-50/20 px-4 py-3 shadow-[inset_1px_0_0_theme(colors.slate.200),inset_0_-1px_0_theme(colors.slate.200)]">
                                    <EmployeeRecentActivitySection
                                        activities={activities}
                                        loading={activityLoading}
                                        error={activityError}
                                    />
                                </div>
                            )}
                        </div>
                    </div>
                </div>
            </div>
        </div>
    )
}