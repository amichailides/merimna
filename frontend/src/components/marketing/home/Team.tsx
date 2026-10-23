import { useEffect, useRef, useState } from 'react'

import { EmployeeOverview, OnboardingForm } from './Screens'
import { ADMIN_EMAIL, NEW_HIRE, houses } from './showcase'
import { delay, useMotionAllowed } from './useReveal'
import { Workspace } from './Workspace'

// Stages of the onboarding sequence and how many ticks each one takes to fill.
const stages = [
    { label: 'Their details', ticks: 5 },
    { label: 'Address', ticks: 5 },
    { label: 'Role and home', ticks: 5 },
    { label: 'Account invitation', ticks: 4 },
    { label: 'Invitation sent', ticks: 8 },
]

const TICK_MS = 420

function useOnboardingPlayer(motion: boolean) {
    const rootRef = useRef<HTMLDivElement>(null)
    const [{ stage, tick }, setPosition] = useState(() => (motion ? { stage: 0, tick: 0 } : { stage: 4, tick: 99 }))
    const [visible, setVisible] = useState(false)
    const [held, setHeld] = useState(!motion)

    useEffect(() => {
        const el = rootRef.current
        if (!el || !('IntersectionObserver' in window)) return
        const observer = new IntersectionObserver(([entry]) => setVisible(entry.isIntersecting), { threshold: 0.35 })
        observer.observe(el)
        return () => observer.disconnect()
    }, [])

    useEffect(() => {
        if (held || !visible) return
        const id = window.setInterval(() => {
            setPosition((p) =>
                p.tick + 1 < stages[p.stage].ticks
                    ? { stage: p.stage, tick: p.tick + 1 }
                    : { stage: (p.stage + 1) % stages.length, tick: 0 },
            )
        }, TICK_MS)
        return () => window.clearInterval(id)
    }, [held, visible])

    const jump = (index: number) => {
        setHeld(true)
        setPosition({ stage: index, tick: 99 })
    }

    return { rootRef, stage, tick, held, jump, resume: () => setHeld(false) }
}

const coverPoints = [
    {
        lead: 'One official home.',
        text: 'Each person’s assignment records where they normally work.',
    },
    {
        lead: 'Cover, with its own dates.',
        text: 'When someone helps out in another home, it’s recorded separately, from the day it starts to the day it ends.',
    },
    {
        lead: 'Access that follows the cover.',
        text: 'While the cover lasts, they can open that home’s resident records too. Their own assignment stays as it was.',
    },
]

export function Team() {
    const motion = useMotionAllowed()
    const { rootRef, stage, tick, held, jump, resume } = useOnboardingPlayer(motion)
    const finished = stage === 4

    return (
        <section id="staff" className="mh-team mh-deep" aria-labelledby="mh-team-title">
            <div className="mh-wrap mh-grid mh-chapter-head">
                <h2 id="mh-team-title" className="mh-title" data-reveal>
                    Know who<br className="mh-wide-break" /> works where
                </h2>
                <p className="mh-statement" data-reveal style={delay(90)}>
                    <span>Every employee’s profile shows the home they’re assigned to and, when they’re covering somewhere else, that home too.</span>{' '}
                    Coordinators see the whole arrangement at a glance.
                </p>
            </div>

            <div className="mh-wrap mh-grid mh-cover">
                <figure className="mh-stage mh-cover-stage" data-reveal>
                    <Workspace role="admin" active="Employees" email={ADMIN_EMAIL} className="mh-ws-cover" compact>
                        <EmployeeOverview
                            name="Eleni Vasileiou"
                            position="Caregiver"
                            current={{ house: houses.PANGRATI.name, since: '17 Aug 2026' }}
                            official={{ house: houses.KYPSELI.name, since: '2 Jun 2026' }}
                        />
                    </Workspace>
                    <figcaption className="mh-cover-caption">
                        Eleni normally works at Kypseli House. Since 17 August she has been covering at
                        Pangrati House.
                    </figcaption>
                </figure>

                <ul className="mh-capabilities">
                    {coverPoints.map((c, index) => (
                        <li key={c.lead} data-reveal style={delay(80 + index * 70)}>
                            <p className="mh-capability-lead">{c.lead}</p>
                            <p className="mh-capability-text">{c.text}</p>
                        </li>
                    ))}
                </ul>
            </div>

            <div className="mh-wrap mh-grid mh-onboard-head">
                <h3 className="mh-subtitle" data-reveal>Bring someone new on board</h3>
                <p className="mh-onboard-intro" data-reveal style={delay(80)}>
                    <span>Add a new colleague with their role and first home, and invite them to set up their own account.</span>{' '}
                    You can see whether the invitation is still waiting, and send it again.
                </p>
            </div>

            <div ref={rootRef} className="mh-wrap mh-grid mh-onboard">
                <div className="mh-onboard-steps" role="group" aria-label="Onboarding sequence">
                    {stages.map((s, index) => {
                        const current = index === stage
                        const progress = current ? Math.min((tick + 1) / s.ticks, 1) : index < stage ? 1 : 0
                        return (
                            <button
                                key={s.label}
                                type="button"
                                className="mh-onboard-step"
                                aria-pressed={current}
                                onClick={() => jump(index)}
                            >
                                <span className="mh-onboard-step-label">{s.label}</span>
                                <span className="mh-onboard-step-bar" aria-hidden="true">
                                    <span style={{ transform: `scaleX(${progress})` }} />
                                </span>
                            </button>
                        )
                    })}
                    {held && motion && (
                        <button type="button" className="mh-link mh-onboard-resume" onClick={resume}>
                            Play the sequence
                        </button>
                    )}
                </div>

                <figure className="mh-stage mh-onboard-stage" data-reveal style={delay(120)}>
                    <Workspace role="admin" active="Employees" email={ADMIN_EMAIL} className="mh-ws-onboard">
                        <div key={finished ? 'profile' : 'form'} className="mh-screen">
                            {finished ? (
                                <EmployeeOverview
                                    name={`${NEW_HIRE.firstName} ${NEW_HIRE.lastName}`}
                                    position={NEW_HIRE.position}
                                    official={{ house: houses[NEW_HIRE.house].name, since: NEW_HIRE.assignmentStart }}
                                    access="pending"
                                />
                            ) : (
                                <OnboardingForm step={stage} filled={stage === 3 ? tick : Math.min(tick, 4)} />
                            )}
                        </div>
                    </Workspace>
                    <figcaption className="mh-visually-hidden">
                        Adding Sofia Andreou as a caregiver at Nea Smyrni House, with an account invitation.
                    </figcaption>
                </figure>
            </div>
        </section>
    )
}
