import { useEffect, useMemo, useState } from 'react'
import { useSearchParams } from 'react-router-dom'
import { Plus } from 'lucide-react'

import { useEmployees } from '@/api/useEmployees'
import type { EmployeeSearchDTO } from '@/api/types'
import { ListPagination } from '@/components/common/ListPagination'
import { EmployeeListFilters } from '@/components/employees/EmployeeListFilters'
import { EmployeeListRow } from '@/components/employees/EmployeeListRow'
import { Button } from '@/components/ui/button'

type EmployeeStatusFilter = NonNullable<EmployeeSearchDTO['status']>

const STATUS_VALUES: EmployeeStatusFilter[] = ['ALL', 'ACTIVE', 'INACTIVE']

function isValidStatus(value: string | null): value is EmployeeStatusFilter {
    return STATUS_VALUES.includes(value as EmployeeStatusFilter)
}

export function EmployeesPage() {
    const [searchParams, setSearchParams] = useSearchParams()
    const [searchTerm, setSearchTerm] = useState(searchParams.get('q') ?? '')
    const [debouncedSearchTerm, setDebouncedSearchTerm] = useState(searchParams.get('q') ?? '')

    const status: EmployeeStatusFilter = isValidStatus(searchParams.get('status'))
        ? (searchParams.get('status') as EmployeeStatusFilter)
        : 'ACTIVE'

    useEffect(() => {
        const id = window.setTimeout(() => {
            setDebouncedSearchTerm(searchTerm)
            setSearchParams((prev) => {
                const next = new URLSearchParams(prev)
                if (searchTerm.trim()) {
                    next.set('q', searchTerm.trim())
                } else {
                    next.delete('q')
                }
                return next
            })
        }, 300)
        return () => window.clearTimeout(id)
    }, [searchTerm])

    function handleStatusChange(value: EmployeeStatusFilter) {
        setSearchParams((prev) => {
            const next = new URLSearchParams(prev)
            next.set('status', value)
            return next
        })
    }

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
        return <p className="text-[13px] text-slate-400">{error}</p>
    }

    return (
        <main className="max-w-3xl space-y-4">
            <section className="flex items-start justify-between gap-4">
                <div>
                    <h1 className="text-[18px] font-medium text-slate-900">
                        Employees
                    </h1>
                    <p className="mt-0.5 text-[13px] text-slate-400">
                        Manage employees and staff access.
                    </p>
                </div>

                <Button
                    type="button"
                    size="sm"
                    className="h-8 gap-1.5 bg-teal-700 text-white hover:bg-teal-800 text-[13px]"
                >
                    <Plus className="h-4 w-4 shrink-0" />
                    Add employee
                </Button>
            </section>

            <EmployeeListFilters
                searchTerm={searchTerm}
                status={status}
                onSearchTermChange={setSearchTerm}
                onStatusChange={handleStatusChange}
            />

            <div className={loading ? 'opacity-60 pointer-events-none' : undefined}>
                {employees.length === 0 && loading ? (
                    <p className="py-6 text-[13px] text-slate-400">
                        Loading employees...
                    </p>
                ) : employees.length === 0 ? (
                    <p className="py-6 text-[13px] text-slate-400">
                        No employees found.
                    </p>
                ) : (
                    employees.map((employee) => (
                        <EmployeeListRow
                            key={employee.publicId}
                            employee={employee}
                        />
                    ))
                )}
            </div>

            <div className="border-t border-slate-100 pt-3">
                <ListPagination
                    page={page}
                    size={size}
                    totalElements={totalElements}
                    totalPages={totalPages}
                    onPaginationChange={changePagination}
                />
            </div>
        </main>
    )
}