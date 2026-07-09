import { BriefcaseBusiness, History, MoreHorizontal, UserX } from 'lucide-react'

import type { EmployeeDetailsDTO } from '@/api/types'
import {
    DropdownMenu,
    DropdownMenuContent,
    DropdownMenuItem,
    DropdownMenuSeparator,
    DropdownMenuTrigger,
} from '@/components/ui/dropdown-menu'

type Props = {
    employee: EmployeeDetailsDTO
    onViewActivity?: () => void
    onManageAssignments?: () => void
    onEmployeeUpdated?: () => void | Promise<void>
}

export function EmployeeProfileActionsMenu({
    employee,
    onViewActivity,
    onManageAssignments,
}: Props) {
    return (
        <DropdownMenu>
            <DropdownMenuTrigger asChild>
                <button
                    type="button"
                    className="inline-flex h-9 w-9 items-center justify-center rounded-lg border border-slate-200 bg-white text-slate-500 transition-colors hover:border-slate-300 hover:bg-slate-50 hover:text-slate-950"
                    aria-label="Employee actions"
                >
                    <MoreHorizontal size={16} strokeWidth={2.25} />
                </button>
            </DropdownMenuTrigger>

            <DropdownMenuContent
                align="end"
                sideOffset={8}
                className="w-47 rounded-xl border border-slate-200 bg-white p-1.5 shadow-sm ring-0"
            >
                <DropdownMenuItem
                    onSelect={onViewActivity}
                    className="cursor-pointer rounded-lg px-2.5 py-2 text-[13px] font-normal text-slate-600 focus:bg-slate-50 focus:text-slate-800"
                >
                    <History size={14} strokeWidth={2} className="text-slate-400" />
                    View activity
                </DropdownMenuItem>

                <DropdownMenuItem
                    onSelect={onManageAssignments}
                    className="cursor-pointer rounded-lg px-2.5 py-2 text-[13px] font-normal text-slate-600 focus:bg-slate-50 focus:text-slate-800"
                >
                    <BriefcaseBusiness size={14} strokeWidth={2} className="text-slate-400" />
                    Manage assignments
                </DropdownMenuItem>

                <DropdownMenuSeparator className="my-1 bg-slate-100" />

                {employee.active ? (
                    <DropdownMenuItem
                        variant="destructive"
                        className="cursor-pointer rounded-lg px-2.5 py-2 text-[13px] font-normal data-[variant=destructive]:focus:bg-red-50/60 data-[variant=destructive]:text-red-500 data-[variant=destructive]:focus:text-red-600"
                    >
                        <UserX size={14} strokeWidth={2} className="text-red-400" />
                        Deactivate employee
                    </DropdownMenuItem>
                ) : (
                    <DropdownMenuItem className="cursor-pointer rounded-lg px-2.5 py-2 text-[13px] font-medium text-teal-700 focus:bg-teal-50 focus:text-teal-700">
                        Reactivate employee
                    </DropdownMenuItem>
                )}
            </DropdownMenuContent>
        </DropdownMenu>
    )
}