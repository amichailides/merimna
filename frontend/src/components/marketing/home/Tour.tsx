import { useRef } from 'react'

import { DashboardScreen, MedicationPanel, ResidentList, ResidentProfile, type PanelField } from './Screens'
import { ADMIN_EMAIL, DOSE_CHANGE, EFTYCHIA, NEW_MEDICATION, recentActivity } from './showcase'
import { delay, useMotionAllowed, useTrackProgress, useWide } from './useReveal'
import { Workspace } from './Workspace'

const steps = [
    {
        title: 'Find anyone, quickly',
        text: 'Search by name or AMKA across the homes you work in.',
    },
    {
        title: 'See the whole picture',
        text: 'Their home, medications, allergies and representatives, together on one profile.',
    },
    {
        title: 'Record a new medication',
        text: 'Dose, frequency, times and instructions, written once for the whole team to see.',
    },
    {
        title: 'Keep it current',
        text: 'When something changes, the record updates and managers see it in recent activity.',
    },
]

const QUERY = 'Savv'
const UPDATED_ACTIVITY = {
    title: 'Medication Updated',
    subtitle: `Medication · ${EFTYCHIA.name}`,
    time: '21 Sep 2026, 10:14',
}

function addFields(count: number): PanelField[] {
    const all: PanelField[] = [
        { label: 'Name', value: NEW_MEDICATION.name },
        { label: 'Dosage', value: NEW_MEDICATION.dosage },
        { label: 'Frequency', value: NEW_MEDICATION.frequency },
        { label: 'Start date', value: NEW_MEDICATION.start },
        { label: 'Administration times', value: NEW_MEDICATION.times, wide: true },
        { label: 'Instructions', value: NEW_MEDICATION.instructions, wide: true, tall: true },
    ]
    return all.map((field, index) => (index < count ? field : { ...field, value: '' }))
}

const metformin = EFTYCHIA.medications[1]

type Frame = {
    step: number
    view: 'list' | 'profile' | 'dashboard'
    query: string
    selected?: string
    medications: typeof EFTYCHIA.medications
    highlight?: string
    panel?: { kind: 'add' | 'edit'; filled: number; pressed: boolean }
}

// Every state of the walkthrough, as a pure function of scroll progress.
function frameAt(progress: number): Frame {
    const step = Math.min(Math.floor(progress * 4), 3)
    const local = Math.min(Math.max(progress * 4 - step, 0), 1)
    const base = EFTYCHIA.medications
    const added = [{ ...NEW_MEDICATION }, ...base]

    if (step === 0) {
        const typed = Math.min(QUERY.length, Math.floor(local / 0.14))
        return { step, view: 'list', query: QUERY.slice(0, typed), selected: local > 0.72 ? EFTYCHIA.name : undefined, medications: base }
    }
    if (step === 1) {
        return { step, view: 'profile', query: QUERY, medications: base }
    }
    if (step === 2) {
        if (local > 0.84) return { step, view: 'profile', query: QUERY, medications: added, highlight: NEW_MEDICATION.name }
        return {
            step,
            view: 'profile',
            query: QUERY,
            medications: base,
            panel: { kind: 'add', filled: Math.min(6, Math.floor((local + 0.04) / 0.11)), pressed: local > 0.74 },
        }
    }
    const edited = added.map((m) => (m.name === DOSE_CHANGE.medication ? { ...m, dosage: DOSE_CHANGE.to } : m))
    if (local < 0.5) {
        return {
            step,
            view: 'profile',
            query: QUERY,
            medications: added,
            panel: { kind: 'edit', filled: local > 0.12 ? 2 : 1, pressed: local > 0.36 },
        }
    }
    return { step, view: 'dashboard', query: QUERY, medications: edited }
}

