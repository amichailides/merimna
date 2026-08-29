import { useCallback, useEffect, useState } from 'react'

import { getEmployeeAccess } from './employeeApi'
import type { EmployeeAccessDTO } from './types'

export function useEmployeeAccess(employeePublicId?: string) {
    const [access, setAccess] = useState<EmployeeAccessDTO | null>(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    const loadAccess = useCallback(async () => {
        if (!employeePublicId) {
            return
        }

        setLoading(true)
        setError(null)

        try {
            const result = await getEmployeeAccess(employeePublicId)
            setAccess(result)
        } catch {
            setError('Could not load system access information.')
        } finally {
            setLoading(false)
        }
    }, [employeePublicId])

    useEffect(() => {
        void loadAccess()
    }, [loadAccess])

    return {
        access,
        loading,
        error,
        reload: loadAccess,
    }
}