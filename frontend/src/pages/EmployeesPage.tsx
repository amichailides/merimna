import { useEmployees } from '@/api/useEmployees'
import { EmployeeListCard } from '@/components/employees/EmployeeListCard'
import { EmployeeListPagination } from '@/components/employees/EmployeeListPagination'

export function EmployeesPage() {
    const {
        employees,
        loading,
        error,
        page,
        size,
        totalPages,
        totalElements,
        changePagination,
    } = useEmployees()

    if (loading) {
        return <p>Loading Employees...</p>
    }

    if (error) {
        return <p>{error}</p>
    }

    return (
        <main className="space-y-6">
            <section className="flex items-start justify-between gap-4">
                <div>
                    <h1 className="text-2xl font-semibold tracking-tight text-slate-900">
                        Employees
                    </h1>

                    <p className="mt-1 text-sm text-slate-500">
                        Manage employees and staff access.
                    </p>
                </div>

                <div className="rounded-lg border bg-white px-4 py-3 text-right shadow-sm">
                    <p className="text-xs font-medium uppercase tracking-wide text-slate-500">
                        Total
                    </p>

                    <p className="text-2xl font-semibold text-slate-900">
                        {totalElements}
                    </p>
                </div>
            </section>

            <section className="space-y-3">
                {employees.map((employee) => (
                    <EmployeeListCard
                        key={employee.publicId}
                        employee={employee}
                    />
                ))}
            </section>

            <EmployeeListPagination
                page={page}
                size={size}
                totalElements={totalElements}
                totalPages={totalPages}
                onPaginationChange={changePagination}
            />
        </main>
    )
}