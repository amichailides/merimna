import { useEmployees } from "@/api/useEmployees";

export function EmployeesPage() {
    const { employees, loading, error, totalElements } = useEmployees()

    if (loading) {
        return <p>Loading Employees...</p>
    }

    if (error) return <p>{error}</p>

    return (
        <main>
            <div>
                <h1>Employees</h1>
                <p>Total employees: {totalElements}</p>
                {employees.map((employee) => (
                    <div key={employee.publicId}>
                        {employee.firstName} {employee.lastName}
                    </div>
                )
                )}
            </div>
        </main>
    )
}