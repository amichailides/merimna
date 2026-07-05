import type { EmployeeDetailsDTO } from '@/api/types'
import { formatAddress } from '@/lib/formatAddress'
import { formatDate } from '@/lib/formatDate'

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
                    <span className="break-words">{content}</span>
                )}
            </dd>
        </div>
    )
}

type Props = {
    employee: EmployeeDetailsDTO
}

export function EmployeeMetadataRail({ employee }: Props) {
    return (
        <aside className="self-start pt-2">
            <div className="space-y-7 border-l border-slate-100 pl-5">
                <section className="space-y-4">
                    <h2 className="text-[13px] font-medium text-slate-700">
                        Contact
                    </h2>

                    <dl className="space-y-4">
                        <DetailItem
                            label="Email"
                            value={employee.contactEmail}
                            href={employee.contactEmail ? `mailto:${employee.contactEmail}` : undefined}
                        />

                        <DetailItem
                            label="Mobile"
                            value={employee.mobileNumber}
                            href={employee.mobileNumber ? `tel:${employee.mobileNumber}` : undefined}
                        />

                        <DetailItem
                            label="Address"
                            value={formatAddress(employee.address)}
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
                            value={employee.emergencyContactName}
                        />

                        <DetailItem
                            label="Phone"
                            value={employee.emergencyContactPhoneNumber}
                            href={
                                employee.emergencyContactPhoneNumber
                                    ? `tel:${employee.emergencyContactPhoneNumber}`
                                    : undefined
                            }
                        />
                    </dl>
                </section>

                <div className="border-t border-slate-100" />
                
                <section className="space-y-4">
                    <h2 className="text-[13px] font-medium text-slate-700">
                        Employment
                    </h2>

                    <dl className="space-y-4">
                        <DetailItem
                            label="Hire date"
                            value={formatDate(employee.hireDate)}
                        />

                        <DetailItem
                            label="Date of birth"
                            value={formatDate(employee.dateOfBirth)}
                        />
                    </dl>
                </section>
            </div>
        </aside>
    )
}