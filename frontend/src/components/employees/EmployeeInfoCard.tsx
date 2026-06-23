import type { ReactNode } from 'react'

type EmployeeInfoCardProps = {
    title: string
    description: string
    icon: ReactNode
    children: ReactNode
    className?: string
}

export function EmployeeInfoCard({
    title,
    description,
    icon,
    children,
    className,
}: EmployeeInfoCardProps) {
    return (
        <div
            className={`space-y-3 rounded-lg p-4 shadow-[inset_1px_0_0_theme(colors.slate.200),inset_0_-1px_0_theme(colors.slate.200)] ${className ?? ''}`}
        >
            <div className="flex items-start gap-2">
                <span className="mt-0.5 text-slate-400">
                    {icon}
                </span>

                <div>
                    <h2 className="text-sm font-medium text-slate-950">
                        {title}
                    </h2>

                    <p className="mt-0.5 text-xs text-slate-400">
                        {description}
                    </p>
                </div>
            </div>

            {children}
        </div>
    )
}