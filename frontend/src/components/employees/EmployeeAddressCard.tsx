import type { EmployeeDetailsDTO } from '@/api/types'
import { Card, CardContent } from '@/components/ui/card'
import { MapPinned } from 'lucide-react'

type EmployeeAddressCardProps = {
    address: EmployeeDetailsDTO['address']
    className?: string
}

export function EmployeeAddressCard({
    address,
    className,
}: EmployeeAddressCardProps) {
    const streetLine = [address?.street, address?.streetNumber]
        .filter(Boolean)
        .join(' ')

    const cityLine = [address?.city, address?.zipCode]
        .filter(Boolean)
        .join(', ')

    return (
        <Card className={`border-slate-200 bg-white shadow-sm ${className ?? ''}`}>
            <CardContent className="flex items-center gap-4 p-4">
                <div className="flex h-9 w-9 shrink-0 items-center justify-center rounded-full bg-teal-50 text-teal-700 ring-1 ring-teal-100">
                    <MapPinned className="h-4 w-4" />
                </div>

                <div className="w-28 shrink-0">
                    <h3 className="text-base font-semibold text-slate-900">
                        Address
                    </h3>
                </div>

                {!streetLine && !cityLine ? (
                    <div>
                        <p className="text-sm font-medium text-slate-900">
                            No address available
                        </p>

                        <p className="mt-1 text-sm text-slate-500">
                            No address information has been recorded for this employee.
                        </p>
                    </div>
                ) : (
                    <div className="text-sm leading-6 text-slate-700">
                        {streetLine && (
                            <p className="font-medium text-slate-900">
                                {streetLine}
                            </p>
                        )}

                        {cityLine && (
                            <p className="text-slate-600">
                                {cityLine}
                            </p>
                        )}
                    </div>
                )}
            </CardContent>
        </Card>
    )
}