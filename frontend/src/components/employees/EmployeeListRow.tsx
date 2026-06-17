import { Link } from 'react-router-dom'
import { ChevronRight } from 'lucide-react'

import type { EmployeeListDTO } from '@/api/types'

type EmployeeListRowProps = {
    employee: EmployeeListDTO
}

function getInitials(firstName: string, lastName: string) {
    return `${firstName.charAt(0)}${lastName.charAt(0)}`.toUpperCase()
}

export function EmployeeListRow({ employee }: EmployeeListRowProps) {
    return (
        <Link
            to={`/employees/${employee.publicId}`}
            className="group flex items-center gap-3.5 px-1.5 py-3 border-b border-slate-100 last:border-b-0 rounded-md transition-all duration-150 hover:bg-teal-50/50 hover:translate-x-0.5"
        >
            <div
                className={`
                    h-[34px] w-[34px] rounded-full flex items-center justify-center
                    text-[11px] font-medium flex-shrink-0 transition-transform duration-150
                    group-hover:scale-105
                    ${employee.active
                        ? 'bg-teal-50 text-teal-800'
                        : 'bg-slate-100 text-slate-400'
                    }
                `}
            >
                {getInitials(employee.firstName, employee.lastName)}
            </div>

            <div className="flex-1 min-w-0">
                <p className={`
                    text-[13px] font-medium truncate transition-colors duration-150
                    ${employee.active
                        ? 'text-slate-900 group-hover:text-teal-700'
                        : 'text-slate-400'
                    }
                `}>
                    {employee.firstName} {employee.lastName}
                </p>
                <p className="text-[11px] text-slate-400 mt-0.5 truncate">
                    {employee.positionDisplayName ?? employee.positionCode}
                </p>
            </div>

            <span className={`inline-flex items-center gap-1 rounded-full px-2 py-0.5 text-[11px] font-medium ${employee.active
                    ? 'bg-emerald-50 text-emerald-700'
                    : 'bg-slate-100 text-slate-500'
                }`}>
                {employee.active && (
                    <span className="h-1.5 w-1.5 rounded-full bg-emerald-500" />
                )}
                {employee.active ? 'Active' : 'Inactive'}
            </span>

            <ChevronRight className="h-3.5 w-3.5 text-slate-300 opacity-0 -translate-x-1 transition-all duration-150 group-hover:opacity-100 group-hover:translate-x-0 group-hover:text-slate-400 flex-shrink-0" />
        </Link>
    )
}