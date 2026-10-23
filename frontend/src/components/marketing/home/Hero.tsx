import { useEffect, useState } from 'react'

import { MerimnaLogo } from '@/components/brand/MerimnaLogo'

import { links } from './links'
import { DashboardScreen } from './Screens'
import { ADMIN_EMAIL, recentActivity } from './showcase'
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

export function Hero() {
    const motion = useMotionAllowed()

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
                        <DashboardScreen activity={recentActivity} countUp={motion} />
                    </Workspace>
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
