import type { EmployeeDetailsDTO } from '@/api/types'
import { formatDate, formatDateRange } from '@/lib/formatDate'

type Assignment = NonNullable<EmployeeDetailsDTO['assignments']>[number]
type EmployeeAssignmentsSectionProps = {
    assignments: Assignment[]
}

function formatAssignmentStatus(status: Assignment['status']) {
    if (!status) return 'Unknown'

    return status.charAt(0) + status.slice(1).toLowerCase()
}

export function EmployeeAssignmentsSection({ assignments }: EmployeeAssignmentsSectionProps) {
    const sortedAssignments = [...assignments].sort((a, b) => {
        return (b.startDate ?? '').localeCompare(a.startDate ?? '')
    })

    const currentAssignment = sortedAssignments.find((assignment) => assignment.status === 'ACTIVE')

    const pastAssignments = sortedAssignments.filter((assignment) => assignment.status !== 'ACTIVE')
    return (
        <section className="max-w-xl space-y-9">
            <div className="space-y-3">
                <h2 className="text-[14px] font-medium text-slate-700">
                    Current assignment
                </h2>

                <div className="pt-4">
                    {currentAssignment ? (
                        <div className="grid grid-cols-[minmax(0,1fr)_auto] gap-4">
                            <div>
                                <p className="text-[14px] font-medium text-slate-900">
                                    {currentAssignment.houseUnitDisplayName}
                                </p>

                                <dl className="mt-3 grid gap-2 text-[13px]">
                                    <div className="grid grid-cols-[7rem_minmax(0,1fr)] gap-3">
                                        <dt className="text-slate-400">Type</dt>
                                        <dd className="text-slate-700">
                                            Official house unit
                                        </dd>
                                    </div>

                                    <div className="grid grid-cols-[7rem_minmax(0,1fr)] gap-3">
                                        <dt className="text-slate-400">Started</dt>
                                        <dd className="text-slate-700">
                                            {formatDate(currentAssignment.startDate)}
                                        </dd>
                                    </div>

                                    {currentAssignment.endDate && (
                                        <div className="grid grid-cols-[7rem_minmax(0,1fr)] gap-3">
                                            <dt className="text-slate-400">
                                                Scheduled end
                                            </dt>
                                            <dd className="text-slate-700">
                                                {formatDate(currentAssignment.endDate)}
                                            </dd>
                                        </div>
                                    )}
                                </dl>
                            </div>

                            <span className="h-fit rounded-full bg-teal-50 px-2 py-0.5 text-[11px] font-medium text-teal-700">
                                {formatAssignmentStatus(currentAssignment.status)}
                            </span>
                        </div>
                    ) : (
                        <p className="text-[13px] text-slate-500">
                            No active assignment recorded.
                        </p>
                    )}
                </div>
            </div>

            <div className="space-y-3 border-t border-slate-100 pt-9">
                <div>
                    <h3 className="text-[14px] font-medium text-slate-700">
                        Assignment history
                    </h3>
                </div>

                {pastAssignments.length > 0 ? (
                    <div className="divide-y divide-slate-100">
                        {pastAssignments.map((assignment) => (
                            <div
                                key={assignment.publicId}
                                className="grid grid-cols-[minmax(0,1fr)_auto] gap-4 py-3"
                            >
                                <div>
                                    <p className="text-[13px] font-medium text-slate-900">
                                        {assignment.houseUnitDisplayName}
                                    </p>

                                    <p className="mt-1 text-[13px] text-slate-500">
                                        {formatDateRange(assignment.startDate, assignment.endDate)}
                                    </p>
                                </div>

                                <span className="h-fit rounded-full bg-slate-50 px-2 py-0.5 text-[11px] font-medium text-slate-600">
                                    {formatAssignmentStatus(assignment.status)}
                                </span>
                            </div>
                        ))}
                    </div>
                ) : (
                    <p className="border-t border-slate-100 pt-4 text-[13px] text-slate-500">
                        No previous assignments recorded.
                    </p>
                )}
            </div>
        </section>
    )
}
