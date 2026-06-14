import { useEmployees } from '@/api/useEmployees'
import { ListPagination } from '@/components/common/ListPagination'
import { EmployeeListRow } from '@/components/employees/EmployeeListRow'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'

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
        <main className="max-w-5xl space-y-6">
            <section className="flex items-start justify-between gap-4">
                <div>
                    <h1 className="text-2xl font-semibold tracking-tight text-slate-900">
                        Employees
                    </h1>

                    <p className="mt-1 text-sm text-slate-500">
                        Manage employees and staff access.
                    </p>
                </div>

                <Button type="button">
                    + Add employee
                </Button>
            </section>

            <Card>
                <CardContent className="p-0">
                    {employees.map((employee) => (
                        <EmployeeListRow
                            key={employee.publicId}
                            employee={employee}
                        />
                    ))}
                </CardContent>
            </Card>

            <ListPagination
                page={page}
                size={size}
                totalElements={totalElements}
                totalPages={totalPages}
                onPaginationChange={changePagination}
            />
        </main>
    )
}