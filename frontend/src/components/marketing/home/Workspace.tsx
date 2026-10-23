import type { ReactNode } from 'react'
import {
    ArrowLeftRight,
    Building2,
    ClipboardList,
    HeartPulse,
    LayoutDashboard,
    LogOut,
    Users,
    type LucideIcon,
} from 'lucide-react'

import { MerimnaLogo } from '@/components/brand/MerimnaLogo'

type NavGroup = { label: string; items: { label: string; icon: LucideIcon }[] }

// The two real navigation trees from src/navigation (admin and staff).
const NAV: Record<'admin' | 'staff', NavGroup[]> = {
    admin: [
        { label: 'Overview', items: [{ label: 'Dashboard', icon: LayoutDashboard }] },
        {
            label: 'People',
            items: [
                { label: 'Employees', icon: Users },
                { label: 'Beneficiaries', icon: HeartPulse },
            ],
        },
        { label: 'Facilities', items: [{ label: 'House Units', icon: Building2 }] },
        {
            label: 'Staffing',
            items: [
                { label: 'Assignments', icon: ClipboardList },
                { label: 'Placements', icon: ArrowLeftRight },
            ],
        },
    ],
    staff: [
        { label: 'Overview', items: [{ label: 'Dashboard', icon: LayoutDashboard }] },
        {
            label: 'Care',
            items: [
                { label: 'My Unit', icon: Building2 },
                { label: 'Residents', icon: HeartPulse },
            ],
        },
    ],
}

type WorkspaceProps = {
    role: 'admin' | 'staff'
    active: string
    email: string
    children: ReactNode
    className?: string
    // Without the sidebar, as the app renders below its md breakpoint.
    compact?: boolean
}

// The Merimna app shell (AppLayout): top bar, sidebar, white main panel.
export function Workspace({ role, active, email, children, className = '', compact = false }: WorkspaceProps) {
    return (
        <div data-ui className={`mh-ws flex flex-col bg-[#f7f7f5] text-slate-900 ${className}`}>
            <div className="flex h-14 shrink-0 items-center justify-between px-4">
                <div className="flex items-center gap-2.5">
                    <MerimnaLogo className="h-7 w-7 text-teal-600" />
                    <div className="flex flex-col leading-none">
                        <span className="mh-ws-brand text-[14px] font-semibold text-slate-950">Merimna</span>
                        <span className="mt-0.5 text-[10px] text-slate-600">Care Management</span>
                    </div>
                </div>

                <div className="flex items-center gap-2 px-2 py-1 text-[13px] text-slate-600">
                    <div className="flex h-6 w-6 items-center justify-center rounded-full bg-teal-100 text-[10px] font-semibold text-teal-700">
                        {email
                            .split('@')[0]
                            .split(/[._-]/)
                            .map((part) => part.charAt(0))
                            .join('')
                            .slice(0, 2)
                            .toUpperCase()}
                    </div>
                    <span className="mh-ws-email font-medium">{email}</span>
                    <LogOut size={13} strokeWidth={1.75} className="ml-1 text-slate-400" />
                </div>
            </div>

            <div className="flex min-h-0 flex-1">
                <aside className={`mh-ws-side w-52 shrink-0 px-3 pt-4 ${compact ? 'hidden' : ''}`}>
                    {NAV[role].map((group, index) => (
                        <div key={group.label} className={index > 0 ? 'mt-5' : ''}>
                            <p className="mb-1 px-2 text-[11px] font-medium text-slate-600">{group.label}</p>
                            <div className="flex flex-col items-start">
                                {group.items.map((item) => {
                                    const current = item.label === active
                                    return (
                                        <span
                                            key={item.label}
                                            className={`inline-flex items-center gap-2 rounded-md px-2 py-[5px] text-[13px] transition-colors duration-300 ${
                                                current
                                                    ? 'bg-teal-50 font-medium text-teal-700'
                                                    : 'text-slate-600'
                                            }`}
                                        >
                                            <item.icon size={14} strokeWidth={1.75} className="shrink-0 opacity-70" />
                                            {item.label}
                                        </span>
                                    )
                                })}
                            </div>
                        </div>
                    ))}
                </aside>

                <div className="min-w-0 flex-1 px-3 pb-3 pt-1">
                    <div className="mh-ws-main h-full overflow-hidden rounded-xl border border-slate-200/70 bg-white">
                        {children}
                    </div>
                </div>
            </div>
        </div>
    )
}

export function StatusPill({ tone, children }: { tone: 'active' | 'muted' | 'teal'; children: ReactNode }) {
    const styles = {
        active: 'bg-emerald-50 text-emerald-700',
        muted: 'bg-slate-100 text-slate-600',
        teal: 'bg-teal-50 text-teal-700',
    }[tone]

    return (
        <span className={`inline-flex h-fit items-center gap-1 whitespace-nowrap rounded-full px-2 py-0.5 text-[11px] font-medium ${styles}`}>
            {tone === 'active' && <span className="h-1.5 w-1.5 rounded-full bg-emerald-500" />}
            {children}
        </span>
    )
}

export function Avatar({ text, muted = false, size = 34 }: { text: string; muted?: boolean; size?: number }) {
    return (
        <div
            className={`flex shrink-0 items-center justify-center rounded-full text-[11px] font-medium ${
                muted ? 'bg-slate-100 text-slate-500' : 'bg-teal-50 text-teal-800'
            }`}
            style={{ width: size, height: size }}
        >
            {text}
        </div>
    )
}
