import type { ReactNode } from 'react'

type EmployeeInfoCardProps = {
    title: string
    description?: string
    children: ReactNode
    className?: string
    icon?: ReactNode
}

export function EmployeeInfoCard({
    title,
    description,
    children,
    className,
    icon,
}: EmployeeInfoCardProps) {
    return (
        <div className={`space-y-3 ${className ?? ''}`}>
            <div className="flex items-center gap-2">
                {icon && (
                    <span className="text-slate-400">
                        {icon}
                    </span>
                )}
                <div>
                    <p className="text-[13px] font-medium text-slate-900">
                        {title}
                    </p>
                    {description && (
                        <p className="text-[11px] text-slate-400">
                            {description}
                        </p>
                    )}
                </div>
            </div>

            {children}
        </div>
    )
}