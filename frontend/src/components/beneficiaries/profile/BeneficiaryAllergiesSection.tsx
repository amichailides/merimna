import type { BeneficiaryDetailsDTO } from '@/api/types'

type Props = {
    allergies: NonNullable<BeneficiaryDetailsDTO['allergies']>
}

const SEVERITY_LABELS = {
    LOW: 'Low',
    MEDIUM: 'Medium',
    HIGH: 'High',
} as const

export function BeneficiaryAllergiesSection({
    allergies,
}: Props) {
    if (allergies.length === 0) {
        return (
            <p className="py-6 text-[13px] text-slate-400">
                No allergies recorded.
            </p>
        )
    }

    return (
        <section className="max-w-2xl space-y-5">
            <div>
                <h2 className="text-[14px] font-medium text-slate-900">
                    Allergies
                </h2>

                <p className="mt-1 text-[12px] text-slate-400">
                    Known allergies and recorded reactions.
                </p>
            </div>

            <div className="divide-y divide-slate-100">
                {allergies.map((allergy, index) => {
                    const severityLabel = allergy.severity
                        ? SEVERITY_LABELS[
                        allergy.severity as keyof typeof SEVERITY_LABELS
                        ] ?? allergy.severity
                        : '—'

                    return (
                        <article
                            key={
                                allergy.publicId ??
                                `${allergy.substance ?? 'allergy'}-${index}`
                            }
                            className="space-y-3 py-4 first:pt-0"
                        >
                            <div className="flex items-center gap-2">
                                <h3 className="text-[13px] font-medium text-slate-900">
                                    {allergy.substance || '—'}
                                </h3>

                                <span className="rounded-full bg-slate-100 px-2 py-0.5 text-[11px] font-medium text-slate-600">
                                    {severityLabel}
                                </span>
                            </div>

                            <div className="space-y-1">
                                <div className="text-[12px] text-slate-500">
                                    Reaction
                                </div>

                                <div className="text-[13px] leading-5 text-slate-900">
                                    {allergy.reaction || '—'}
                                </div>
                            </div>
                        </article>
                    )
                })}
            </div>
        </section>
    )
}
