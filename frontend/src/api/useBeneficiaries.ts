import { useEffect, useRef, useState } from 'react'

import { getBeneficiaries } from './beneficiaryApi'

import type {
    BeneficiaryListDTO,
    BeneficiarySearchDTO,
    PageResponseBeneficiaryListDTO,
} from './types'

type UseBeneficiariesResult = {
    beneficiaries: BeneficiaryListDTO[]
    loading: boolean
    error: string | null
    page: number
    size: number
    totalElements: number
    totalPages: number
    changePagination: (page: number, size: number) => void
}

export function useBeneficiaries(
    criteria?: BeneficiarySearchDTO
): UseBeneficiariesResult {
    const [beneficiaries, setBeneficiaries] = useState<BeneficiaryListDTO[]>([])
    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [page, setPage] = useState(0)
    const [size, setSize] = useState(10)
    const [totalElements, setTotalElements] = useState(0)
    const [totalPages, setTotalPages] = useState(0)

    const prevCriteriaRef = useRef(criteria)

    function changePagination(newPage: number, newSize: number) {
        setPage(newPage)
        setSize(newSize)
    }

    useEffect(() => {
        if (prevCriteriaRef.current !== criteria) {
            prevCriteriaRef.current = criteria

            if (page !== 0) {
                setPage(0)
                return // bail — effect re-runs once page settles to 0
            }
        }

        let cancelled = false

        async function loadBeneficiaries() {
            setLoading(true)
            setError(null)

            try {
                const data: PageResponseBeneficiaryListDTO =
                    await getBeneficiaries(criteria, { page, size })

                if (cancelled) return

                setBeneficiaries(data.content ?? [])
                setTotalElements(data.totalElements ?? 0)
                setTotalPages(data.totalPages ?? 0)
            } catch {
                if (!cancelled) setError('Failed to load beneficiaries')
            } finally {
                if (!cancelled) setLoading(false)
            }
        }

        loadBeneficiaries()

        return () => {
            cancelled = true
        }
    }, [criteria, page, size])

    return {
        beneficiaries,
        loading,
        error,
        page,
        size,
        totalElements,
        totalPages,
        changePagination,
    }
}
