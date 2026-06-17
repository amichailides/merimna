import { Link, useParams } from 'react-router-dom'
import { AlertCircle, Loader2, UserX } from 'lucide-react'

import { useEmployeeDetails } from '@/api/useEmployeeDetails'
import { Button } from '@/components/ui/button'
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
                icon={<Loader2 className="size-5 animate-spin" />}
                title="Loading employee"
                description="Please wait while the employee details are being loaded."
            />
        )
    }

    if (error) {
        return (
            <EmployeeDetailsState
                icon={<AlertCircle className="size-5" />}
                title="Could not load employee"
                description={error}
            />
        )
    }

    if (!employee) {
        return (
            <EmployeeDetailsState
                icon={<UserX className="size-5" />}
                title="Employee not found"
                description="The employee you are looking for does not exist or may no longer be available."
            />
        )
    }

    return (
        <div className="space-y-6">
            <Button
                asChild
                variant="ghost"
                size="sm"
                className="-ml-2 text-slate-600 hover:bg-teal-50 hover:text-teal-800"
            >
                <Link to="/admin/employees">← Back to employees</Link>
            </Button>

            <div>
                <h1 className="text-2xl font-semibold tracking-tight text-slate-900">
                    Employee profile
                </h1>

                <p className="mt-1 text-sm text-slate-500">
                    View employee details, placement and organizational assignment.
                </p>
            </div>

            <EmployeeProfileHeader employee={employee} />

            <div className="grid gap-6 lg:grid-cols-2">
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