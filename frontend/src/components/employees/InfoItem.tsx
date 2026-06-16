import type { ReactNode } from 'react'

type InfoItemProps = {
    label: string
    value?: ReactNode
}

export function InfoItem({ label, value }: InfoItemProps) {
    return (
        <div className="space-y-1">
            <p className="text-xs font-medium text-slate-500">
                {label}
            </p>

            <div className="text-sm font-medium text-slate-700">
                {value || '—'}
            </div>
        </div>
    )
}