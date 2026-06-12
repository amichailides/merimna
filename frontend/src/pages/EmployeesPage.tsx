import { useEmployees } from "@/api/useEmployees";

export function EmployeesPage() {
    const {
        employees,
        loading,
        error,
        page,
        totalPages,
        totalElements,
        goToNextPage,
        goToPreviousPage,
    } = useEmployees()

    if (loading) {
        return <p>Loading Employees...</p>
    }

    if (error) return <p>{error}</p>

    return (
        <main className="space-y-6">
            <div>
                <h1 className="text-2xl font-semibold text-slate-900">
                    Employees
                </h1>
                <p className="text-sm text-slate-500">
                    Manage employees and staff access.
                </p>
            </div>

            <div>
                <p>Total employees: {totalElements}</p>

                <div>
                    {employees.map((employee) => (
                        <div key={employee.publicId}>
                            <div>
                                {employee.firstName} {employee.lastName}
                            </div>

                            <div>
                                {employee.positionCode}
                            </div>

                            <div>
                                {employee.active ? 'Active' : 'Inactive'}
                            </div>
                        </div>
                    ))}
                </div>
                <div>
                    <button onClick={goToPreviousPage}>
                        Previous
                    </button>

                    <span>
                        Page {page + 1} of {totalPages}
                    </span>

                    <button onClick={goToNextPage}>
                        Next
                    </button>
                </div>
            </div>
        </main>
    )
}