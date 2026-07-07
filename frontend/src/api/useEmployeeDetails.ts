import { useCallback, useEffect, useState } from 'react'

import { getEmployeeByPublicId } from './employeeApi'
import type { EmployeeDetailsDTO } from './types'

export function useEmployeeDetails(publicId: string | undefined) {
    const [employee, setEmployee] = useState<EmployeeDetailsDTO | null>(null)
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    const reload = useCallback(async () => {
        if (!publicId) {
            setEmployee(null)
            setError(null)
            setLoading(false)
            return
        }

        setLoading(true)
        setError(null)

        try {
            const data = await getEmployeeByPublicId(publicId)
            setEmployee(data)
        } catch {
            setEmployee(null)
            setError('Failed to load employee details')
        } finally {
            setLoading(false)
        }
    }, [publicId])

    useEffect(() => {
        reload()
    }, [reload])

    return {
        employee,
        loading,
        error,
        reload,
    }
}