import type { EmployeeDetailsDTO } from '@/api/types'
import { Badge } from '@/components/ui/badge'
import { Card, CardContent, CardHeader, CardTitle } from '@/components/ui/card'

type EmployeeDetailsCardProps = {
    employee: EmployeeDetailsDTO
}

export function EmployeeDetailsCard({ employee }: EmployeeDetailsCardProps) {
    return (
        <Card>
            <CardHeader className="flex flex-row items-start justify-between gap-4">
                <div>
                    <CardTitle>
                        {employee.firstName} {employee.lastName}
                    </CardTitle>

                    <p className="mt-1 text-sm text-slate-500">
                        {employee.positionDisplayName}
                    </p>
                </div>

                <Badge variant={employee.active ? 'default' : 'secondary'}>
                    {employee.active ? 'Active' : 'Inactive'}
                </Badge>
            </CardHeader>

            <CardContent>
                <dl className="grid gap-4 sm:grid-cols-2">
                    <DetailItem label="Email" value={employee.contactEmail} />
                    <DetailItem label="Mobile" value={employee.mobileNumber} />
                    <DetailItem label="Hire date" value={employee.hireDate} />
                    <DetailItem label="Position code" value={employee.positionCode} />
                </dl>

                {employee.activePlacement && (
                    <div className="mt-6 border-t pt-6">
                        <h3 className="text-sm font-semibold text-slate-900">
                            Current placement
                        </h3>

                        <dl className="mt-4 grid gap-4 sm:grid-cols-2">
                            <DetailItem
                                label="House unit"
                                value={employee.activePlacement.houseUnitDisplayName}
                            />

                            <DetailItem
                                label="Reason"
                                value={employee.activePlacement.reasonDisplayName}
                            />

                            <DetailItem
                                label="Start date"
                                value={employee.activePlacement.startDate}
                            />

                            <DetailItem
                                label="End date"
                                value={employee.activePlacement.endDate}
                            />

                            <DetailItem
                                label="Active"
                                value={employee.activePlacement.active ? 'Yes' : 'No'}
                            />
                        </dl>
                    </div>
                )}
            </CardContent>
        </Card>
    )
}

function DetailItem({
    label,
    value,
}: {
    label: string
    value: string | null | undefined
}) {
    return (
        <div className="space-y-1">
            <dt className="text-xs font-medium uppercase tracking-wide text-slate-500">
                {label}
            </dt>
            <dd className="text-sm text-slate-900">
                {value || '—'}
            </dd>
        </div>
    )
}