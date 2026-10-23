import { useEffect, useState, type CSSProperties, type RefObject } from 'react'

// Stagger offset for an entrance, read by the stylesheet as --d.
export function delay(ms: number) {
    return { '--d': `${ms}ms` } as CSSProperties
}

// Marks [data-reveal] and .mh-shutter elements as they enter the viewport, once.
// Content stays visible when this never runs: hiding is gated on the root's .mh-js class.
export function useReveal(rootRef: RefObject<HTMLElement | null>) {
    useEffect(() => {
        const root = rootRef.current
        if (!root) return

        root.classList.add('mh-js')

        const targets = root.querySelectorAll<HTMLElement>('[data-reveal], .mh-shutter')

        if (!('IntersectionObserver' in window)) {
            targets.forEach((target) => target.classList.add('is-in'))
            return
        }

        const observer = new IntersectionObserver(
            (entries) => {
                for (const entry of entries) {
                    if (entry.isIntersecting) {
                        entry.target.classList.add('is-in')
                        observer.unobserve(entry.target)
                    }
                }
            },
            { rootMargin: '0px 0px -12% 0px', threshold: 0.08 },
        )

        targets.forEach((target) => observer.observe(target))

        return () => observer.disconnect()
    }, [rootRef])
}

const MOTION_QUERY = '(prefers-reduced-motion: no-preference)'

// True when the visitor has not asked for reduced motion; follows changes live.
export function useMotionAllowed() {
    const [allowed, setAllowed] = useState(() => window.matchMedia(MOTION_QUERY).matches)

    useEffect(() => {
        const list = window.matchMedia(MOTION_QUERY)
        const update = () => setAllowed(list.matches)
        list.addEventListener('change', update)
        return () => list.removeEventListener('change', update)
    }, [])

    return allowed
}

// True when the viewport is at least `width` wide.
export function useWide(width: number) {
    const query = `(min-width: ${width}px)`
    const [wide, setWide] = useState(() => window.matchMedia(query).matches)

    useEffect(() => {
        const list = window.matchMedia(query)
        const update = () => setWide(list.matches)
        list.addEventListener('change', update)
        return () => list.removeEventListener('change', update)
    }, [query])

    return wide
}

// Progress (0-1) of the viewport through a tall track that holds a sticky stage.
export function useTrackProgress(ref: RefObject<HTMLElement | null>, enabled: boolean) {
    const [progress, setProgress] = useState(0)

    useEffect(() => {
        if (!enabled) return
        let frame = 0
        const update = () => {
            frame = 0
            const el = ref.current
            if (!el) return
            const rect = el.getBoundingClientRect()
            const travel = rect.height - window.innerHeight
            setProgress(travel > 0 ? Math.min(Math.max(-rect.top / travel, 0), 1) : 0)
        }
        const onScroll = () => {
            if (!frame) frame = requestAnimationFrame(update)
        }
        update()
        window.addEventListener('scroll', onScroll, { passive: true })
        window.addEventListener('resize', onScroll)
        return () => {
            window.removeEventListener('scroll', onScroll)
            window.removeEventListener('resize', onScroll)
            cancelAnimationFrame(frame)
        }
    }, [ref, enabled])

    return progress
}
