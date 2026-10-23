import { MerimnaLogo } from '@/components/brand/MerimnaLogo'

import { links } from './links'
import { delay } from './useReveal'

export function Closing() {
    return (
        <section className="mh-closing mh-deep" aria-labelledby="mh-closing-title">
            <div className="mh-wrap mh-closing-inner">
                <h2 id="mh-closing-title" className="mh-display mh-closing-title" data-reveal>
                    See Merimna running
                </h2>
                <p className="mh-statement mh-closing-text" data-reveal style={delay(90)}>
                    <span>The demo runs on sample homes, residents and staff.</span> Sign in and look
                    around. Nothing you do there affects real people.
                </p>
                <div className="mh-actions mh-closing-actions" data-reveal style={delay(160)}>
                    <a className="mh-button mh-button-light" href={links.demo}>Explore the live demo</a>
                    <a className="mh-link mh-link-arrow" href={links.demo}>Sign in</a>
                </div>
                <p className="mh-closing-contact" data-reveal style={delay(230)}>
                    Working in supported living?{' '}
                    <a className="mh-link" href={links.contact}>Tell us how your homes work.</a>
                </p>
            </div>
        </section>
    )
}

export function SiteFooter() {
    return (
        <footer className="mh-footer mh-deep" aria-labelledby="mh-footer-title">
            <div className="mh-wrap">
                <h2 id="mh-footer-title" className="mh-visually-hidden">Merimna</h2>

                <div className="mh-grid mh-footer-top">
                    <div className="mh-footer-brand">
                        <span className="mh-footer-mark" aria-hidden="true">
                            <MerimnaLogo className="mh-footer-logo" />
                            <span>merimna</span>
                        </span>
                        <p className="mh-footer-statement">
                            One clear picture of every home: residents, staff and the changes that matter.
                            Designed and built by Anastasios Michailidis.
                        </p>
                    </div>

                    <nav className="mh-footer-col" aria-label="Product">
                        <p className="mh-footer-heading">Product</p>
                        <a className="mh-link" href="#residents">Residents</a>
                        <a className="mh-link" href="#staff">Staff</a>
                        <a className="mh-link" href="#access">Access and history</a>
                        <a className="mh-link" href="#story">Story</a>
                    </nav>

                    <nav className="mh-footer-col" aria-label="Resources">
                        <p className="mh-footer-heading">Resources</p>
                        <a className="mh-link" href={links.demo}>Live demo</a>
                        <a className="mh-link" href={links.api} target="_blank" rel="noreferrer">API reference</a>
                        <a className="mh-link" href={links.github} target="_blank" rel="noreferrer">Source code</a>
                        <a className="mh-link" href={links.security} target="_blank" rel="noreferrer">Security and architecture</a>
                        <a className="mh-link" href={links.adrs} target="_blank" rel="noreferrer">Architecture decisions</a>
                    </nav>

                    <nav className="mh-footer-col" aria-label="Contact">
                        <p className="mh-footer-heading">Contact</p>
                        <a className="mh-link" href={links.email}>{links.emailAddress}</a>
                        <a className="mh-link" href={links.linkedin} target="_blank" rel="noreferrer">LinkedIn</a>
                    </nav>
                </div>

                <p className="mh-footer-dev">
                    For developers: the{' '}
                    <a className="mh-link" href={links.github} target="_blank" rel="noreferrer">source code</a>, an{' '}
                    <a className="mh-link" href={links.api} target="_blank" rel="noreferrer">interactive API reference</a> and the{' '}
                    <a className="mh-link" href={links.adrs} target="_blank" rel="noreferrer">architecture decisions</a> are public.
                </p>

                <div className="mh-footer-bottom">
                    <span>© {new Date().getFullYear()} Anastasios Michailidis</span>
                    <span>People, homes and records shown on this page are fictional.</span>
                </div>
            </div>
        </footer>
    )
}
