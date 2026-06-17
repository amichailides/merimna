import type { EmployeeDetailsDTO } from '@/api/types'
import { CalendarDays, Mail, MapPin, Phone } from 'lucide-react'
import { formatAddress } from '@/lib/formatAddress'
import { formatDate } from '@/lib/formatDate'

type EmployeeProfileHeaderProps = {
    employee: EmployeeDetailsDTO
}

function getInitials(firstName?: string, lastName?: string) {
    return `${firstName?.[0] ?? ''}${lastName?.[0] ?? ''}`.toUpperCase() || '?'
}

const dot = <span className="text-slate-400">·</span>

export function EmployeeProfileHeader({ employee }: EmployeeProfileHeaderProps) {
    const addressLine = formatAddress(employee.address)

    return (
        <div className="flex items-start gap-5 pb-5 border-b border-slate-100">
            {/* Avatar */}
            <div className="flex h-14 w-14 shrink-0 items-center justify-center rounded-full bg-teal-100 text-[15px] font-semibold text-teal-700">
                {getInitials(employee.firstName, employee.lastName)}
            </div>

            {/* Info */}
            <div className="min-w-0 pt-1">
                <div className="flex items-center gap-2.5">
                    <h1 className="text-[18px] font-semibold text-slate-950 leading-tight">
                        {employee.firstName} {employee.lastName}
                    </h1>
                    <span className={`inline-flex items-center rounded-full px-2 py-0.5 text-[11px] font-medium ${employee.active
                        ? 'bg-emerald-50 text-emerald-700'
                        : 'bg-slate-100 text-slate-500'
                        }`}>
                        {employee.active ? 'Active' : 'Inactive'}
                    </span>
                </div>

                <p className="mt-0.5 text-[13px] font-medium text-teal-700">
                    {employee.positionDisplayName}
                </p>

                <div className="mt-2.5 space-y-1">
                    <div className="flex items-center gap-3">
                        {employee.contactEmail && (
                            <span className="flex items-center gap-1.5 text-[12px] text-slate-500">
                                <Mail size={11} className="text-slate-400" />
                                {employee.contactEmail}
                            </span>
                        )}
                        {employee.mobileNumber && (
                            <>
                                {dot}
                                <span className="flex items-center gap-1.5 text-[12px] text-slate-500">
                                    <Phone size={11} className="text-slate-400" />
                                    {employee.mobileNumber}
                                </span>
                            </>
                        )}
                        {employee.hireDate && (
                            <>
                                {dot}
                                <span className="flex items-center gap-1.5 text-[12px] text-slate-500">
                                    <CalendarDays size={11} className="text-slate-400" />
                                    Hired {formatDate(employee.hireDate)}
                                </span>
                            </>
                        )}
                    </div>
                    {addressLine && (
                        <div className="flex items-center gap-1.5 text-[12px] text-slate-500">
                            <MapPin size={11} className="text-slate-400" />
                            {addressLine}
                        </div>
                    )}
                </div>
            </div>
        </div>
    )
}