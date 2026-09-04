import type { BeneficiaryDetailsDTO } from '@/api/types'
import { formatDate } from '@/lib/formatDate'

type Props = {
    beneficiary: BeneficiaryDetailsDTO
}

function DetailItem({
    label,
    value,
}: {
    label: string
    value?: string | null
}) {
    return (
        <div className="space-y-1">
            <dt className="text-[12px] text-slate-500">
                {label}
            </dt>

            <dd className="text-[13px] leading-5 text-slate-900">
                {value || '—'}
            </dd>
        </div>
    )
}

export function BeneficiaryOverviewSection({
    beneficiary,
}: Props) {
    return (
        <section className="max-w-xl space-y-6">
            <div>
                <h2 className="text-[14px] font-medium text-slate-900">
                    Living arrangement
                </h2>

                <p className="mt-1 text-[12px] text-slate-400">
                    Current supported living placement.
                </p>
            </div>

            <dl>
                <DetailItem
                    label="House unit"
                    value={
                        beneficiary.houseUnitDisplayName ??
                        beneficiary.houseUnitCode
                    }
                />
            </dl>

            {!beneficiary.isActive && (
                <>
                    <div className="border-t border-slate-100" />

                    <div className="space-y-5">
                        <div>
                            <h2 className="text-[14px] font-medium text-slate-900">
                                Discharge
                            </h2>

                            <p className="mt-1 text-[12px] text-slate-400">
                                Recorded discharge information.
                            </p>
                        </div>

                        <dl className="grid gap-5 sm:grid-cols-2">
                            <DetailItem
                                label="Discharge date"
                                value={formatDate(
                                    beneficiary.dischargeDate
                                )}
                            />

                            <DetailItem
                                label="Recorded by"
                                value={
                                    beneficiary.dischargedByEmployeeFullName
                                }
                            />

                            <div className="sm:col-span-2">
                                <DetailItem
                                    label="Reason"
                                    value={beneficiary.dischargeReason}
                                />
                            </div>
                        </dl>
                    </div>
                </>
            )}
        </section>
    )
}