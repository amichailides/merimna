import type { BeneficiaryDetailsDTO } from '@/api/types'

import { MedicationCreateForm } from '@/components/beneficiaries/medications/MedicationCreateForm'
import {
    FloatingPanelContent,
    FloatingPanelRoot,
    FloatingPanelTrigger,
} from '@/components/ui/floating-panel'

type Props = {
    beneficiaryPublicId: string
    medications: NonNullable<BeneficiaryDetailsDTO['medications']>
    onMedicationAdded?: () => void | Promise<void>
}

export function BeneficiaryMedicationsSection({
    beneficiaryPublicId,
    medications,
    onMedicationAdded,
}: Props) {
    return (
        <section className="max-w-2xl space-y-5">
            <div className="border-b border-slate-100 pb-3">
                <h2 className="text-[14px] font-medium text-slate-900">
                    Medications
                </h2>

                <div className="mt-1 flex items-center justify-between gap-4">
                    <p className="text-[12px] text-slate-400">
                        Current medication details for this beneficiary.
                    </p>

                    <FloatingPanelRoot>
                        <FloatingPanelTrigger
                            title="Add medication"
                            className="
                    h-auto border-0 bg-transparent px-0 py-0
                    text-[12px] font-medium text-teal-700
                    shadow-none transition-colors
                    hover:text-teal-800
                "
                        >
                            + Add medication
                        </FloatingPanelTrigger>

                        <FloatingPanelContent
                            align="center"
                            className="w-[min(92vw,34rem)]"
                        >
                            <MedicationCreateForm
                                beneficiaryPublicId={beneficiaryPublicId}
                                onMedicationAdded={onMedicationAdded}
                            />
                        </FloatingPanelContent>
                    </FloatingPanelRoot>
                </div>
            </div>

            {medications.length === 0 ? (
                <p className="py-6 text-[13px] text-slate-400">
                    No medications recorded.
                </p>
            ) : (
                <div className="divide-y divide-slate-100">
                    {medications.map((medication, index) => (
                        <article
                            key={
                                medication.publicId ??
                                `${medication.name ?? 'medication'}-${index}`
                            }
                            className="space-y-3 py-4 first:pt-0"
                        >
                            <div>
                                <h3 className="text-[13px] font-medium text-slate-900">
                                    {medication.name || '—'}
                                </h3>

                                <p className="mt-0.5 text-[12px] text-slate-400">
                                    {[
                                        medication.dosage,
                                        medication.frequency,
                                    ]
                                        .filter(Boolean)
                                        .join(' · ') || '—'}
                                </p>
                            </div>

                            <dl className="grid gap-4 sm:grid-cols-2">
                                <div className="space-y-1">
                                    <dt className="text-[12px] text-slate-500">
                                        Administration times
                                    </dt>

                                    <dd className="text-[13px] leading-5 text-slate-900">
                                        {medication.administrationTimes || '—'}
                                    </dd>
                                </div>

                                <div className="space-y-1">
                                    <dt className="text-[12px] text-slate-500">
                                        Instructions
                                    </dt>

                                    <dd className="text-[13px] leading-5 text-slate-900">
                                        {medication.instructions || '—'}
                                    </dd>
                                </div>
                            </dl>
                        </article>
                    ))}
                </div>
            )}
        </section>
    )
}
