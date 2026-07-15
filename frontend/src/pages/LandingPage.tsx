import productScreenshot from '@/assets/product-screenshot.png'

const modeledWorkflows = [
    {
        title: 'People & facilities',
        description:
            'Employees, beneficiaries, positions, and supported living facilities represented as connected domain entities.',
    },
    {
        title: 'Assignments & placements',
        description:
            'Official staff assignments are modeled separately from temporary placements, including their lifecycle and history.',
    },
    {
        title: 'Access & accountability',
        description:
            'Permission-aware access and an activity trail for important employee, assignment, and placement changes.',
    },
]

const technicalHighlights = [
    {
        title: 'Authentication & authorization',
        description:
            'JWT authentication with refresh token rotation, reuse detection, and fine-grained permission-based access.',
    },
    {
        title: 'Business rules',
        description:
            'Validation around dates, active assignments, temporary placements, employee lifecycle actions, and facility access.',
    },
    {
        title: 'Audit trail',
        description:
            'Event-driven activity records that capture important domain changes without coupling audit logic to each workflow.',
    },
]

const techStack = [
    'Java 21',
    'Spring Boot 4',
    'Spring Security 7',
    'PostgreSQL',
    'React',
    'TypeScript',
    'Docker',
    'Tailwind CSS',
]

function LogoMark({ size = 22 }: { size?: number }) {
    return (
        <div
            className="rounded-md bg-teal-600"
            style={{ width: size, height: size }}
            aria-hidden="true"
        />
    )
}

