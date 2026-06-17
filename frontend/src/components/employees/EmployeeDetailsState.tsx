import type { ReactNode } from 'react'
import { Link } from 'react-router-dom'
import { ArrowLeft } from 'lucide-react'

type EmployeeDetailsStateProps = {
    icon: ReactNode
    title: string
    description: string
}

export function EmployeeDetailsState({
    icon,
    title,
    description,
}: EmployeeDetailsStateProps) {
    return (
        <div className="space-y-5">
            <Link
                to="/employees"
                className="inline-flex items-center gap-1.5 text-[13px] text-slate-500 hover:text-slate-950 transition-colors"
            >
                <ArrowLeft size={13} />
                Back to employees
            </Link>

            <div className="flex items-center gap-3 py-12 text-center flex-col">
                <div className="flex h-9 w-9 items-center justify-center rounded-full bg-slate-100 text-slate-400">
                    {icon}
                </div>
                <div>
                    <p className="text-[13px] font-semibold text-slate-900">{title}</p>
                    <p className="mt-1 text-[12px] text-slate-400 max-w-sm">
                        {description}
                    </p>
                </div>
            </div>
        </div>
    )
}