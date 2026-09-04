import type { BeneficiaryDetailsDTO } from '@/api/types'
import { formatAddress } from '@/lib/formatAddress'
import { formatDate } from '@/lib/formatDate'

const RELATIONSHIP_LABELS = {
    PARENT: 'Parent',
    SIBLING: 'Sibling',
    OTHER_RELATIVE: 'Other relative',
    FRIEND: 'Friend',
    SOCIAL_WORKER: 'Social worker',
    OTHER: 'Other',
} as const

function DetailItem({
    label,
    value,
    href,
}: {
    label: string
    value?: string | null
    href?: string
}) {
    const content = value || '—'

    return (
        <div className="space-y-1">
            <dt className="text-[12px] text-slate-500">
                {label}
            </dt>

            <dd className="min-w-0 text-[13px] leading-5 text-slate-900">
                {href && value ? (
                    <a
                        href={href}
                        className="break-words transition-colors hover:text-slate-950"
                    >
                        {content}
                    </a>
                ) : (
                    <span className="break-words">
                        {content}
                    </span>
                )}
            </dd>
        </div>
    )
}

type Props = {
    beneficiary: BeneficiaryDetailsDTO
}

export function BeneficiaryMetadataRail({
    beneficiary,
}: Props) {
    const emergencyContact = beneficiary.emergencyContact

    const emergencyContactName = emergencyContact
        ? `${emergencyContact.firstName ?? ''} ${emergencyContact.lastName ?? ''
            }`.trim() || undefined
        : undefined

    const relationshipLabel = emergencyContact?.relationshipType
        ? RELATIONSHIP_LABELS[
        emergencyContact.relationshipType as keyof typeof RELATIONSHIP_LABELS
        ] ?? emergencyContact.relationshipType
        : undefined

    return (
        <aside className="self-start pt-2">
            <div className="space-y-7 border-l border-slate-100 pl-5">
                <h2 className="text-[13px] font-medium text-slate-700">
                    Details
                </h2>

                <section className="space-y-4">
                    <h2 className="text-[13px] font-medium text-slate-700">
                        Personal
                    </h2>

                    <dl className="space-y-4">
                        <DetailItem
                            label="AMKA"
                            value={beneficiary.amka}
                        />

                        <DetailItem
                            label="Date of birth"
                            value={formatDate(beneficiary.dateOfBirth)}
                        />
                    </dl>
                </section>

                <div className="border-t border-slate-100" />

                <section className="space-y-4">
                    <h2 className="text-[13px] font-medium text-slate-700">
                        Permanent address
                    </h2>

                    <dl className="space-y-4">
                        <DetailItem
                            label="Address"
                            value={formatAddress(
                                beneficiary.permanentAddress
                            )}
                        />
                    </dl>
                </section>

                <div className="border-t border-slate-100" />

                <section className="space-y-4">
                    <h2 className="text-[13px] font-medium text-slate-700">
                        Emergency contact
                    </h2>

                    <dl className="space-y-4">
                        <DetailItem
                            label="Name"
                            value={emergencyContactName}
                        />

                        <DetailItem
                            label="Relationship"
                            value={relationshipLabel}
                        />

                        <DetailItem
                            label="Mobile"
                            value={emergencyContact?.mobileNumber}
                            href={
                                emergencyContact?.mobileNumber
                                    ? `tel:${emergencyContact.mobileNumber}`
                                    : undefined
                            }
                        />

                        <DetailItem
                            label="Landline"
                            value={emergencyContact?.landlinePhone}
                            href={
                                emergencyContact?.landlinePhone
                                    ? `tel:${emergencyContact.landlinePhone}`
                                    : undefined
                            }
                        />

                        <DetailItem
                            label="Email"
                            value={emergencyContact?.email}
                            href={
                                emergencyContact?.email
                                    ? `mailto:${emergencyContact.email}`
                                    : undefined
                            }
                        />

                        <DetailItem
                            label="Address"
                            value={formatAddress(
                                emergencyContact?.address
                            )}
                        />
                    </dl>
                </section>
            </div>
        </aside>
    )
}