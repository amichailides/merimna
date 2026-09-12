import { useEffect, useState } from 'react'
import { getHouseUnits } from './houseUnitApi'
import type { HouseUnitReadOnlyDTO } from './types'

export function useHouseUnits() {
    const [houseUnits, setHouseUnits] = useState<HouseUnitReadOnlyDTO[]>([])
    const [loading, setLoading] = useState(true)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        let cancelled = false

        async function loadHouseUnits() {
            setLoading(true)
            setError(null)

            try {
                const data = await getHouseUnits()

                if (cancelled) return

                setHouseUnits(data)
            } catch {
                if (!cancelled) {
                    setError('Failed to load house units')
                }
            } finally {
                if (!cancelled) {
                    setLoading(false)
                }
            }
        }

        loadHouseUnits()

        return () => {
            cancelled = true
        }
    }, [])

    return { houseUnits, loading, error }
}
