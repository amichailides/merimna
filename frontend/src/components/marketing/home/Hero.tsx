import { useEffect, useState } from 'react'

import { MerimnaLogo } from '@/components/brand/MerimnaLogo'

import { links } from './links'
import { DashboardScreen, MedicationPanel, type PanelField } from './Screens'
import { ADMIN_EMAIL, DOSE_CHANGE, EFTYCHIA, recentActivity } from './showcase'
import { delay, useMotionAllowed } from './useReveal'
import { Workspace } from './Workspace'

export function SiteNav() {
    const [scrolled, setScrolled] = useState(false)

    useEffect(() => {
        const onScroll = () => setScrolled(window.scrollY > 24)
        onScroll()
        window.addEventListener('scroll', onScroll, { passive: true })
        return () => window.removeEventListener('scroll', onScroll)
    }, [])

    return (
        <header className="mh-nav" data-scrolled={scrolled}>
            <div className="mh-wrap mh-nav-inner">
                <a href="#top" className="mh-wordmark" aria-label="Merimna, back to top">
                    <MerimnaLogo className="mh-wordmark-mark" />
                    <span>merimna</span>
                </a>

                <nav aria-label="Sections" className="mh-nav-links">
                    <a className="mh-link" href="#residents">Residents</a>
                    <a className="mh-link" href="#staff">Staff</a>
                    <a className="mh-link" href="#access">Access</a>
                    <a className="mh-link" href="#story">Story</a>
                </nav>

                <div className="mh-nav-actions">
                    <a className="mh-link mh-nav-signin" href={links.demo}>Sign in</a>
                    <a className="mh-button mh-button-small" href={links.demo}>Explore the demo</a>
                </div>
            </div>
        </header>
    )
}

// The hero plays one short, real moment: a dose change is saved and arrives in recent activity.
const UPDATED_ACTIVITY = { title: 'Medication Updated', subtitle: `Medication · ${EFTYCHIA.name}`, time: '21 Sep 2026, 10:14' }
const metformin = EFTYCHIA.medications.find((m) => m.name === DOSE_CHANGE.medication)!
const editFields: PanelField[] = [
    { label: 'Name', value: metformin.name },
    { label: 'Dosage', value: DOSE_CHANGE.to },
    { label: 'Frequency', value: metformin.frequency },
    { label: 'Administration times', value: metformin.times },
    { label: 'Instructions', value: metformin.instructions, wide: true, tall: true },
]

// 0 panel open, 1 save pressed, 2 panel closing, 3 change arrived in recent activity.
function useLiveStep() {
    const motion = useMotionAllowed()
    const [step, setStep] = useState(0)

    useEffect(() => {
        if (!motion) return
        const timers = [1700, 2300, 2700].map((t, i) => window.setTimeout(() => setStep(i + 1), t))
        return () => timers.forEach(clearTimeout)
    }, [motion])

    // With reduced motion the visitor sees the finished moment straight away.
    return { step: motion ? step : 3, motion }
}

export function Hero() {
    const { step, motion } = useLiveStep()
    const activity = step >= 3 ? [UPDATED_ACTIVITY, ...recentActivity.slice(0, 2)] : recentActivity

    return (
        <section className="mh-hero" aria-labelledby="mh-hero-title">
            <div className="mh-wrap mh-grid mh-hero-head">
                <h1 id="mh-hero-title" className="mh-display mh-hero-title">
                    <span className="mh-line"><span>Every home,</span></span>{' '}
                    <span className="mh-line"><span>one clear picture</span></span>
                </h1>

                <p className="mh-hero-gloss mh-load" style={delay(240)}>
                    From resident records to staff assignments and temporary placements, Merimna brings the
                    moving parts of supported living into one connected workspace — giving teams a clearer
                    view of the people, homes and responsibilities they manage.
                </p>
            </div>

            <div className="mh-hero-stage">
                <figure className="mh-hero-window mh-load" style={delay(420)}>
                    <Workspace role="admin" active="Dashboard" email={ADMIN_EMAIL} className="mh-ws-hero">
                        <DashboardScreen activity={activity} countUp={motion} highlightFirst={step >= 3} />
                    </Workspace>
                    {step < 3 && (
                        <div className={`mh-hero-panel ${step === 2 ? 'is-closed' : ''}`} aria-hidden="true">
                            <MedicationPanel
                                title="Edit medication"
                                subtitle={EFTYCHIA.name}
                                fields={editFields}
                                action="Save changes"
                                pressed={step === 1}
                                activeField={step === 1 ? -1 : 1}
                            />
                        </div>
                    )}
                </figure>
            </div>
        </section>
    )
}

const questions = [
    'Who is assigned to which home?',
    'Who is covering somewhere else this month?',
    'Who should be able to open whose records?',
]

export function Problem() {
    return (
        <section className="mh-problem" aria-labelledby="mh-problem-title">
            <div className="mh-wrap mh-grid mh-problem-grid">
                <div className="mh-problem-head">
                    <h2 id="mh-problem-title" className="mh-title" data-reveal>
                        Questions that shouldn’t be hard
                    </h2>
                    <p className="mh-problem-text" data-reveal style={delay(90)}>
                        In supported living, the answers are often spread across spreadsheets, separate
                        files and messages, so even simple questions take longer than they should.
                        Merimna brings that information together, in one place the whole team can rely on.
                    </p>
                    <a className="mh-link mh-link-arrow mh-problem-link" href="#story" data-reveal style={delay(160)}>
                        Where these questions came from
                    </a>
                </div>

                <ol className="mh-questions">
                    {questions.map((question, index) => (
                        <li key={question} data-reveal style={delay(120 + index * 110)}>
                            {question}
                        </li>
                    ))}
                </ol>
            </div>
        </section>
    )
}
