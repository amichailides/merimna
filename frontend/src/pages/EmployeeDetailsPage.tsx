import { Link, useParams } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'

import { EmployeeProfileHeader } from '@/components/employees/EmployeeProfileHeader'
import { EmployeeRecentActivitySection } from '@/components/employees/EmployeeRecentActivitySection'
import { useEmployeeDetails } from '@/api/useEmployeeDetails'
import { useEmployeeActivity } from '@/api/useEmployeeActivity'
import { EmployeeWorkDetailsSection } from '@/components/employees/EmployeeWorkDetailsSection'

const PROFILE_TABS = ['Overview', 'Assignments', 'Placements', 'Activity'] as const

function formatAddress(address: unknown) {
    if (!address || typeof address !== 'object') {
        return '—'
    }

    const values = Object.values(address as Record<string, string | null | undefined>)
        .filter((value): value is string => Boolean(value && value.trim()))

    return values.length > 0 ? values.join(', ') : '—'
}

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

export function EmployeeDetailsPage() {
    const { publicId } = useParams<{ publicId: string }>()

    const {
        employee,
        loading,
        error,
    } = useEmployeeDetails(publicId)

    const {
        activities,
        loading: activityLoading,
        error: activityError,
    } = useEmployeeActivity(publicId)

    if (loading) {
        return (
            <div className="text-[13px] text-slate-500">
                Loading employee…
            </div>
        )
    }

    if (error || !employee) {
        return (
            <div className="text-[13px] text-slate-500">
                {error ?? 'Employee not found'}
            </div>
        )
    }

    return (
        <div className="max-w-6xl space-y-7">
            <Link
                to="/employees"
                className="inline-flex items-center gap-1.5 text-[13px] text-slate-500 transition-colors hover:text-slate-950"
            >
                <ArrowLeft size={13} />
                Back to employees
            </Link>

            <div>
                <EmployeeProfileHeader employee={employee} />

                <div className="mt-3 border-b border-slate-200">
                    <div className="flex items-center gap-6">
                        {PROFILE_TABS.map((tab) => {
                            const active = tab === 'Overview'

                            return (
                                <button
                                    key={tab}
                                    type="button"
                                    className={[
                                        'relative pb-3 text-sm leading-none transition-colors',
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
                <main className="space-y-8">
                    <EmployeeWorkDetailsSection
                        assignments={employee.assignments}
                        placement={employee.activePlacement}
                    />

                    <div className="max-w-xl border-t border-slate-100" />

                    {employee.publicId && (
                        <section>
                            <EmployeeRecentActivitySection
                                activities={activities}
                                loading={activityLoading}
                                error={activityError}
                            />
                        </section>
                    )}
                </main>

                <aside className="pt-[3.75rem]">
                    <div className="space-y-8 border-l border-slate-100 pl-5">
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
                                Employment
                            </h2>

                            <dl className="space-y-4">
                                <DetailItem
                                    label="Position"
                                    value={employee.positionDisplayName}
                                />

                                <DetailItem
                                    label="Hire date"
                                    value={employee.hireDate}
                                />
                            </dl>
                        </section>
                    </div>
                </aside>
            </div>
        </div>
    )
}