import { Link, useParams } from 'react-router-dom'
import { AlertCircle, ArrowLeft, Loader2, UserX } from 'lucide-react'

import { useEmployeeDetails } from '@/api/useEmployeeDetails'
import { useEmployeeActivity } from '@/api/useEmployeeActivity'
import { EmployeeAssignmentCard } from '@/components/employees/EmployeeAssignmentCard'
import { EmployeeCurrentPlacementCard } from '@/components/employees/EmployeeCurrentPlacementCard'
import { EmployeeDetailsState } from '@/components/employees/EmployeeDetailsState'
import { EmployeeProfileHeader } from '@/components/employees/EmployeeProfileHeader'
import { EmployeeRecentActivitySection } from '@/components/employees/EmployeeRecentActivitySection'

export function EmployeeDetailsPage() {
    const { publicId } = useParams()
    const { employee, loading, error } = useEmployeeDetails(publicId)

    const {
        activities,
        loading: activityLoading,
        error: activityError,
    } = useEmployeeActivity(employee?.publicId)

    if (loading) {
        return (
            <EmployeeDetailsState
                icon={<Loader2 className="size-4 animate-spin" />}
                title="Loading employee"
                description="Please wait while the employee details are being loaded."
            />
        )
    }

    if (error) {
        return (
            <EmployeeDetailsState
                icon={<AlertCircle className="size-4" />}
                title="Could not load employee"
                description={error}
            />
        )
    }

    if (!employee) {
        return (
            <EmployeeDetailsState
                icon={<UserX className="size-4" />}
                title="Employee not found"
                description="The employee you are looking for does not exist or may no longer be available."
            />
        )
    }

    return (
        <div className="space-y-6">
            <Link
                to="/employees"
                className="inline-flex items-center gap-1.5 text-[13px] text-slate-500 transition-colors hover:text-slate-950"
            >
                <ArrowLeft size={13} />
                Back to employees
            </Link>

            <EmployeeProfileHeader employee={employee} />

            <div className="grid max-w-3xl gap-8 lg:grid-cols-2 ml-[4.5rem]">
                {employee.activePlacement ? (
                    <>
                        <EmployeeCurrentPlacementCard placement={employee.activePlacement} />
                        <EmployeeAssignmentCard assignments={employee.assignments} />
                    </>
                ) : (
                    <EmployeeAssignmentCard
                        assignments={employee.assignments}
                        isCurrentWorkingUnit
                    />
                )}
            </div>

            {employee.publicId && (
                 <div className="ml-[4.5rem] mt-6">
                    <EmployeeRecentActivitySection
                        activities={activities}
                        loading={activityLoading}
                        error={activityError}
                    />
                </div>
            )}
        </div>
    )
}