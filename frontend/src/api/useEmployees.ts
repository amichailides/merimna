import { useEffect, useState, useRef } from 'react'

import { getEmployees } from './employeeApi'

import type {
    EmployeeListDTO,
    EmployeeSearchDTO,
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
    changePagination: (page: number, size: number) => void
}

export function useEmployees(criteria?: EmployeeSearchDTO): UseEmployeesResult {
    const [employees, setEmployees] = useState<EmployeeListDTO[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [page, setPage] = useState(0)
    const [size, setSize] = useState(10)
    const [totalElements, setTotalElements] = useState(0)
    const [totalPages, setTotalPages] = useState(0)

    function changePagination(newPage: number, newSize: number) {
        setPage(newPage)
        setSize(newSize)
    }

    const isFirstRender = useRef(true)

    useEffect(() => {
        if (isFirstRender.current) {
            isFirstRender.current = false
            return
        }
        setPage(0)
    }, [criteria])

    useEffect(() => {
        async function loadEmployees() {
            setLoading(true)
            setError(null)

            try {
                const data: PageResponseEmployeeListDTO = await getEmployees(criteria, {
                    page,
                    size,
                })

                setEmployees(data.content ?? [])
                setTotalElements(data.totalElements ?? 0)
                setTotalPages(data.totalPages ?? 0)
            } catch {
                setError('Failed to load employees')
            } finally {
                setLoading(false)
            }
        }

        loadEmployees()
    }, [criteria, page, size])

    return {
        employees,
        loading,
        error,
        page,
        size,
        totalElements,
        totalPages,
        changePagination,
    }
}