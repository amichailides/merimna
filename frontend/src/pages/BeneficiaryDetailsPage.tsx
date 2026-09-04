import { useState } from 'react'
import { Link, useParams } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'

import { useBeneficiaryDetails } from '@/api/useBeneficiaryDetails'

import { BeneficiaryProfileHeader } from '@/components/beneficiaries/profile/BeneficiaryProfileHeader'
import { BeneficiaryMetadataRail } from '@/components/beneficiaries/profile/BeneficiaryMetadataRail'
import { BeneficiaryOverviewSection } from '@/components/beneficiaries/profile/BeneficiaryOverviewSection'
import { BeneficiaryMedicationsSection } from '@/components/beneficiaries/profile/BeneficiaryMedicationsSection'
import { BeneficiaryAllergiesSection } from '@/components/beneficiaries/profile/BeneficiaryAllergiesSection'

const PROFILE_TABS = [
    'Overview',
    'Medications',
    'Allergies',
    'Legal representatives',
] as const

type BeneficiaryDetailsTab = (typeof PROFILE_TABS)[number]

export function BeneficiaryDetailsPage() {
    const { publicId } = useParams<{ publicId: string }>()
    const [activeTab, setActiveTab] =
        useState<BeneficiaryDetailsTab>('Overview')

    const {
        beneficiary,
        loading,
        error,
        reload,
    } = useBeneficiaryDetails(publicId)

    return (
        <div className="max-w-6xl space-y-7">
            <Link
                to="/beneficiaries"
                className="inline-flex items-center gap-1.5 text-[13px] text-slate-500 transition-colors hover:text-slate-950"
            >
                <ArrowLeft size={13} />
                Back to beneficiaries
            </Link>

            {loading && !beneficiary && (
                <div className="text-[13px] text-slate-500">
                    Loading beneficiary…
                </div>
            )}

            {error && !beneficiary && (
                <div className="space-y-2 text-[13px] text-slate-500">
                    <p>{error}</p>

                    <button
                        type="button"
                        onClick={reload}
                        className="text-[12px] font-medium text-teal-700 transition-colors hover:text-teal-800"
                    >
                        Try again
                    </button>
                </div>
            )}

            {!loading && !error && !beneficiary && (
                <div className="text-[13px] text-slate-500">
                    Beneficiary not found
                </div>
            )}

            {beneficiary && (
                <>
                    <div>
                        <BeneficiaryProfileHeader
                            beneficiary={beneficiary}
                        />

                        <div className="mt-3 border-b border-slate-200">
                            <div className="flex items-center gap-6">
                                {PROFILE_TABS.map((tab) => {
                                    const active = tab === activeTab

                                    return (
                                        <button
                                            key={tab}
                                            type="button"
                                            onClick={() => setActiveTab(tab)}
                                            className={[
                                                'relative cursor-pointer pb-3 text-sm leading-none transition-colors',
                                                active
                                                    ? 'font-medium text-slate-900'
                                                    : 'font-medium text-[#586579] hover:text-slate-900',
                                            ].join(' ')}
                                        >
                                            {tab}

                                            {active && (
                                                <span className="absolute inset-x-0 -bottom-px h-[2px] rounded-full bg-teal-500" />
                                            )}
                                        </button>
                                    )
                                })}
                            </div>
                        </div>
                    </div>

                    <div className="grid items-start gap-8 lg:grid-cols-[minmax(0,1fr)_18rem]">
                        <main className="space-y-8 pt-2">
                            {activeTab === 'Overview' && (
                                <BeneficiaryOverviewSection
                                    beneficiary={beneficiary}
                                />
                            )}

                            {activeTab === 'Medications' && (
                                <BeneficiaryMedicationsSection
                                    medications={beneficiary.medications ?? []}
                                />
                            )}

                            {activeTab === 'Allergies' && (
                                <BeneficiaryAllergiesSection
                                    allergies={beneficiary.allergies ?? []}
                                />
                            )}

                            {activeTab === 'Legal representatives' && (
                                <p className="py-6 text-[13px] text-slate-400">
                                    Legal representatives — coming soon.
                                </p>
                            )}
                        </main>

                        <BeneficiaryMetadataRail
                            beneficiary={beneficiary}
                        />
                    </div>
                </>
            )}
        </div>
    )
}