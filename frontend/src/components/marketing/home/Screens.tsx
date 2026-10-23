import { useEffect, useState, type ReactNode } from 'react'
import {
    ArrowLeft,
    BriefcaseBusiness,
    Check,
    ChevronRight,
    Mail,
    MapPin,
    MoreHorizontal,
    Pencil,
    Plus,
    Search,
} from 'lucide-react'

import {
    EFTYCHIA,
    NEW_HIRE,
    houseCodes,
    houses,
    initials,
    residents,
    summary,
    type Activity,
    type Medication,
    type Resident,
} from './showcase'
import { Avatar, StatusPill } from './Workspace'

// Product screens rebuilt from the app's own components (Tailwind classes copied
// from src/components and src/pages) and driven by props, so the page can play
// real workflows through them.

export function ScreenHeader({ title, subtitle, action }: { title: string; subtitle: string; action?: ReactNode }) {
    return (
        <div className="mb-6 flex items-start justify-between gap-4">
            <div>
                <p className="text-[22px] font-semibold tracking-tight text-slate-950">{title}</p>
                <p className="mt-1 text-sm text-slate-500">{subtitle}</p>
            </div>
            {action}
        </div>
    )
}

export function TabsBar({ tabs, current }: { tabs: string[]; current: string }) {
    return (
        <div className="mt-5 flex gap-6 border-b border-slate-200 text-[13px]">
            {tabs.map((tab) => (
                <span
                    key={tab}
                    className={`-mb-px pb-2.5 transition-colors duration-300 ${
                        tab === current ? 'border-b-2 border-teal-600 font-medium text-slate-900' : 'text-slate-500'
                    }`}
                >
                    {tab}
                </span>
            ))}
        </div>
    )
}

function useCountUp(target: number, run: boolean) {
    const [value, setValue] = useState(run ? 0 : target)

    useEffect(() => {
        if (!run) return
        let frame = 0
        const started = performance.now()
        const tick = (now: number) => {
            const t = Math.min((now - started) / 1200, 1)
            setValue(Math.round(target * (1 - Math.pow(1 - t, 3))))
            if (t < 1) frame = requestAnimationFrame(tick)
        }
        const delay = window.setTimeout(() => (frame = requestAnimationFrame(tick)), 500)
        return () => {
            window.clearTimeout(delay)
            cancelAnimationFrame(frame)
        }
    }, [target, run])

    return value
}

function SummaryItem({ label, value, first, run }: { label: string; value: number; first: boolean; run: boolean }) {
    const shown = useCountUp(value, run)
    return (
        <div className={`py-5 ${first ? 'pr-5' : 'border-l border-slate-100 px-5'}`}>
            <div className="text-[26px] font-semibold leading-none tracking-tight tabular-nums text-slate-950">{shown}</div>
            <div className="mt-2 text-[13px] text-slate-500">{label}</div>
        </div>
    )
}

export function ActivityList({ items, highlightFirst = false }: { items: Activity[]; highlightFirst?: boolean }) {
    return (
        <ol>
            {items.map((e, index) => (
                <li
                    key={e.title + e.time}
                    className={`relative pb-5 pl-7 last:pb-0 ${highlightFirst && index === 0 ? 'mh-ui-enter' : ''}`}
                >
                    {index < items.length - 1 && <span className="absolute left-[3.5px] top-[14px] h-full w-px bg-slate-100" />}
                    <span
                        className={`absolute left-0 top-[5px] size-2 rounded-full border ${
                            index === 0 ? 'border-teal-500 bg-teal-500' : 'border-slate-300 bg-white'
                        }`}
                    />
                    <p className="text-[14px] font-semibold leading-5 text-slate-950/80">{e.title}</p>
                    <p className="text-[13px] leading-5 text-slate-500">{e.subtitle}</p>
                    <p className="text-[12px] leading-5 text-slate-500">{e.time}</p>
                </li>
            ))}
        </ol>
    )
}

