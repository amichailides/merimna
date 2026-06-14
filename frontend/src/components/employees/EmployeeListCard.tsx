import type { EmployeeListDTO } from '@/api/types'

import { Badge } from '@/components/ui/badge'
import { Card, CardContent } from '@/components/ui/card'

type EmployeeCardProps = {
    employee: EmployeeListDTO
}

export function EmployeeListCard({ employee }: EmployeeCardProps) {
    return (
        <Card>
            <CardContent className="px-4 py-2.5">
                <div className="flex items-center justify-between gap-4">
                    <div>
                        <h2 className="text-sm font-medium leading-5 text-slate-900">
                            {employee.firstName} {employee.lastName}
                        </h2>

                        <p className="mt-0.5 text-xs leading-4 text-slate-500">
                            {employee.positionCode}
                        </p>
                    </div>

                    <Badge variant={employee.active ? 'default' : 'secondary'}>
                        {employee.active ? 'Active' : 'Inactive'}
                    </Badge>
                </div>
            </CardContent>
        </Card>
    )
}