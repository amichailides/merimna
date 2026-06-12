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
}

export function useEmployees(): UseEmployeesResult {
    const [employees, setEmployees] = useState<EmployeeListDTO[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [page, setPage] = useState(0)
    const [size, setSize] = useState(10)
    const [totalElements, setTotalElements] = useState(0)
    const [totalPages, setTotalPages] = useState(0)

    useEffect(() => {
        async function loadEmployees() {
            setLoading(true)
            setError(null)

            try {
                const data: PageResponseEmployeeListDTO = await getEmployees()
                setEmployees(data.content ?? [])
                setPage(data.page ?? 0)
                setSize(data.size ?? 10)
                setTotalElements(data.totalElements ?? 0)
                setTotalPages(data.totalPages ?? 0)
            } catch {
                setError('Failed to load employees')
            } finally {
                setLoading(false)
            }
        }

        loadEmployees()
    }, [])

      return {
    employees,
    loading,
    error,
    page,
    size,
    totalElements,
    totalPages,
  }
}