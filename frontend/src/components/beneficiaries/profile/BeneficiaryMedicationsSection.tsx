import type { BeneficiaryDetailsDTO } from '@/api/types'

type Props = {
    medications: NonNullable<BeneficiaryDetailsDTO['medications']>
}

export function BeneficiaryMedicationsSection({
    medications,
}: Props) {
    if (medications.length === 0) {
        return (
            <p className="py-6 text-[13px] text-slate-400">
                No medications recorded.
            </p>
        )
    }

    return (
        <section className="max-w-2xl space-y-5">
            <div>
                <h2 className="text-[14px] font-medium text-slate-900">
                    Medications
                </h2>

                <p className="mt-1 text-[12px] text-slate-400">
                    Current medication details for this beneficiary.
                </p>
            </div>

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
                                {[medication.dosage, medication.frequency]
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
        </section>
    )
}