import { useEffect, useState } from 'react'
import { getHouseUnits } from './houseUnitApi'
import type { HouseUnitReadOnlyDTO } from './types'

export function useHouseUnits() {
    const [houseUnits, setHouseUnits] = useState<HouseUnitReadOnlyDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        async function loadHouseUnits() {
            setLoading(true)
            setError(null)

            try {
                const data = await getHouseUnits()
                setHouseUnits(data)
            } catch {
                setError('Failed to load house units')
            } finally {
                setLoading(false)
            }
        }

        loadHouseUnits()
    }, [])

    return { houseUnits, loading, error }
}