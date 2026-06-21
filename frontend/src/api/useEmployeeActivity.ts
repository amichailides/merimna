import { useEffect, useState } from 'react'
import { getEmployeeActivity } from './employeeActivityApi'
import type { EmployeeActivityDTO } from './types'

interface UseEmployeeActivityResult {
    activities: EmployeeActivityDTO[]
    loading: boolean
    error: string | null
}

export function useEmployeeActivity(
    employeePublicId: string | undefined,
): UseEmployeeActivityResult {
    const [activities, setActivities] = useState<EmployeeActivityDTO[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        let ignore = false

        if (!employeePublicId) {
            setActivities([])
            setLoading(false)
            setError(null)
            return
        }

        setLoading(true)
        setError(null)

        getEmployeeActivity(employeePublicId)
            .then(page => {
                if (!ignore) {
                    setActivities(page.content ?? [])
                }
            })
            .catch(() => {
                if (!ignore) {
                    setError('Could not load activity.')
                }
            })
            .finally(() => {
                if (!ignore) {
                    setLoading(false)
                }
            })

        return () => {
            ignore = true
        }
    }, [employeePublicId])

    return { activities, loading, error }
}