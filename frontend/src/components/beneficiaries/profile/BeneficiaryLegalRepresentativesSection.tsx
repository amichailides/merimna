import type { BeneficiaryDetailsDTO } from '@/api/types'

type Props = {
    legalRepresentatives: NonNullable<
        BeneficiaryDetailsDTO['legalRepresentatives']
    >
}

const LEGAL_REPRESENTATIVE_TYPE_LABELS = {
    JUDICIAL_SUPPORTER: 'Judicial supporter',
    LEGAL_GUARDIAN: 'Legal guardian',
    PUBLIC_AUTHORITY: 'Public authority',
    OTHER: 'Other',
} as const

function getFullName(firstName?: string, lastName?: string) {
    return `${firstName ?? ''} ${lastName ?? ''}`.trim() || '—'
}

export function BeneficiaryLegalRepresentativesSection({
    legalRepresentatives,
}: Props) {
    if (legalRepresentatives.length === 0) {
        return (
            <p className="py-6 text-[13px] text-slate-400">
                No legal representatives recorded.
            </p>
        )
    }

    return (
        <section className="max-w-2xl space-y-5">
            <div>
                <h2 className="text-[14px] font-medium text-slate-900">
                    Legal representatives
                </h2>

                <p className="mt-1 text-[12px] text-slate-400">
                    Assigned legal representatives and contact details.
                </p>
            </div>

            <div className="divide-y divide-slate-100">
                {legalRepresentatives.map((representative, index) => {
                    const typeLabel = representative.type
                        ? LEGAL_REPRESENTATIVE_TYPE_LABELS[
                        representative.type as keyof typeof LEGAL_REPRESENTATIVE_TYPE_LABELS
                        ] ?? representative.type
                        : '—'

                    return (
                        <article
                            key={
                                representative.id ??
                                `${representative.firstName ?? 'representative'}-${index}`
                            }
                            className="space-y-3 py-4 first:pt-0"
                        >
                            <div className="flex items-center gap-2">
                                <h3 className="text-[13px] font-medium text-slate-900">
                                    {getFullName(
                                        representative.firstName,
                                        representative.lastName
                                    )}
                                </h3>

                                <span className="rounded-full bg-slate-100 px-2 py-0.5 text-[11px] font-medium text-slate-600">
                                    {typeLabel}
                                </span>
                            </div>

                            <dl className="grid gap-4 sm:grid-cols-2">
                                <div className="space-y-1">
                                    <dt className="text-[12px] text-slate-500">
                                        Mobile
                                    </dt>
                                    <dd className="text-[13px] leading-5 text-slate-900">
                                        {representative.mobileNumber ? (
                                            <a
                                                href={`tel:${representative.mobileNumber}`}
                                                className="transition-colors hover:text-slate-950"
                                            >
                                                {representative.mobileNumber}
                                            </a>
                                        ) : (
                                            '—'
                                        )}
                                    </dd>
                                </div>

                                <div className="space-y-1">
                                    <dt className="text-[12px] text-slate-500">
                                        Landline
                                    </dt>
                                    <dd className="text-[13px] leading-5 text-slate-900">
                                        {representative.landlinePhone ? (
                                            <a
                                                href={`tel:${representative.landlinePhone}`}
                                                className="transition-colors hover:text-slate-950"
                                            >
                                                {representative.landlinePhone}
                                            </a>
                                        ) : (
                                            '—'
                                        )}
                                    </dd>
                                </div>

                                <div className="space-y-1 sm:col-span-2">
                                    <dt className="text-[12px] text-slate-500">
                                        Email
                                    </dt>
                                    <dd className="text-[13px] leading-5 text-slate-900">
                                        {representative.email ? (
                                            <a
                                                href={`mailto:${representative.email}`}
                                                className="break-words transition-colors hover:text-slate-950"
                                            >
                                                {representative.email}
                                            </a>
                                        ) : (
                                            '—'
                                        )}
                                    </dd>
                                </div>

                                <div className="space-y-1 sm:col-span-2">
                                    <dt className="text-[12px] text-slate-500">
                                        Notes
                                    </dt>
                                    <dd className="text-[13px] leading-5 text-slate-900">
                                        {representative.notes || '—'}
                                    </dd>
                                </div>
                            </dl>
                        </article>
                    )
                })}
            </div>
        </section>
    )
}
