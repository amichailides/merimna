import { useEffect, useState } from 'react'
import { getEmployeeActivity } from './employeeActivityApi'
import type { EmployeeActivityDTO } from './types'

interface UseEmployeeActivityOptions {
    size?: number
}

interface UseEmployeeActivityResult {
    activities: EmployeeActivityDTO[]
    loading: boolean
    error: string | null
}

export function useEmployeeActivity(
    employeePublicId: string | undefined,
    options: UseEmployeeActivityOptions = {},
): UseEmployeeActivityResult {
    const [activities, setActivities] = useState<EmployeeActivityDTO[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    const size = options.size ?? 5

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

        getEmployeeActivity(employeePublicId, { page: 0, size })
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
    }, [employeePublicId, size])

    return { activities, loading, error }
}