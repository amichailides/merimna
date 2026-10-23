import { useRef } from 'react'

import { Capabilities } from '@/components/marketing/home/Capabilities'
import { Closing, SiteFooter } from '@/components/marketing/home/Closing'
import { Hero, Problem, SiteNav } from '@/components/marketing/home/Hero'
import { Security } from '@/components/marketing/home/Security'
import { Story } from '@/components/marketing/home/Story'
import { Team } from '@/components/marketing/home/Team'
import { Tour } from '@/components/marketing/home/Tour'
import { useReveal } from '@/components/marketing/home/useReveal'

import '@fontsource-variable/inter/opsz.css'
import '@fontsource-variable/literata/opsz.css'
import '@/components/marketing/home/home.css'

export default function LandingPage() {
    const rootRef = useRef<HTMLDivElement>(null)
    useReveal(rootRef)

    return (
        <div ref={rootRef} className="mh">
            <a className="mh-skip" href="#mh-main">
                Skip to content
            </a>

            <SiteNav />

            <main id="mh-main">
                <span id="top" />
                <Hero />
                <Problem />
                <Tour />
                <Team />
                <Security />
                <Capabilities />
                <Story />
                <Closing />
            </main>

            <SiteFooter />
        </div>
    )
}
