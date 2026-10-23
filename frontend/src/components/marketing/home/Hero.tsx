import { useEffect, useRef, useState } from 'react'

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

// The product window settles into place as the visitor starts to scroll.
function useSettle() {
    const ref = useRef<HTMLDivElement>(null)
    const motion = useMotionAllowed()

    useEffect(() => {
        const el = ref.current
        if (!el || !motion) return
        let frame = 0
        const update = () => {
            frame = 0
            const progress = Math.min(Math.max(window.scrollY / (window.innerHeight * 0.55), 0), 1)
            el.style.setProperty('--settle', String(progress))
        }
        const onScroll = () => {
            if (!frame) frame = requestAnimationFrame(update)
        }
        update()
        window.addEventListener('scroll', onScroll, { passive: true })
        return () => {
            window.removeEventListener('scroll', onScroll)
            cancelAnimationFrame(frame)
        }
    }, [motion])

    return ref
}

export function Hero() {
    const settleRef = useSettle()
    const motion = useMotionAllowed()

    return (
        <section className="mh-hero" aria-labelledby="mh-hero-title">
            <div className="mh-wrap mh-grid mh-hero-head">
                <h1 id="mh-hero-title" className="mh-display mh-hero-title">
                    <span className="mh-line"><span>Every home,</span></span>{' '}
                    <span className="mh-line"><span>one clear picture</span></span>
                </h1>

                <p className="mh-statement mh-hero-statement mh-load" style={delay(240)}>
                    <span>Merimna brings residents, staff and the everyday details of supported living together in one shared system,</span>{' '}
                    so managers, coordinators and care staff work from the same information.
                </p>

                <div className="mh-actions mh-hero-actions mh-load" style={delay(320)}>
                    <a className="mh-button" href={links.demo}>Explore the live demo</a>
                    <a className="mh-link mh-link-arrow" href="#residents">See how it works</a>
                </div>
            </div>

            <div className="mh-wrap">
                <div ref={settleRef} className="mh-hero-frame">
                    <figure className="mh-hero-stage mh-load" style={delay(380)}>
                        <Workspace role="admin" active="Dashboard" email={ADMIN_EMAIL} className="mh-ws-hero">
                            <DashboardScreen activity={recentActivity} countUp={motion} />
                        </Workspace>
                    </figure>
                </div>
                <p className="mh-hero-caption" data-reveal>
                    <span>The dashboard.</span> How full each home is, who is assigned there and who is
                    covering, and the latest changes across your organisation.
                </p>
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
