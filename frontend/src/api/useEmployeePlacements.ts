import { useEffect, useState } from "react";
import type { EmployeePlacementReadOnlyDTO } from "./types";
import { getPlacements } from "./placementApi";

type UseEmployeePlacementsResult = {
    placements: EmployeePlacementReadOnlyDTO[]
    loading: boolean
    error: string | null
}

export function useEmployeePlacements(
    employeePublicId: string | undefined
): UseEmployeePlacementsResult {
    const [placements, setPlacements] = useState<EmployeePlacementReadOnlyDTO[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)

    useEffect(() => {
        if (!employeePublicId) {
            setPlacements([])
            setError(null)
            setLoading(false)
            return
        }

        async function loadPlacements() {
            setLoading(true)
            setError(null)

            try {
                const data = await getPlacements({
                    employeePublicId,
                    includeInactive: true,
                    page: 0,
                    size: 20,
                    sort: 'startDate,desc',
                })

                setPlacements(data.content ?? [])
            } catch {
                setPlacements([])
                setError('Failed to load placements')
            } finally {
                setLoading(false)
            }
        }

        loadPlacements()
    }, [employeePublicId])

    return {
        placements,
        loading,
        error
    }
}