export default function LandingPage() {
    return (
        <div className="min-h-screen w-full overflow-x-hidden bg-[#fdfcfa] text-slate-900">
            <header className="sticky top-0 z-10 border-b border-slate-200 bg-[#fdfcfa]/90 backdrop-blur-sm">
                <div className="mx-auto flex max-w-[1120px] items-center justify-between px-6 py-5 sm:px-8">
                    <a href="#top" className="flex items-center gap-[9px]">
                        <LogoMark />

                        <span className="text-[16px] font-bold tracking-tight">
                            merimna
                        </span>
                    </a>

                    <nav className="hidden items-center gap-7 md:flex">
                        <a
                            href="#story"
                            className="text-sm font-medium text-slate-600 transition-colors hover:text-slate-900"
                        >
                            The story
                        </a>

                        <a
                            href="#workflows"
                            className="text-sm font-medium text-slate-600 transition-colors hover:text-slate-900"
                        >
                            What it models
                        </a>

                        <a
                            href="#technical"
                            className="text-sm font-medium text-slate-600 transition-colors hover:text-slate-900"
                        >
                            Technical
                        </a>

                        <a
                            href="/login"
                            className="rounded-md bg-teal-600 px-4 py-2 text-sm font-semibold text-white transition-colors hover:bg-teal-700"
                        >
                            Open demo
                        </a>
                    </nav>
                </div>
            </header>

            <main id="top">
                <section className="px-6 pt-16 sm:px-8 sm:pt-20">
                    <div className="mx-auto max-w-[780px] text-center">
                        <h1 className="mx-auto mb-5 max-w-[680px] text-[38px] font-semibold leading-[1.08] tracking-[-0.025em] text-slate-950 sm:text-[48px]">
                            A personal project,
                            <br className="hidden sm:block" />
                            grounded in a real story.
                        </h1>

                        <p className="mx-auto mb-8 max-w-[650px] text-[16px] leading-relaxed text-slate-600 sm:text-[17px]">
                            Inspired by the operational complexity of supported living services,
                            Merimna explores how people, facilities, assignments, and access can be
                            modeled in one structured system.
                        </p>

                        <div className="mb-[72px] flex flex-wrap justify-center gap-3">
                            <a
                                href="/login"
                                className="rounded-lg bg-teal-600 px-[22px] py-3 text-[15px] font-semibold text-white transition-colors hover:bg-teal-700"
                            >
                                Explore the demo
                            </a>

                            <a
                                href="https://github.com/amichailides/merimna"
                                target="_blank"
                                rel="noreferrer"
                                className="rounded-lg border border-slate-300 px-[22px] py-3 text-[15px] font-semibold text-slate-900 transition-colors hover:border-slate-400"
                            >
                                View source code
                            </a>
                        </div>
                    </div>

                    <div className="mx-auto max-w-[980px] pb-24">
                        <div className="overflow-hidden rounded-xl border border-slate-200 bg-white shadow-[0_20px_50px_-25px_rgba(20,20,30,0.35)]">
                            <div className="flex items-center gap-1.5 border-b border-slate-100 px-4 py-[11px]">
                                <div className="h-[9px] w-[9px] rounded-full bg-slate-200" />
                                <div className="h-[9px] w-[9px] rounded-full bg-slate-200" />
                                <div className="h-[9px] w-[9px] rounded-full bg-slate-200" />
                            </div>

                            <img
                                src={productScreenshot}
                                alt="Employee management page in Merimna"
                                className="block h-auto w-full"
                            />
                        </div>
                    </div>
                </section>

                <section
                    id="story"
                    className="mx-auto max-w-[1120px] border-t border-slate-100 px-6 py-20 sm:px-8"
                >
                    <div className="grid gap-12 lg:grid-cols-[0.75fr_1.25fr] lg:gap-20">
                        <div>
                            <p className="mb-3 text-xs font-bold uppercase tracking-[0.14em] text-teal-600">
                                The story behind Merimna
                            </p>

                            <h2 className="text-[30px] font-bold leading-tight tracking-tight">
                                I wanted to build around a real domain.
                            </h2>
                        </div>

                        <div className="space-y-5 text-[16px] leading-7 text-slate-600">
                            <p>
                                While studying software development at Coding Factory, I
                                wanted my main project to be more than another task manager,
                                online store, or tutorial application.
                            </p>

                            <p>
                                My partner works with an organization that supports people with
                                disabilities through supported living facilities. Through our
                                conversations, I became familiar with some of the operational
                                complexity behind the work: employees assigned to facilities,
                                temporary staff movements, changing employment states, and
                                access to information across different units.
                            </p>

                            <p>
                                Merimna became my attempt to understand and model part of that
                                domain as a structured full-stack system. It is not a product
                                commissioned by a care organization, but an independently
                                developed portfolio project grounded in a real-world context.
                            </p>
                        </div>
                    </div>
                </section>

                <section
                    id="workflows"
                    className="border-y border-slate-100 bg-white"
                >
                    <div className="mx-auto max-w-[1120px] px-6 py-20 sm:px-8">
                        <div className="mx-auto mb-12 max-w-[650px] text-center">
                            <p className="mb-3 text-xs font-bold uppercase tracking-[0.14em] text-teal-600">
                                What I chose to model
                            </p>

                            <h2 className="mb-4 text-[30px] font-bold tracking-tight">
                                A focused part of supported-living operations
                            </h2>

                            <p className="text-base leading-relaxed text-slate-600">
                                The project does not try to represent every aspect of care.
                                Its scope focuses on people, facilities, workforce movements,
                                permissions, and operational history.
                            </p>
                        </div>

                        <div className="grid gap-6 md:grid-cols-3">
                            {modeledWorkflows.map((workflow) => (
                                <article
                                    key={workflow.title}
                                    className="rounded-xl border border-slate-200 bg-[#fdfcfa] p-6"
                                >
                                    <h3 className="mb-3 text-[16px] font-bold text-slate-900">
                                        {workflow.title}
                                    </h3>

                                    <p className="text-sm leading-relaxed text-slate-500">
                                        {workflow.description}
                                    </p>
                                </article>
                            ))}
                        </div>
                    </div>
                </section>

                <section
                    id="technical"
                    className="mx-auto max-w-[1120px] px-6 py-20 sm:px-8"
                >
                    <div className="mb-12 max-w-[650px]">
                        <p className="mb-3 text-xs font-bold uppercase tracking-[0.14em] text-teal-600">
                            Behind the system
                        </p>

                        <h2 className="mb-4 text-[30px] font-bold tracking-tight">
                            The technical problems I wanted to explore
                        </h2>

                        <p className="text-base leading-relaxed text-slate-600">
                            Merimna gave me a domain in which authentication, authorization,
                            validation, lifecycle rules, and auditability were meaningful
                            parts of the application rather than isolated technical
                            exercises.
                        </p>
                    </div>

                    <div className="grid gap-8 md:grid-cols-3">
                        {technicalHighlights.map((highlight) => (
                            <article
                                key={highlight.title}
                                className="border-t border-slate-300 pt-5"
                            >
                                <h3 className="mb-2 text-[16px] font-bold text-slate-900">
                                    {highlight.title}
                                </h3>

                                <p className="text-sm leading-relaxed text-slate-500">
                                    {highlight.description}
                                </p>
                            </article>
                        ))}
                    </div>
                </section>

                <section className="border-y border-slate-100 bg-white">
                    <div className="mx-auto max-w-[1120px] px-6 py-20 sm:px-8">
                        <div className="grid items-center gap-10 md:grid-cols-[1fr_auto]">
                            <div className="max-w-[650px]">
                                <p className="mb-3 text-xs font-bold uppercase tracking-[0.14em] text-teal-600">
                                    Explore the project
                                </p>

                                <h2 className="mb-4 text-[28px] font-bold tracking-tight">
                                    See the application and the decisions behind it
                                </h2>

                                <p className="text-base leading-relaxed text-slate-600">
                                    The live environment demonstrates the current implementation,
                                    while the repository documents the architecture, API, domain
                                    decisions, and development process.
                                </p>
                            </div>

                            <div className="flex flex-wrap gap-3 md:justify-end">
                                <a
                                    href="/login"
                                    className="rounded-lg bg-teal-600 px-5 py-3 text-sm font-semibold text-white transition-colors hover:bg-teal-700"
                                >
                                    Open demo
                                </a>

                                <a
                                    href="https://github.com/amichailides/merimna"
                                    target="_blank"
                                    rel="noreferrer"
                                    className="rounded-lg border border-slate-300 px-5 py-3 text-sm font-semibold text-slate-900 transition-colors hover:border-slate-400"
                                >
                                    GitHub repository
                                </a>
                            </div>
                        </div>
                    </div>
                </section>

                <section className="mx-auto max-w-[1120px] px-6 pb-[88px] text-center sm:px-8">
                    <h2 className="my-10 text-[22px] font-semibold tracking-tight text-slate-700">
                        Built with
                    </h2>

                    <div className="flex flex-wrap justify-center gap-2.5">
                        {techStack.map((technology) => (
                            <span
                                key={technology}
                                className="rounded-md border border-slate-200 px-3.5 py-[7px] text-[13.5px] font-medium text-slate-600"
                            >
                                {technology}
                            </span>
                        ))}
                    </div>
                </section>
            </main>

            <footer className="border-t border-slate-100">
                <div className="mx-auto grid max-w-[1120px] gap-10 px-6 pb-8 pt-12 sm:px-8 md:grid-cols-[1.5fr_1fr_1fr]">
                    <div>
                        <div className="mb-3 flex items-center gap-[9px]">
                            <LogoMark size={20} />

                            <span className="text-[15px] font-bold">merimna</span>
                        </div>

                        <p className="max-w-[360px] text-[13.5px] leading-relaxed text-slate-500">
                            A personal full-stack project inspired by supported living
                            services and developed as part of my transition into software
                            engineering.
                        </p>
                    </div>

                    <div>
                        <div className="mb-3 text-xs font-bold uppercase tracking-wide text-slate-400">
                            Project
                        </div>

                        <div className="flex flex-col gap-[9px] text-[13.5px]">
                            <a
                                href="/login"
                                className="text-slate-700 transition-colors hover:text-teal-600"
                            >
                                Live demo
                            </a>

                            <a
                                href="https://github.com/amichailides/merimna"
                                target="_blank"
                                rel="noreferrer"
                                className="text-slate-700 transition-colors hover:text-teal-600"
                            >
                                GitHub repository
                            </a>
                        </div>
                    </div>

                    <div>
                        <div className="mb-3 text-xs font-bold uppercase tracking-wide text-slate-400">
                            Navigation
                        </div>

                        <div className="flex flex-col gap-[9px] text-[13.5px]">
                            <a
                                href="#story"
                                className="text-slate-700 transition-colors hover:text-teal-600"
                            >
                                The story
                            </a>

                            <a
                                href="#workflows"
                                className="text-slate-700 transition-colors hover:text-teal-600"
                            >
                                What it models
                            </a>

                            <a
                                href="#technical"
                                className="text-slate-700 transition-colors hover:text-teal-600"
                            >
                                Technical focus
                            </a>
                        </div>
                    </div>
                </div>

                <div className="border-t border-slate-100 px-8 py-4 text-center text-[12.5px] text-slate-400">
                    © 2026 Merimna — personal full-stack project
                </div>
            </footer>
        </div>
    )
}