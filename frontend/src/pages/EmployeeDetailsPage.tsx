import { Link, useParams } from 'react-router-dom'
import { AlertCircle, ArrowLeft, Loader2, UserX } from 'lucide-react'

import { useEmployeeDetails } from '@/api/useEmployeeDetails'
import { EmployeeAssignmentCard } from '@/components/employees/EmployeeAssignmentCard'
import { EmployeeCurrentPlacementCard } from '@/components/employees/EmployeeCurrentPlacementCard'
import { EmployeeDetailsState } from '@/components/employees/EmployeeDetailsState'
import { EmployeeProfileHeader } from '@/components/employees/EmployeeProfileHeader'

export function EmployeeDetailsPage() {
    const { publicId } = useParams()
    const { employee, loading, error } = useEmployeeDetails(publicId)

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
            {/* Back */}
            <Link
                to="/employees"
                className="inline-flex items-center gap-1.5 text-[13px] text-slate-500 hover:text-slate-950 transition-colors"
            >
                <ArrowLeft size={13} />
                Back to employees
            </Link>

            {/* Profile header */}
            <EmployeeProfileHeader employee={employee} />

            {/* Placement + Assignment */}
            <div className="grid gap-8 lg:grid-cols-2 max-w-3xl ml-[4.5rem]">
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
        </div>
    )
}