export function DashboardScreen({
    activity,
    countUp = false,
    highlightFirst = false,
}: {
    activity: Activity[]
    countUp?: boolean
    highlightFirst?: boolean
}) {
    return (
        <div className="px-8 py-7">
            <ScreenHeader title="Dashboard" subtitle="Organization overview" />

            <div className="grid grid-cols-5 border-y border-slate-200">
                {summary.map((item, index) => (
                    <SummaryItem key={item.label} label={item.label} value={item.value} first={index === 0} run={countUp} />
                ))}
            </div>

            <div className="mh-dash-split mt-8 grid grid-cols-[minmax(0,1.55fr)_minmax(0,1fr)] gap-10">
                <section>
                    <p className="text-lg font-semibold text-slate-950">House units</p>
                    <p className="mt-1 text-sm text-slate-500">Current occupancy and staffing across the organization.</p>
                    <div className="mt-4 overflow-hidden border-y border-slate-200">
                        <div className="grid grid-cols-[1.5fr_1.2fr_0.8fr_0.9fr] bg-teal-50/40 px-4 py-2.5 text-xs font-medium uppercase tracking-wide text-slate-500">
                            <div>House unit</div>
                            <div>Occupancy</div>
                            <div>Assigned</div>
                            <div>Temporary</div>
                        </div>
                        {houseCodes.map((code) => {
                            const unit = houses[code]
                            return (
                                <div key={code} className="grid grid-cols-[1.5fr_1.2fr_0.8fr_0.9fr] items-center border-t border-slate-100 px-4 py-4 text-sm first:border-t-0">
                                    <div className="font-semibold text-slate-900">{unit.name}</div>
                                    <div className="pr-8">
                                        <div className="flex items-center justify-between">
                                            <span className="font-medium text-slate-800">{unit.residents}</span>
                                            <span className="text-xs text-slate-500">of {unit.capacity}</span>
                                        </div>
                                        <div className="mt-2 h-1 overflow-hidden rounded-full bg-slate-100">
                                            <div
                                                className="mh-bar h-full rounded-full bg-teal-500"
                                                style={{ width: `${(unit.residents / unit.capacity) * 100}%` }}
                                            />
                                        </div>
                                    </div>
                                    <div className="font-medium text-slate-800">{unit.assigned}</div>
                                    <div className="font-medium text-slate-800">{unit.placed}</div>
                                </div>
                            )
                        })}
                    </div>
                </section>

                <section className="mh-dash-activity">
                    <p className="text-base font-semibold text-slate-900">Recent activity</p>
                    <p className="mt-1 mb-5 text-sm text-slate-500">Latest operational changes across the organization.</p>
                    <ActivityList items={activity} highlightFirst={highlightFirst} />
                </section>
            </div>
        </div>
    )
}

// BeneficiariesPage with its search field and list.
export function ResidentList({ query, focused, selected }: { query: string; focused: boolean; selected?: string }) {
    const normalized = query.trim().toLowerCase()
    const rows = residents.filter((r) => r.active && (normalized.length === 0 || r.name.toLowerCase().includes(normalized)))

    return (
        <div className="px-8 py-6">
            <ScreenHeader
                title="Beneficiaries"
                subtitle="Manage beneficiaries and supported living details."
                action={
                    <span className="inline-flex h-8 items-center gap-1.5 rounded-md bg-teal-700 px-3 text-[13px] font-medium text-white">
                        <Plus className="h-4 w-4" /> Add beneficiary
                    </span>
                }
            />

            <div className="flex items-center gap-2">
                <div
                    className={`relative flex h-8 flex-1 items-center rounded-md border bg-slate-50 pl-8 text-[13px] transition-colors duration-300 ${
                        focused ? 'border-teal-400 ring-1 ring-teal-500/30' : 'border-slate-100'
                    }`}
                >
                    <Search className="pointer-events-none absolute left-2.5 top-1/2 h-3.5 w-3.5 -translate-y-1/2 text-slate-400" />
                    {query ? <span className="text-slate-900">{query}</span> : <span className="text-slate-500">Search by name or AMKA...</span>}
                    {focused && <span className="mh-caret" aria-hidden="true" />}
                </div>
                <div className="flex h-8 items-center rounded-lg bg-slate-50 p-0.5">
                    <span className="flex h-7 w-16 items-center justify-center rounded-md bg-white text-[12px] font-medium text-slate-900 shadow-sm">Active</span>
                    <span className="flex h-7 w-16 items-center justify-center text-[12px] text-slate-500">All</span>
                </div>
            </div>

            <div className="mt-2">
                {rows.slice(0, 7).map((r) => (
                    <div
                        key={r.name}
                        className={`flex items-center gap-3.5 rounded-md border-b border-slate-100 py-3 transition-all duration-300 last:border-b-0 ${
                            selected === r.name ? 'translate-x-0.5 bg-teal-50/60' : ''
                        }`}
                    >
                        <Avatar text={initials(r.name)} />
                        <div className="min-w-0 flex-1">
                            <p className={`truncate text-[13px] font-medium ${selected === r.name ? 'text-teal-700' : 'text-slate-900'}`}>{r.name}</p>
                            <p className="mt-0.5 truncate text-[11px] text-slate-500">{houses[r.house].name}</p>
                        </div>
                        <StatusPill tone="active">Active</StatusPill>
                        <ChevronRight className={`h-3.5 w-3.5 shrink-0 ${selected === r.name ? 'text-slate-500' : 'text-slate-300'}`} />
                    </div>
                ))}
            </div>

            <p className="border-t border-slate-100 pt-3 text-[13px] text-slate-500">
                {rows.length === 0 ? 'No beneficiaries found.' : `Showing 1-${Math.min(rows.length, 7)} of ${rows.length}`}
            </p>
        </div>
    )
}

