import { useParams } from 'react-router-dom'

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
            <EmployeeDetailsCard employee={employee} />
        </div>
    )
}