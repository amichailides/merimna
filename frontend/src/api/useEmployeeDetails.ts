import { useEffect, useState } from 'react'

import { getEmployeeByPublicId } from './employeeApi'
import type { EmployeeDetailsDTO } from './types'

export function useEmployeeDetails(publicId: string | undefined) {
    const [employee, setEmployee] = useState<EmployeeDetailsDTO | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        if (!publicId) {
            setEmployee(null)
            setError(null)
            setLoading(false)
            return
        }

        const employeePublicId = publicId

        async function loadEmployee() {
            setLoading(true)
            setError(null)

            try {
                const data = await getEmployeeByPublicId(employeePublicId)
                setEmployee(data)
            } catch {
                setEmployee(null)
                setError('Failed to load employee details')
            } finally {
                setLoading(false)
            }
        }

        loadEmployee()
    }, [publicId])

    return {
        employee,
        loading,
        error,
    }
}