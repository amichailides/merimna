import { useEffect, useState } from 'react'

import { getPositions } from './employeePositionApi'
import type { EmployeePositionReadOnlyDTO } from './types'

type UsePositionsResult = {
    positions: EmployeePositionReadOnlyDTO[]
    loading: boolean
    error: string | null
}

export function usePositions(): UsePositionsResult {
    const [positions, setPositions] = useState<EmployeePositionReadOnlyDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        async function loadPositions() {
            setLoading(true)
            setError(null)

            try {
                const data = await getPositions()
                setPositions(data)
            } catch {
                setError('Failed to load positions')
            } finally {
                setLoading(false)
            }
        }

        loadPositions()
    }, [])

    return { positions, loading, error }
}