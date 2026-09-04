import type { BeneficiaryDetailsDTO } from '@/api/types'

type BeneficiaryProfileHeaderProps = {
    beneficiary: BeneficiaryDetailsDTO
}

function getInitials(firstName?: string, lastName?: string) {
    return `${firstName?.[0] ?? ''}${lastName?.[0] ?? ''}`.toUpperCase() || '?'
}

export function BeneficiaryProfileHeader({
    beneficiary,
}: BeneficiaryProfileHeaderProps) {
    const fullName =
        `${beneficiary.firstName ?? ''} ${beneficiary.lastName ?? ''}`.trim() || '—'

    return (
        <div className="flex items-center justify-between gap-6 pb-6">
            <div className="flex items-center gap-5">
                <div className="flex h-14 w-14 shrink-0 items-center justify-center rounded-full bg-teal-50 text-[15px] font-semibold text-teal-700">
                    {getInitials(beneficiary.firstName, beneficiary.lastName)}
                </div>

                <div className="min-w-0">
                    <div className="flex items-center gap-2.5">
                        <h1 className="text-[19px] font-semibold leading-tight text-slate-950">
                            {fullName}
                        </h1>

                        <span
                            className={`inline-flex items-center rounded-full px-2 py-0.5 text-[11px] font-medium ${beneficiary.isActive
                                    ? 'bg-teal-50 text-teal-700'
                                    : 'bg-slate-100 text-slate-500'
                                }`}
                        >
                            {beneficiary.isActive ? 'Active' : 'Inactive'}
                        </span>
                    </div>

                    <p className="mt-1 text-sm text-slate-500">
                        {beneficiary.houseUnitDisplayName ??
                            beneficiary.houseUnitCode ??
                            '—'}
                    </p>
                </div>
            </div>
        </div>
    )
}