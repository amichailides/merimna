import type { EmployeeDetailsDTO } from '@/api/types'
import { EmployeeProfileHeaderEditForm } from '@/components/employees/EmployeeProfileHeaderEditForm'
import {
    FloatingPanelContent,
    FloatingPanelRoot,
    FloatingPanelTrigger,
} from '@/components/ui/floating-panel'
import { Pencil } from 'lucide-react'

type EmployeeProfileHeaderProps = {
    employee: EmployeeDetailsDTO
    onEmployeeUpdated?: () => void | Promise<void>
}

function getInitials(firstName?: string, lastName?: string) {
    return `${firstName?.[0] ?? ''}${lastName?.[0] ?? ''}`.toUpperCase() || '?'
}

export function EmployeeProfileHeader({
    employee,
    onEmployeeUpdated,
}: EmployeeProfileHeaderProps) {
    return (
        <div className="flex items-center justify-between gap-6 pb-6">
            <div className="flex items-center gap-5">
                <div className="flex h-14 w-14 shrink-0 items-center justify-center rounded-full bg-teal-50 text-[15px] font-semibold text-teal-700">
                    {getInitials(employee.firstName, employee.lastName)}
                </div>

                <div className="min-w-0">
                    <div className="flex items-center gap-2.5">
                        <h1 className="text-[19px] font-semibold leading-tight text-slate-950">
                            {employee.firstName} {employee.lastName}
                        </h1>

                        <span
                            className={`inline-flex items-center rounded-full px-2 py-0.5 text-[11px] font-medium ${employee.active
                                ? 'bg-teal-50 text-teal-700'
                                : 'bg-slate-100 text-slate-500'
                                }`}
                        >
                            {employee.active ? 'Active' : 'Inactive'}
                        </span>
                    </div>

                    <p className="mt-1 text-sm text-slate-500">
                        {employee.positionDisplayName}
                    </p>
                </div>
            </div>

            <FloatingPanelRoot>
                <FloatingPanelTrigger
                    title="Edit profile"
                    className="inline-flex h-auto items-center rounded-lg border border-slate-200 bg-white px-3 py-1.5 text-[13px] font-medium text-slate-700 shadow-none transition-colors hover:border-slate-300 hover:bg-slate-50 hover:text-slate-950"
                >
                    <span className="inline-flex items-center gap-1.5">
                        <Pencil size={13} strokeWidth={2.25} className="text-slate-500" />
                        Edit
                    </span>
                </FloatingPanelTrigger>

                <FloatingPanelContent
                    align="center"
                    className="w-[360px] rounded-xl border border-slate-200 bg-white shadow-sm"
                >
                    <EmployeeProfileHeaderEditForm
                        employee={employee}
                        onEmployeeUpdated={onEmployeeUpdated}
                    />
                </FloatingPanelContent>
            </FloatingPanelRoot>

        </div>
    )
}