export function MedicationRow({ medication, highlight = false }: { medication: Medication; highlight?: boolean }) {
    return (
        <article className={`space-y-3 py-4 first:pt-0 ${highlight ? 'mh-ui-enter' : ''}`}>
            <div className="flex items-center gap-3">
                <p className="text-[13px] font-medium text-slate-900">{medication.name}</p>
                <Pencil className="size-3.5 text-slate-400" />
            </div>
            <p className="-mt-2 text-[12px] text-slate-500">
                {medication.dosage} · {medication.frequency}
            </p>
            <dl className="grid grid-cols-2 gap-4">
                <div className="space-y-1">
                    <dt className="text-[12px] text-slate-500">Administration times</dt>
                    <dd className="text-[13px] leading-5 text-slate-900">{medication.times}</dd>
                </div>
                <div className="space-y-1">
                    <dt className="text-[12px] text-slate-500">Instructions</dt>
                    <dd className="text-[13px] leading-5 text-slate-900">{medication.instructions}</dd>
                </div>
            </dl>
        </article>
    )
}

// BeneficiaryDetailsPage on its Medications tab.
export function ResidentProfile({
    resident = EFTYCHIA,
    medications,
    highlight,
}: {
    resident?: Resident
    medications: Medication[]
    highlight?: string
}) {
    return (
        <div className="px-8 py-6">
            <p className="inline-flex items-center gap-1.5 text-[12px] font-medium text-teal-700">
                <ArrowLeft className="h-3.5 w-3.5" /> Back to beneficiaries
            </p>
            <div className="mt-5 flex items-center gap-3.5">
                <Avatar text={initials(resident.name)} size={44} />
                <div>
                    <div className="flex items-center gap-2">
                        <p className="text-[18px] font-medium text-slate-950">{resident.name}</p>
                        <StatusPill tone="active">Active</StatusPill>
                    </div>
                    <p className="text-[13px] text-slate-500">{houses[resident.house].name}</p>
                </div>
            </div>
            <TabsBar tabs={['Overview', 'Medications', 'Allergies', 'Legal representatives']} current="Medications" />

            <section className="mt-6 max-w-2xl">
                <div className="flex items-start justify-between">
                    <div>
                        <p className="text-[14px] font-medium text-slate-900">Medications</p>
                        <p className="mt-1 text-[12px] text-slate-500">Current medication details for this beneficiary.</p>
                    </div>
                    <span className="inline-flex h-8 items-center gap-1.5 rounded-md border border-slate-200 px-3 text-[12px] font-medium text-slate-700">
                        <Plus className="h-3.5 w-3.5" /> Add medication
                    </span>
                </div>
                <div className="mt-5 divide-y divide-slate-100">
                    {medications.map((m) => (
                        <MedicationRow key={m.name} medication={m} highlight={highlight === m.name} />
                    ))}
                </div>
            </section>
        </div>
    )
}

