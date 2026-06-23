import type { EmployeeDetailsDTO } from '@/api/types'
import { formatDate } from '@/lib/formatDate'
import { Button } from '@/components/ui/button'

type EmployeeWorkDetailsSectionProps = {
    assignments: EmployeeDetailsDTO['assignments']
    placement?: EmployeeDetailsDTO['activePlacement']
}

export function EmployeeWorkDetailsSection({
    assignments,
    placement,
}: EmployeeWorkDetailsSectionProps) {
    const activeAssignment = assignments.find(
        (assignment) => assignment.status === 'ACTIVE'
    )

    return (
        <section className="max-w-2xl space-y-3">
            <div className="flex items-start justify-between gap-4">
                <div>
                    <h2 className="text-sm font-medium text-slate-950">
                        Work details
                    </h2>

                    <p className="mt-1 text-xs text-slate-400">
                        Assignment and placement information for this employee.
                    </p>
                </div>

                <Button
                    variant="ghost"
                    size="sm"
                    disabled
                    className="h-7 px-2 text-xs font-normal text-slate-400"
                >
                    Edit
                </Button>
            </div>

            <div className="max-w-xl rounded-xl border border-slate-100 bg-slate-50/40 px-4 py-3">
                <div className="grid grid-cols-[8.5rem_1fr] gap-x-6 gap-y-4">
                    <p className="text-[12px] font-medium text-slate-400">
                        Assignment
                    </p>

                    <div>
                        {!activeAssignment ? (
                            <p className="text-[13px] text-slate-400">
                                No active assignment
                            </p>
                        ) : (
                            <>
                                <p className="text-[13px] font-medium text-slate-800">
                                    {activeAssignment.houseUnitDisplayName}
                                </p>

                                <p className="mt-0.5 text-[12px] text-slate-500">
                                    {formatDate(activeAssignment.startDate)}
                                    <span className="mx-1.5 text-slate-300">–</span>
                                    {activeAssignment.endDate
                                        ? formatDate(activeAssignment.endDate)
                                        : 'Open-ended'}
                                </p>
                            </>
                        )}
                    </div>

                    <p className="text-[12px] font-medium text-slate-400">
                        Current placement
                    </p>

                    <div>
                        {!placement ? (
                            <p className="text-[13px] text-slate-400">
                                No active placement
                            </p>
                        ) : (
                            <>
                                <p className="text-[13px] font-medium text-slate-800">
                                    {placement.houseUnitDisplayName}
                                </p>

                                <p className="mt-0.5 text-[12px] text-slate-500">
                                    {placement.reasonDisplayName}
                                </p>

                                <p className="mt-0.5 text-[12px] text-slate-500">
                                    {formatDate(placement.startDate)}
                                    <span className="mx-1.5 text-slate-300">–</span>
                                    {placement.endDate
                                        ? formatDate(placement.endDate)
                                        : 'Open-ended'}
                                </p>
                            </>
                        )}
                    </div>
                </div>
            </div>
        </section>
    )
}