import type { EmployeeListDTO } from '@/api/types'

import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { Badge } from '@/components/ui/badge'
import { Button } from '@/components/ui/button'

type EmployeeListRowProps = {
    employee: EmployeeListDTO
}

function getInitials(firstName?: string, lastName?: string) {
    return `${firstName?.charAt(0) ?? ''}${lastName?.charAt(0) ?? ''}`.toUpperCase()
}

export function EmployeeListRow({ employee }: EmployeeListRowProps) {
    return (
        <div className="flex items-center gap-4 border-b px-4 py-3 transition-colors last:border-b-0 hover:bg-slate-50">
            <Avatar className="h-9 w-9">
                <AvatarFallback className="bg-teal-100 text-xs font-semibold text-teal-800">
                    {getInitials(employee.firstName, employee.lastName)}
                </AvatarFallback>
            </Avatar>

            <div className="min-w-0 flex-1">
                <p className="truncate text-sm font-medium text-slate-900">
                    {employee.firstName} {employee.lastName}
                </p>

                <p className="mt-0.5 truncate text-xs text-slate-500">
                    {employee.positionDisplayName ?? employee.positionCode}
                </p>
            </div>

            <Badge
                variant="outline"
                className={
                    employee.active
                        ? 'border-emerald-200 bg-emerald-50 text-emerald-700'
                        : 'border-slate-200 bg-slate-50 text-slate-600'
                }
            >
                {employee.active ? 'Active' : 'Inactive'}
            </Badge>

            <Button
                type="button"
                variant="ghost"
                size="sm"
                className="text-slate-600 hover:bg-teal-50 hover:text-teal-800"
            >
                View
            </Button>
        </div>
    )
}