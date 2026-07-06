import type { EmployeeDetailsDTO } from '@/api/types'
import { formatAddress } from '@/lib/formatAddress'
import { formatDate } from '@/lib/formatDate'
import {
    FloatingPanelBody,
    FloatingPanelContent,
    FloatingPanelFooter,
    FloatingPanelForm,
    FloatingPanelHeader,
    FloatingPanelRoot,
    FloatingPanelTrigger,
} from '@/components/ui/floating-panel'

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
                    <div className="flex items-center justify-between gap-4">
                        <h2 className="text-[13px] font-medium text-slate-700">
                            Contact
                        </h2>

                        <FloatingPanelRoot>
                            <FloatingPanelTrigger
                                title="Edit contact"
                                className="h-auto border-0 bg-transparent px-0 text-[12px] font-medium text-slate-400 shadow-none hover:text-slate-700"
                            >
                                Edit
                            </FloatingPanelTrigger>

                            <FloatingPanelContent align="center" className="w-[360px] rounded-xl border border-slate-200 bg-white shadow-sm">
                                <FloatingPanelForm
                                    className="min-h-[320px]"
                                    onSubmit={() => {
                                        console.log('save contact')
                                    }}
                                >
                                    <FloatingPanelHeader className="flex items-center justify-between border-b border-slate-100 px-4 py-3">
                                        <div>
                                            <div className="text-[13px] font-medium text-slate-800">
                                                Edit contact
                                            </div>
                                            <p className="mt-0.5 text-[12px] font-normal text-slate-400">
                                                Update this employee’s contact details.
                                            </p>
                                        </div>                                  
                                    </FloatingPanelHeader>

                                    <FloatingPanelBody className="space-y-4 px-4 py-4">
                                        <label className="block space-y-1.5">
                                            <span className="text-[11px] text-slate-400">Email</span>
                                            <input
                                                defaultValue={employee.contactEmail ?? ''}
                                                className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                                            />
                                        </label>

                                        <label className="block space-y-1.5">
                                            <span className="text-[11px] text-slate-400">Mobile</span>
                                            <input
                                                defaultValue={employee.mobileNumber ?? ''}
                                                className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                                            />
                                        </label>

                                        <label className="block space-y-1.5">
                                            <span className="text-[11px] text-slate-400">Address</span>
                                            <input
                                                defaultValue={formatAddress(employee.address) ?? ''}
                                                className="h-9 w-full rounded-lg border border-slate-200 px-3 text-[13px] text-slate-800 outline-none focus:border-slate-300"
                                            />
                                        </label>
                                    </FloatingPanelBody>

                                    <FloatingPanelFooter className="mt-auto justify-end gap-2 border-t border-slate-100 px-4 py-3">
                                        <button
                                            type="button"
                                            className="rounded-lg px-3 py-1.5 text-[13px] font-medium text-slate-500 hover:text-slate-800"
                                        >
                                            Cancel
                                        </button>

                                        <button
                                            type="submit"
                                            className="rounded-lg bg-teal-600 px-3 py-1.5 text-[13px] font-medium text-white hover:bg-teal-700"
                                        >
                                            Save changes
                                        </button>
                                    </FloatingPanelFooter>
                                </FloatingPanelForm>
                            </FloatingPanelContent>
                        </FloatingPanelRoot>
                    </div>

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