export type PanelField = { label: string; value: string; wide?: boolean; tall?: boolean }

// The floating panel used by MedicationCreateForm and MedicationUpdateForm.
export function MedicationPanel({
    title,
    subtitle,
    fields,
    action,
    pressed,
    activeField,
}: {
    title: string
    subtitle?: string
    fields: PanelField[]
    action: string
    pressed: boolean
    activeField: number
}) {
    return (
        <div data-ui className="mh-float w-[380px] overflow-hidden rounded-xl border border-zinc-950/10 bg-white text-slate-900">
            <div className="border-b border-slate-100 px-5 py-4">
                <p className="text-sm font-semibold">{title}</p>
                {subtitle && <p className="mt-0.5 text-[12px] text-slate-500">{subtitle}</p>}
            </div>
            <div className="grid grid-cols-2 gap-3 px-5 py-4">
                {fields.map((field, index) => (
                    <label key={field.label} className={`block space-y-1.5 ${field.wide ? 'col-span-2' : ''}`}>
                        <span className="text-[11px] text-slate-500">{field.label}</span>
                        <span
                            className={`flex rounded-lg border px-3 py-2 text-[13px] text-slate-800 transition-colors duration-200 ${
                                field.tall ? 'min-h-[52px] items-start' : 'h-[36px] items-center'
                            } ${index === activeField ? 'border-teal-400 ring-1 ring-teal-500/30' : 'border-slate-200'}`}
                        >
                            {field.value}
                            {index === activeField && <span className="mh-caret" aria-hidden="true" />}
                        </span>
                    </label>
                ))}
            </div>
            <div className="flex justify-end gap-2 border-t border-slate-100 px-5 py-3">
                <span className="inline-flex h-8 items-center rounded-md px-3 text-[12px] font-medium text-slate-600">Cancel</span>
                <span
                    className={`inline-flex h-8 items-center gap-1.5 rounded-md bg-teal-700 px-3 text-[12px] font-medium text-white transition-transform duration-150 ${
                        pressed ? 'scale-95 bg-teal-800' : ''
                    }`}
                >
                    {pressed && <Check className="h-3.5 w-3.5" />}
                    {action}
                </span>
            </div>
        </div>
    )
}

function ContextItem({ icon, label, badge, title, description }: { icon: ReactNode; label: string; badge?: string; title: string; description: string }) {
    return (
        <div className="flex gap-3">
            <div className="mt-[1px] flex h-3 w-4 shrink-0 items-center justify-center text-slate-400">{icon}</div>
            <div className="min-w-0 space-y-1.5">
                <div className="flex flex-wrap items-center gap-2">
                    <p className="text-[11px] font-semibold uppercase tracking-[0.04em] text-slate-500">{label}</p>
                    {badge && <span className="rounded-full bg-teal-50 px-2 py-0.5 text-[11px] font-medium leading-none text-teal-700">{badge}</span>}
                </div>
                <p className="text-[14px] font-semibold leading-5 text-slate-950/80">{title}</p>
                <p className="text-[13px] leading-5 text-slate-500">{description}</p>
            </div>
        </div>
    )
}

export function AccessStatus({ status }: { status: 'pending' | 'enabled' }) {
    return (
        <section className="space-y-3">
            <p className="text-[13px] font-medium text-slate-700">System access</p>
            <div className="flex items-center gap-2 text-[13px] font-medium text-slate-900">
                <span className={`h-1.5 w-1.5 shrink-0 rounded-full ${status === 'pending' ? 'bg-amber-500' : 'bg-emerald-500'}`} />
                {status === 'pending' ? 'Invitation pending' : 'Login enabled'}
            </div>
            <div className="flex items-center gap-1.5 text-[12px] text-slate-500">
                <Mail size={13} strokeWidth={2} />
                <span>{NEW_HIRE.email}</span>
            </div>
            {status === 'pending' && <p className="text-[12px] text-slate-500">Expires {NEW_HIRE.invitationExpires}</p>}
        </section>
    )
}

