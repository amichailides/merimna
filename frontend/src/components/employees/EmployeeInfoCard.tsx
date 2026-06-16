import type { ReactNode } from 'react'
import {
    Card,
    CardContent,
    CardDescription,
    CardHeader,
    CardTitle,
} from '@/components/ui/card'

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
        <Card className={`rounded-xl border-slate-200 bg-white shadow-sm ${className ?? ''}`}>
            <CardHeader className="pb-3">
                <div className="flex items-start gap-3">
                    {icon && (
                        <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-teal-50 text-teal-700 ring-1 ring-teal-100">
                            {icon}
                        </div>
                    )}

                    <div>
                        <CardTitle className="text-base font-semibold text-slate-900">
                            {title}
                        </CardTitle>

                        {description && (
                            <CardDescription className="mt-1 text-sm text-slate-500">
                                {description}
                            </CardDescription>
                        )}
                    </div>
                </div>
            </CardHeader>

            <CardContent className="pt-0">
                {children}
            </CardContent>
        </Card>
    )
}