import { useEffect, useState } from 'react'

import { getBeneficiaryByPublicId } from './beneficiaryApi'
import type { BeneficiaryDetailsDTO } from './types'

type UseBeneficiaryDetailsResult = {
    beneficiary: BeneficiaryDetailsDTO | null
    loading: boolean
    error: string | null
    reload: () => void
}

export function useBeneficiaryDetails(
    publicId: string | undefined
): UseBeneficiaryDetailsResult {
    const [beneficiary, setBeneficiary] =
        useState<BeneficiaryDetailsDTO | null>(null)

    const [loading, setLoading] = useState(false)
    const [error, setError] = useState<string | null>(null)
    const [reloadToken, setReloadToken] = useState(0)

    function reload() {
        setReloadToken((token) => token + 1)
    }

    useEffect(() => {
        if (!publicId) {
            setBeneficiary(null)
            setLoading(false)
            setError(null)
            return
        }

        const beneficiaryPublicId = publicId
        let cancelled = false

        async function loadBeneficiary() {
            setLoading(true)
            setError(null)

            try {
                const data =
                    await getBeneficiaryByPublicId(beneficiaryPublicId)

                if (cancelled) return

                setBeneficiary(data)
            } catch {
                if (!cancelled) {
                    setError('Failed to load beneficiary')
                }
            } finally {
                if (!cancelled) {
                    setLoading(false)
                }
            }
        }

        loadBeneficiary()

        return () => {
            cancelled = true
        }
    }, [publicId, reloadToken])

    return {
        beneficiary,
        loading,
        error,
        reload,
    }
}