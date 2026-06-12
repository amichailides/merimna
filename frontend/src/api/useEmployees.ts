import { useEffect, useState } from 'react'

import { getEmployees } from './employeeApi'

import type {
    EmployeeListDTO,
    PageResponseEmployeeListDTO,
} from './types'

type UseEmployeesResult = {
    employees: EmployeeListDTO[]
    loading: boolean
    error: string | null
    page: number
    size: number
    totalElements: number
    totalPages: number
    goToNextPage: () => void
    goToPreviousPage: () => void
}

export function useEmployees(): UseEmployeesResult {
    const [employees, setEmployees] = useState<EmployeeListDTO[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [page, setPage] = useState(0)
    const [size, setSize] = useState(5)
    const [totalElements, setTotalElements] = useState(0)
    const [totalPages, setTotalPages] = useState(0)

    function goToNextPage() {
        setPage((currentPage) => {
            if (currentPage >= totalPages - 1) {
                return currentPage
            }

            return currentPage + 1
        })
    }

    function goToPreviousPage() {
        setPage((currentPage) => {
            if (currentPage <= 0) {
                return currentPage
            }

            return currentPage - 1
        })
    }

    useEffect(() => {
        async function loadEmployees() {
            setLoading(true)
            setError(null)

            try {
                const data: PageResponseEmployeeListDTO = await getEmployees(undefined, {
                    page,
                    size,
                })
                setEmployees(data.content ?? [])
                setPage(data.page ?? 0)
                setSize(data.size ?? 5)
                setTotalElements(data.totalElements ?? 0)
                setTotalPages(data.totalPages ?? 0)
            } catch {
                setError('Failed to load employees')
            } finally {
                setLoading(false)
            }
        }

        loadEmployees()
    }, [page, size])

    return {
        employees,
        loading,
        error,
        page,
        size,
        totalElements,
        totalPages,
        goToNextPage,
        goToPreviousPage,
    }
}