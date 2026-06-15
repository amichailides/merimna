import { Link, useParams } from 'react-router-dom'
import { Button } from '@/components/ui/button'

import { useEmployeeDetails } from '@/api/useEmployeeDetails'
import { EmployeeDetailsCard } from '@/components/employees/EmployeeDetailsCard'



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
            <Button asChild variant="outline" size="sm">
                <Link to="/admin/employees">Back to employees</Link>
            </Button>

            <EmployeeDetailsCard employee={employee} />
        </div>
    )
}