// EmployeeDetailsPage, Overview tab.
export function EmployeeOverview({
    name,
    position,
    current,
    official,
    access,
}: {
    name: string
    position: string
    current?: { house: string; since: string }
    official: { house: string; since: string }
    access?: 'pending' | 'enabled'
}) {
    return (
        <div className="px-8 py-6">
            <p className="inline-flex items-center gap-1.5 text-[12px] font-medium text-teal-700">
                <ArrowLeft className="h-3.5 w-3.5" /> Back to employees
            </p>
            <div className="mt-5 flex items-center justify-between">
                <div className="flex items-center gap-3.5">
                    <Avatar text={initials(name)} size={44} />
                    <div>
                        <div className="flex items-center gap-2">
                            <p className="text-[18px] font-medium text-slate-950">{name}</p>
                            <StatusPill tone="teal">Active</StatusPill>
                        </div>
                        <p className="text-[13px] text-slate-500">{position}</p>
                    </div>
                </div>
                <div className="flex items-center gap-2">
                    <span className="inline-flex h-8 items-center gap-1.5 rounded-md border border-slate-200 px-3 text-[12px] font-medium text-slate-700">
                        <Pencil className="h-3 w-3" /> Edit
                    </span>
                    <span className="inline-flex h-8 w-8 items-center justify-center rounded-md border border-slate-200 text-slate-500">
                        <MoreHorizontal className="h-4 w-4" />
                    </span>
                </div>
            </div>
            <TabsBar tabs={['Overview', 'Assignments', 'Placements', 'Activity']} current="Overview" />

            <div className="mt-6 grid grid-cols-[minmax(0,1fr)_220px] gap-10">
                <section className="space-y-5">
                    <p className="text-[13px] font-medium text-slate-700">Work context</p>
                    {current ? (
                        <>
                            <ContextItem
                                icon={<MapPin size={15} strokeWidth={1.75} />}
                                label="Current work location"
                                badge="Temporary placement"
                                title={current.house}
                                description={`Temporary coverage · ${current.since} → Open-ended`}
                            />
                            <ContextItem
                                icon={<BriefcaseBusiness size={15} strokeWidth={1.75} />}
                                label="Official home unit"
                                title={official.house}
                                description={`Active assignment · ${official.since} → Open-ended`}
                            />
                        </>
                    ) : (
                        <>
                            <ContextItem
                                icon={<MapPin size={15} strokeWidth={1.75} />}
                                label="Current work location"
                                badge="Official assignment"
                                title={official.house}
                                description={`Active assignment · ${official.since} → Open-ended`}
                            />
                            <ContextItem
                                icon={<BriefcaseBusiness size={15} strokeWidth={1.75} />}
                                label="Official home unit"
                                title={official.house}
                                description="Same unit as current work location."
                            />
                        </>
                    )}
                </section>
                <aside className="space-y-6 border-l border-slate-100 pl-6">{access ? <AccessStatus status={access} /> : null}</aside>
            </div>
        </div>
    )
}

type OnboardingSection = { title: string; fields: { label: string; value: string }[] }

const ONBOARDING: OnboardingSection[] = [
    {
        title: 'Personal details',
        fields: [
            { label: 'First name', value: NEW_HIRE.firstName },
            { label: 'Last name', value: NEW_HIRE.lastName },
            { label: 'Date of birth', value: NEW_HIRE.dateOfBirth },
            { label: 'Mobile number', value: NEW_HIRE.mobile },
        ],
    },
    {
        title: 'Contact & address',
        fields: [
            { label: 'Street', value: NEW_HIRE.street },
            { label: 'Street number', value: NEW_HIRE.streetNumber },
            { label: 'City', value: NEW_HIRE.city },
            { label: 'Postal code', value: NEW_HIRE.postalCode },
        ],
    },
    {
        title: 'Employment & initial assignment',
        fields: [
            { label: 'Position', value: NEW_HIRE.position },
            { label: 'Hire date', value: NEW_HIRE.hireDate },
            { label: 'House unit', value: houses[NEW_HIRE.house].name },
            { label: 'Assignment start date', value: NEW_HIRE.assignmentStart },
        ],
    },
]

