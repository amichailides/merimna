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
}

export function useEmployees(): UseEmployeesResult {
    const [employees, setEmployees] = useState<EmployeeListDTO[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        async function loadEmployees() {
            setLoading(true)
            setError(null)

            try {
                const data: PageResponseEmployeeListDTO = await getEmployees()
                setEmployees(data.content ?? [])
            } catch {
                setError('Failed to load employees')
            } finally {
                setLoading(false)
            }
        }

        loadEmployees
    }, [])

    return {
        employees,
        loading,
        error
    }
}