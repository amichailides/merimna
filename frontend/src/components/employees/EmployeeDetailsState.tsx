import type { ReactNode } from 'react'
import { Link } from 'react-router-dom'

import { Button } from '@/components/ui/button'
import { Card, CardContent } from '@/components/ui/card'

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
        <div className="space-y-6">
            <Button
                asChild
                variant="ghost"
                size="sm"
                className="-ml-2 text-slate-600 hover:bg-teal-50 hover:text-teal-800"
            >
                <Link to="/admin/employees">← Back to employees</Link>
            </Button>

            <Card className="border-slate-200 bg-white shadow-sm">
                <CardContent className="flex min-h-[260px] flex-col items-center justify-center px-6 py-12 text-center">
                    <div className="mb-4 flex size-12 items-center justify-center rounded-full bg-slate-100 text-slate-500">
                        {icon}
                    </div>

                    <h2 className="text-base font-semibold text-slate-900">{title}</h2>

                    <p className="mt-2 max-w-md text-sm leading-6 text-slate-500">
                        {description}
                    </p>
                </CardContent>
            </Card>
        </div>
    )
}