function TourScreen({ frame, live }: { frame: Frame; live: boolean }) {
    const editFields: PanelField[] = [
        { label: 'Name', value: metformin.name },
        { label: 'Dosage', value: frame.panel && frame.panel.filled >= 2 ? DOSE_CHANGE.to : DOSE_CHANGE.from },
        { label: 'Frequency', value: metformin.frequency },
        { label: 'Administration times', value: metformin.times },
        { label: 'Instructions', value: metformin.instructions, wide: true, tall: true },
    ]

    return (
        <div className="mh-tour-screen">
            <Workspace
                role="admin"
                active={frame.view === 'dashboard' ? 'Dashboard' : 'Beneficiaries'}
                email={ADMIN_EMAIL}
                className="mh-ws-tour"
            >
                <div key={frame.view} className={live ? 'mh-screen' : ''}>
                    {frame.view === 'list' && <ResidentList query={frame.query} focused selected={frame.selected} />}
                    {frame.view === 'profile' && <ResidentProfile medications={frame.medications} highlight={frame.highlight} />}
                    {frame.view === 'dashboard' && <DashboardScreen activity={[UPDATED_ACTIVITY, ...recentActivity.slice(0, 2)]} highlightFirst />}
                </div>
            </Workspace>

            {frame.panel && (
                <div key={frame.panel.kind} className="mh-tour-panel">
                    {frame.panel.kind === 'add' ? (
                        <MedicationPanel
                            title="Add medication"
                            subtitle="Record a medication for this beneficiary."
                            fields={addFields(frame.panel.filled)}
                            action="Add medication"
                            pressed={frame.panel.pressed}
                            activeField={frame.panel.pressed ? -1 : frame.panel.filled}
                        />
                    ) : (
                        <MedicationPanel
                            title="Edit medication"
                            fields={editFields}
                            action="Save changes"
                            pressed={frame.panel.pressed}
                            activeField={frame.panel.pressed ? -1 : 1}
                        />
                    )}
                </div>
            )}
        </div>
    )
}

export function Tour() {
    const motion = useMotionAllowed()
    const wide = useWide(1000)
    const pinned = motion && wide
    const trackRef = useRef<HTMLDivElement>(null)
    const progress = useTrackProgress(trackRef, pinned)
    const frame = frameAt(pinned ? progress : 0)

    return (
        <section id="residents" className="mh-tour" aria-labelledby="mh-tour-title">
            <div className="mh-wrap mh-grid mh-chapter-head">
                <h2 id="mh-tour-title" className="mh-title" data-reveal>
                    What every shift needs to know
                </h2>
                <p className="mh-statement" data-reveal style={delay(90)}>
                    <span>Each resident has one record: their home, their medications and when to give them, their allergies, and the people who represent them.</span>{' '}
                    Everyone who looks after them works from the same information.
                </p>
            </div>

            {pinned ? (
                <div ref={trackRef} className="mh-tour-track">
                    <div className="mh-tour-sticky">
                        <div className="mh-wrap mh-grid mh-tour-grid">
                            <ol className="mh-tour-steps">
                                {steps.map((step, index) => (
                                    <li key={step.title} data-current={index === frame.step} data-done={index < frame.step}>
                                        <p className="mh-tour-step-title">{step.title}</p>
                                        <p className="mh-tour-step-text">{step.text}</p>
                                    </li>
                                ))}
                            </ol>

                            <figure className="mh-stage mh-tour-stage" aria-label="Walkthrough of the Merimna beneficiary workflow">
                                <TourScreen frame={frame} live />
                            </figure>
                        </div>
                    </div>
                </div>
            ) : (
                <ol className="mh-wrap mh-tour-static">
                    {steps.map((step, index) => (
                        <li key={step.title} className="mh-tour-static-step" data-reveal>
                            <div className="mh-tour-static-text">
                                <p className="mh-tour-step-title">{step.title}</p>
                                <p className="mh-tour-step-text">{step.text}</p>
                            </div>
                            <figure className="mh-stage mh-tour-stage">
                                <TourScreen frame={frameAt([0.2, 0.37, 0.68, 0.99][index])} live={false} />
                            </figure>
                        </li>
                    ))}
                </ol>
            )}
        </section>
    )
}
