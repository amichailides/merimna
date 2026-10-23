import { securityEvents } from './showcase'
import { delay } from './useReveal'

const history = [
    { title: 'Placement started', subtitle: 'Placement · Pangrati House', time: '17 Aug 2026, 08:05' },
    { title: 'Employee updated', subtitle: 'Changed mobile number', time: '5 Aug 2026, 13:42', change: ['690 000 0412', '690 000 0417'] },
    { title: 'Assignment created', subtitle: 'Assignment · Kypseli House', time: '2 Jun 2026, 09:10' },
]

const points = [
    { lead: 'Access by role.', text: 'Each position defines what the people in it can see and do, from caregiver to administrator.' },
    { lead: 'Access by home.', text: 'Staff see the residents of the homes they work in or are covering.' },
    { lead: 'Access ends with employment.', text: 'Deactivating an employee closes their account in the same step.' },
    { lead: 'Unusual sign-ins stand out.', text: 'Failed logins and suspicious session activity appear on the dashboard.' },
]

export function Security() {
    return (
        <section id="access" className="mh-security" aria-labelledby="mh-security-title">
            <div className="mh-wrap mh-grid mh-chapter-head">
                <h2 id="mh-security-title" className="mh-title" data-reveal>
                    Sensitive information, in the right hands
                </h2>
                <p className="mh-statement" data-reveal style={delay(90)}>
                    <span>What someone can see follows their role and the homes they work in. Important changes are recorded with who made them and when,</span>{' '}
                    so there’s a clear history to look back on.
                </p>
            </div>

            <div className="mh-wrap mh-grid mh-security-grid">
                <article className="mh-feature mh-feature-history" data-reveal>
                    <h3 className="mh-feature-title">A clear history of changes</h3>
                    <p className="mh-feature-text">
                        See when someone joined, where they were assigned, when they covered another home,
                        and what changed on their profile, including the previous value.
                    </p>
                    <div data-ui className="mh-panel rounded-xl bg-white px-6 py-5 text-slate-900">
                        <p className="text-[14px] font-medium text-slate-900">Activity</p>
                        <p className="mt-1 text-[12px] text-slate-500">Audit history for profile, assignment and placement changes.</p>
                        <ol className="mt-5">
                            {history.map((item, index) => (
                                <li key={item.title} className="mh-history-item relative pb-5 pl-7 last:pb-0" style={delay(200 + index * 160)}>
                                    {index < history.length - 1 && <span className="absolute left-[3.5px] top-[14px] h-full w-px bg-slate-100" />}
                                    <span className={`absolute left-0 top-[5px] size-2 rounded-full border ${index === 0 ? 'border-teal-500 bg-teal-500' : 'border-slate-300 bg-white'}`} />
                                    <div className="flex items-baseline justify-between gap-6">
                                        <p className="text-[14px] font-semibold leading-5 text-slate-950/80">{item.title}</p>
                                        <p className="shrink-0 text-[12px] text-slate-500">{item.time}</p>
                                    </div>
                                    <p className="text-[13px] leading-5 text-slate-500">{item.subtitle}</p>
                                    {item.change && (
                                        <div className="mh-diff mt-2 inline-flex items-center gap-2 rounded-md bg-slate-50 px-2.5 py-1.5 text-[12px]">
                                            <span className="mh-diff-old text-slate-500">{item.change[0]}</span>
                                            <span className="text-slate-500">→</span>
                                            <span className="mh-diff-new font-medium text-slate-900">{item.change[1]}</span>
                                        </div>
                                    )}
                                </li>
                            ))}
                        </ol>
                    </div>
                </article>

                <article className="mh-feature mh-feature-events" data-reveal style={delay(90)}>
                    <h3 className="mh-feature-title">Security at a glance</h3>
                    <p className="mh-feature-text">
                        Managers see recent sign-in and account events alongside the day’s operational
                        changes.
                    </p>
                    <div data-ui className="mh-panel rounded-xl bg-white px-5 py-5 text-slate-900">
                        <p className="text-[15px] font-semibold text-slate-900">Security events</p>
                        <p className="mt-1 text-[13px] text-slate-500">Recent authentication and account security events.</p>
                        <ul className="mt-4 space-y-3.5">
                            {securityEvents.map((e) => (
                                <li key={e.title + e.time} className="mh-history-item flex items-start gap-3">
                                    <span className={`mt-[7px] size-2 shrink-0 rounded-full ${e.tone === 'warn' ? 'bg-amber-500' : 'bg-teal-500'}`} />
                                    <div>
                                        <p className="text-[14px] font-semibold leading-5 text-slate-950/80">{e.title}</p>
                                        <p className="text-[12px] leading-5 text-slate-500">{e.time}</p>
                                    </div>
                                </li>
                            ))}
                        </ul>
                    </div>
                </article>
            </div>

            <div className="mh-wrap">
                <ul className="mh-safeguards">
                    {points.map((s, index) => (
                        <li key={s.lead} data-reveal style={delay(index * 70)}>
                            <p className="mh-safeguard-lead">{s.lead}</p>
                            <p className="mh-safeguard-text">{s.text}</p>
                        </li>
                    ))}
                </ul>
            </div>
        </section>
    )
}
