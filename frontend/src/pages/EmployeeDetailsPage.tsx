import { Link, useParams } from 'react-router-dom'
import { Button } from '@/components/ui/button'

import { useEmployeeDetails } from '@/api/useEmployeeDetails'
import { EmployeeProfileHeader } from '@/components/employees/EmployeeProfileHeader'
import { EmployeeCurrentPlacementCard } from '@/components/employees/EmployeeCurrentPlacementCard'
import { EmployeeAssignmentCard } from '@/components/employees/EmployeeAssignmentCard'
import { EmployeeAddressCard } from '@/components/employees/EmployeeAddressCard'



export function EmployeeDetailsPage() {
    const { publicId } = useParams()
    const { employee, loading, error } = useEmployeeDetails(publicId)

    if (loading) {
        return <div>Loading Employee...</div>
    }

    if (error) {
        return <div>{error}</div>
    }

    if (!employee) {
        return <div>Employee not found.</div>
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

            <div className="space-y-6">
                {employee.activePlacement ? (
                    <div className="grid gap-6 lg:grid-cols-2">
                        <EmployeeCurrentPlacementCard placement={employee.activePlacement} />
                        <EmployeeAssignmentCard assignments={employee.assignments} />
                    </div>
                ) : (
                    <div className="grid gap-6 lg:grid-cols-2">
                        <EmployeeAssignmentCard
                            assignments={employee.assignments}
                            isCurrentWorkingUnit
                        />
                    </div>
                )}

                <div className="grid gap-6 lg:grid-cols-2">
                    <EmployeeAddressCard address={employee.address} />
                </div>
            </div>
        </div>
    )
}