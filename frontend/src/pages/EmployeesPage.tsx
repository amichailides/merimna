import { useEffect, useMemo, useState } from 'react'

import { useEmployees } from '@/api/useEmployees'
import type { EmployeeSearchDTO } from '@/api/types'
import { ListPagination } from '@/components/common/ListPagination'
import { EmployeeListFilters } from '@/components/employees/EmployeeListFilters'
import { EmployeeListRow } from '@/components/employees/EmployeeListRow'
import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'

export function EmployeesPage() {
    const [searchTerm, setSearchTerm] = useState('')
    const [debouncedSearchTerm, setDebouncedSearchTerm] = useState('')
    const [status, setStatus] =
        useState<NonNullable<EmployeeSearchDTO['status']>>('ACTIVE')

    useEffect(() => {
        const timeoutId = window.setTimeout(() => {
            setDebouncedSearchTerm(searchTerm)
        }, 300)

        return () => {
            window.clearTimeout(timeoutId)
        }
    }, [searchTerm])

    const criteria = useMemo<EmployeeSearchDTO>(
        () => ({
            q: debouncedSearchTerm.trim() || undefined,
            status,
        }),
        [debouncedSearchTerm, status]
    )

    const {
        employees,
        loading,
        error,
        page,
        size,
        totalPages,
        totalElements,
        changePagination,
    } = useEmployees(criteria)

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

                <Button
                    type="button"
                    className="bg-teal-700 text-white hover:bg-teal-800"
                >
                    + Add employee
                </Button>
            </section>

            <EmployeeListFilters
                searchTerm={searchTerm}
                status={status}
                onSearchTermChange={setSearchTerm}
                onStatusChange={setStatus}
            />

            <Card className="overflow-hidden border-slate-200 bg-white shadow-sm">
                <CardContent className="p-0">
                    {employees.length === 0 && loading ? (
                        <p className="px-4 py-6 text-sm text-slate-500">
                            Loading employees...
                        </p>
                    ) : employees.length === 0 ? (
                        <p className="px-4 py-6 text-sm text-slate-500">
                            No employees found.
                        </p>
                    ) : (
                        <div className={loading ? 'opacity-60' : undefined}>
                            {employees.map((employee) => (
                                <EmployeeListRow
                                    key={employee.publicId}
                                    employee={employee}
                                />
                            ))}
                        </div>
                    )}

                    <div className="border-t border-slate-200 px-4 py-3">
                        <ListPagination
                            page={page}
                            size={size}
                            totalElements={totalElements}
                            totalPages={totalPages}
                            onPaginationChange={changePagination}
                        />
                    </div>
                </CardContent>
            </Card>
        </main>
    )
}