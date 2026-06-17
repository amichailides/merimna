import type { ReactNode } from 'react'

type InfoItemProps = {
    label: string
    value?: ReactNode
}

export function InfoItem({ label, value }: InfoItemProps) {
    return (
        <div className="space-y-0.5">
            <p className="text-[11px] font-medium text-slate-400">
                {label}
            </p>

            <div className="text-[13px] font-medium text-slate-900">
                {value || '—'}
            </div>
        </div>
    )
}