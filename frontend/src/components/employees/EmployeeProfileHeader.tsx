import type { EmployeeDetailsDTO } from '@/api/types'
import { Avatar, AvatarFallback } from '@/components/ui/avatar'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent } from '@/components/ui/card'
import { Mail, Phone, CalendarDays } from 'lucide-react'

type EmployeeProfileHeaderProps = {
    employee: EmployeeDetailsDTO
}

function getInitials(firstName?: string, lastName?: string) {
    return `${firstName?.[0] ?? ''}${lastName?.[0] ?? ''}`.toUpperCase()
}

export function EmployeeProfileHeader({ employee }: EmployeeProfileHeaderProps) {
    return (
        <Card className="border-slate-200 bg-gradient-to-br from-white to-teal-50/40 py-0 shadow-sm">
            <CardContent className="flex items-start justify-between gap-6 p-7">
                <div className="flex items-start gap-4">
                    <Avatar className="h-16 w-16 border border-teal-200 bg-teal-100 text-teal-800 shadow-sm ring-4 ring-white">
                        <AvatarFallback className="bg-teal-50 text-base font-semibold text-teal-700">
                            {getInitials(employee.firstName, employee.lastName)}
                        </AvatarFallback>
                    </Avatar>

                    <div>
                        <h1 className="text-2xl font-semibold tracking-tight text-slate-900">
                            {employee.firstName} {employee.lastName}
                        </h1>

                        <p className="mt-1 text-sm font-semibold text-teal-700">
                            {employee.positionDisplayName}
                        </p>

                        <div className="mt-4 flex flex-wrap items-center gap-x-5 gap-y-2 text-sm text-slate-600">
                            {employee.contactEmail && (
                                <span className="flex items-center gap-1.5">
                                    <Mail className="h-4 w-4 text-slate-400" />
                                    {employee.contactEmail}
                                </span>
                            )}

                            {employee.mobileNumber && (
                                <span className="flex items-center gap-1.5">
                                    <Phone className="h-4 w-4 text-slate-400" />
                                    {employee.mobileNumber}
                                </span>
                            )}

                            {employee.hireDate && (
                                <span className="flex items-center gap-1.5">
                                    <CalendarDays className="h-4 w-4 text-slate-400" />
                                    Hired on {employee.hireDate}
                                </span>
                            )}
                        </div>
                    </div>
                </div>

                <Badge
                    variant="outline"
                    className={
                        employee.active
                            ? 'border-emerald-200 bg-emerald-50 px-2.5 py-0.5 text-xs font-medium text-emerald-700'
                            : 'border-slate-200 bg-slate-50 px-2.5 py-0.5 text-xs font-medium text-slate-600'
                    }
                >
                    {employee.active ? 'Active' : 'Inactive'}
                </Badge>
            </CardContent>
        </Card>
    )
}