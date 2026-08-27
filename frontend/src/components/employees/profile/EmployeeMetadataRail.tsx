
import type { EmployeeDetailsDTO } from '@/api/types'
import { formatAddress } from '@/lib/formatAddress'
import { formatDate } from '@/lib/formatDate'
import {
    FloatingPanelContent,
    FloatingPanelRoot,
    FloatingPanelTrigger,
} from '@/components/ui/floating-panel'
import { EmployeeMetadataEditForm } from '../edit/EmployeeMetadataEditForm'

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
    onEmployeeUpdated?: () => void | Promise<void>
}

export function EmployeeMetadataRail({
    employee,
    onEmployeeUpdated }: Props) {

    return (
        <aside className="self-start pt-2">
            <FloatingPanelRoot>
                <div className="space-y-7 border-l border-slate-100 pl-5">
                    <div className="flex items-center justify-between gap-4">
                        <h2 className="text-[13px] font-medium text-slate-700">
                            Details
                        </h2>

                        <FloatingPanelTrigger
                            title="Edit employee details"
                            className="h-auto border-0 bg-transparent px-0 text-[12px] font-medium text-slate-400 shadow-none hover:text-slate-700"
                        >
                            Edit
                        </FloatingPanelTrigger>
                    </div>

                    <FloatingPanelContent align="center" className="w-[420px] rounded-xl border border-slate-200 bg-white shadow-sm">
                        <EmployeeMetadataEditForm
                            employee={employee}
                            onEmployeeUpdated={onEmployeeUpdated}
                        />
                    </FloatingPanelContent>

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
            </FloatingPanelRoot>
        </aside>
    )
}