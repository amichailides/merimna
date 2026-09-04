import { useEffect, useMemo, useState } from 'react'
import { useNavigate, useSearchParams } from 'react-router-dom'
import { Plus } from 'lucide-react'

import { useBeneficiaries } from '@/api/useBeneficiaries'
import type { BeneficiarySearchDTO } from '@/api/types'

import { ListPagination } from '@/components/common/ListPagination'
import { BeneficiaryListFilters } from '@/components/beneficiaries/list/BeneficiaryListFilters'
import { BeneficiaryListRow } from '@/components/beneficiaries/list/BeneficiaryListRow'
import { Button } from '@/components/ui/button'

export function BeneficiariesPage() {
    const [searchParams, setSearchParams] = useSearchParams()
    const navigate = useNavigate()

    const [searchTerm, setSearchTerm] = useState(searchParams.get('q') ?? '')
    const [debouncedSearchTerm, setDebouncedSearchTerm] = useState(
        searchParams.get('q') ?? ''
    )

    const includeInactive = searchParams.get('includeInactive') === 'true'
    const houseUnitPublicId = searchParams.get('houseUnitPublicId') ?? undefined

    useEffect(() => {
        const id = window.setTimeout(() => {
            const normalizedSearchTerm = searchTerm.trim()

            setDebouncedSearchTerm(normalizedSearchTerm)

            setSearchParams(
                (prev) => {
                    const next = new URLSearchParams(prev)

                    if (normalizedSearchTerm.length >= 2) {
                        next.set('q', normalizedSearchTerm)
                    } else {
                        next.delete('q')
                    }

                    return next
                },
                { replace: true }
            )
        }, 300)

        return () => window.clearTimeout(id)
    }, [searchTerm, setSearchParams])

    function handleIncludeInactiveChange(value: boolean) {
        setSearchParams(
            (prev) => {
                const next = new URLSearchParams(prev)

                if (value) {
                    next.set('includeInactive', 'true')
                } else {
                    next.delete('includeInactive')
                }

                return next
            },
            { replace: true }
        )
    }

    function handleHouseUnitPublicIdChange(value: string | undefined) {
        setSearchParams(
            (prev) => {
                const next = new URLSearchParams(prev)

                if (value) {
                    next.set('houseUnitPublicId', value)
                } else {
                    next.delete('houseUnitPublicId')
                }

                return next
            },
            { replace: true }
        )
    }

    function handleClearFilters() {
        setSearchParams(
            (prev) => {
                const next = new URLSearchParams(prev)
                next.delete('houseUnitPublicId')
                return next
            },
            { replace: true }
        )
    }

    const criteria = useMemo<BeneficiarySearchDTO>(() => {
        const normalizedSearchTerm = debouncedSearchTerm.trim()

        return {
            q: normalizedSearchTerm.length >= 2 ? normalizedSearchTerm : undefined,
            includeInactive,
            houseUnitPublicId,
        }
    }, [debouncedSearchTerm, includeInactive, houseUnitPublicId])

    const {
        beneficiaries,
        loading,
        error,
        page,
        size,
        totalPages,
        totalElements,
        changePagination,
    } = useBeneficiaries(criteria)

    return (
        <main className="max-w-3xl space-y-4">
            <section className="flex items-start justify-between gap-4">
                <div>
                    <h1 className="text-[18px] font-medium text-slate-900">
                        Beneficiaries
                    </h1>

                    <p className="mt-0.5 text-[13px] text-slate-400">
                        Manage beneficiaries and supported living details.
                    </p>
                </div>

                <Button
                    type="button"
                    size="sm"
                    onClick={() => navigate('/beneficiaries/new')}
                    className="h-8 gap-1.5 bg-teal-700 text-white hover:bg-teal-800 text-[13px]"
                >
                    <Plus className="h-4 w-4 shrink-0" />
                    Add beneficiary
                </Button>
            </section>

            <BeneficiaryListFilters
                searchTerm={searchTerm}
                includeInactive={includeInactive}
                houseUnitPublicId={houseUnitPublicId}
                onSearchTermChange={setSearchTerm}
                onIncludeInactiveChange={handleIncludeInactiveChange}
                onHouseUnitPublicIdChange={handleHouseUnitPublicIdChange}
                onClearFilters={handleClearFilters}
            />

            {error ? (
                <p className="py-6 text-[13px] text-red-500">{error}</p>
            ) : (
                <div className={loading ? 'opacity-60 pointer-events-none' : undefined}>
                    {beneficiaries.length === 0 && loading ? (
                        <p className="py-6 text-[13px] text-slate-400">
                            Loading beneficiaries...
                        </p>
                    ) : beneficiaries.length === 0 ? (
                        <p className="py-6 text-[13px] text-slate-400">
                            No beneficiaries found.
                        </p>
                    ) : (
                        beneficiaries.map((beneficiary) => (
                            <BeneficiaryListRow
                                key={beneficiary.publicId}
                                beneficiary={beneficiary}
                            />
                        ))
                    )}
                </div>
            )}

            <div className="border-t border-slate-100 pt-3">
                <ListPagination
                    page={page}
                    size={size}
                    totalElements={totalElements}
                    totalPages={totalPages}
                    onPaginationChange={changePagination}
                />
            </div>
        </main>
    )
}