// EmployeeOnboardingPage. `step` is the section being filled (0-3), `filled` how far.
export function OnboardingForm({ step, filled }: { step: number; filled: number }) {
    return (
        <div className="h-full overflow-hidden">
            <div className="mh-onboarding-track px-8 py-6" style={{ transform: `translateY(-${Math.min(step, 3) * 150}px)` }}>
                <p className="inline-flex items-center gap-1.5 text-[12px] font-medium text-teal-700">
                    <ArrowLeft className="h-3.5 w-3.5" /> Back to employees
                </p>
                <div className="mt-4 mb-6">
                    <p className="text-[18px] font-medium text-slate-950">Add employee</p>
                    <p className="mt-0.5 text-[13px] text-slate-500">Create an employee, initial assignment and system invitation.</p>
                </div>

                    {ONBOARDING.map((section, index) => {
                        const done = index < step
                        const current = index === step
                        return (
                            <section
                                key={section.title}
                                className={`mb-6 space-y-4 transition-opacity duration-500 ${done || current ? 'opacity-100' : 'opacity-50'}`}
                            >
                                <p className="flex items-center gap-2 text-[14px] font-medium text-slate-900">
                                    {section.title}
                                    {done && <Check className="h-3.5 w-3.5 text-teal-600" />}
                                </p>
                                <div className="grid grid-cols-2 gap-x-6 gap-y-3">
                                    {section.fields.map((field, f) => {
                                        const shown = done || (current && f < filled)
                                        const active = current && f === filled
                                        return (
                                            <label key={field.label} className="block space-y-1">
                                                <span className="text-[11px] text-slate-500">{field.label}</span>
                                                <span
                                                    className={`flex h-9 items-center border-b text-[13px] transition-colors duration-300 ${
                                                        active ? 'border-b-teal-600' : 'border-slate-200'
                                                    }`}
                                                >
                                                    <span className={`transition-opacity duration-300 ${shown ? 'opacity-100' : 'opacity-0'}`}>{field.value}</span>
                                                    {active && <span className="mh-caret" aria-hidden="true" />}
                                                </span>
                                            </label>
                                        )
                                    })}
                                </div>
                            </section>
                        )
                    })}

                    <section className={`space-y-3 transition-opacity duration-500 ${step >= 3 ? 'opacity-100' : 'opacity-50'}`}>
                        <p className="text-[14px] font-medium text-slate-900">System access</p>
                        <div className="flex items-start gap-3 text-[13px]">
                            <span
                                className={`mt-0.5 flex h-4 w-4 shrink-0 items-center justify-center rounded border transition-colors duration-300 ${
                                    step >= 3 && filled > 0 ? 'border-teal-600 bg-teal-600 text-white' : 'border-slate-300 bg-white'
                                }`}
                            >
                                {step >= 3 && filled > 0 && <Check className="h-3 w-3" strokeWidth={3} />}
                            </span>
                            <span>
                                <span className="block font-medium text-slate-900">Send account invitation</span>
                                <span className="block text-[12px] text-slate-500">The employee will receive an email to choose a username and password.</span>
                            </span>
                        </div>
                        <label className="block max-w-xs space-y-1 pt-1">
                            <span className="text-[11px] text-slate-500">Account email</span>
                            <span className={`flex h-9 items-center border-b text-[13px] ${step >= 3 && filled === 1 ? 'border-b-teal-600' : 'border-slate-200'}`}>
                                <span className={`transition-opacity duration-300 ${step >= 3 && filled > 1 ? 'opacity-100' : 'opacity-0'}`}>{NEW_HIRE.email}</span>
                                {step >= 3 && filled === 1 && <span className="mh-caret" aria-hidden="true" />}
                            </span>
                        </label>
                        <div className="flex justify-end pt-3">
                            <span
                                className={`inline-flex h-9 items-center rounded-md bg-teal-700 px-4 text-[13px] font-medium text-white transition-transform duration-150 ${
                                    step >= 3 && filled > 2 ? 'scale-95 bg-teal-800' : ''
                                }`}
                            >
                                Add employee
                            </span>
                        </div>
                    </section>
            </div>
        </div>
    )
}
