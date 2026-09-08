import type { BeneficiaryDetailsDTO } from '@/api/types'
import { BeneficiaryDetailsEditForm } from '@/components/beneficiaries/edit/BeneficiaryDetailsEditForm'
import {
    FloatingPanelContent,
    FloatingPanelRoot,
    FloatingPanelTrigger,
} from '@/components/ui/floating-panel'
import { Pencil } from 'lucide-react'

type BeneficiaryProfileHeaderProps = {
    beneficiary: BeneficiaryDetailsDTO
    onBeneficiaryUpdated?: () => void | Promise<void>
}

function getInitials(firstName?: string, lastName?: string) {
    return `${firstName?.[0] ?? ''}${lastName?.[0] ?? ''}`.toUpperCase() || '?'
}

export function BeneficiaryProfileHeader({
    beneficiary,
    onBeneficiaryUpdated,
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

            <FloatingPanelRoot>
                <FloatingPanelTrigger
                    title="Edit beneficiary"
                    className="inline-flex h-auto items-center rounded-lg border border-slate-200 bg-white px-3 py-1.5 text-[13px] font-medium text-slate-700 shadow-none transition-colors hover:border-slate-300 hover:bg-slate-50 hover:text-slate-950"
                >
                    <span className="inline-flex items-center gap-1.5">
                        <Pencil
                            size={13}
                            strokeWidth={2.25}
                            className="text-slate-500"
                        />
                        Edit
                    </span>
                </FloatingPanelTrigger>

                <FloatingPanelContent
                    align="center"
                    className="w-[440px] rounded-xl border border-slate-100 bg-white shadow-sm"
                >
                    <BeneficiaryDetailsEditForm
                        beneficiary={beneficiary}
                        onBeneficiaryUpdated={onBeneficiaryUpdated}
                    />
                </FloatingPanelContent>
            </FloatingPanelRoot>
        </div>
    )
}
