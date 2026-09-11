import { useEffect, useState } from 'react'

import { getAdminDashboard } from './dashboardApi'

import type { AdminDashboardReadOnlyDTO } from './types'

type UseAdminDashboardResult = {
    dashboard: AdminDashboardReadOnlyDTO | null
    loading: boolean
    error: string | null
}

export function useAdminDashboard(): UseAdminDashboardResult {
    const [dashboard, setDashboard] =
        useState<AdminDashboardReadOnlyDTO | null>(null)
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        let cancelled = false

        async function loadDashboard() {
            setLoading(true)
            setError(null)

            try {
                const data = await getAdminDashboard()

                if (cancelled) return

                setDashboard(data)
            } catch {
                if (!cancelled) {
                    setError('Failed to load dashboard')
                }
            } finally {
                if (!cancelled) {
                    setLoading(false)
                }
            }
        }

        loadDashboard()

        return () => {
            cancelled = true
        }
    }, [])

    return {
        dashboard,
        loading,
        error,
    }
}
