import { useParams } from 'react-router-dom'
import { useEmployeeDetails } from '@/api/useEmployeeDetails'
import { Card, CardContent, CardHeader } from '@/components/ui/card'



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
        <Card>
            <CardHeader>EmployeeDetails</CardHeader>
            <CardContent>
                {employee?.firstName}
            </CardContent>
        </Card>
    )
}