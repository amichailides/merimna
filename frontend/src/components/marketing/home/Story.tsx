import { MerimnaLogo } from '@/components/brand/MerimnaLogo'

import { delay } from './useReveal'

export function Story() {
    return (
        <section id="story" className="mh-story" aria-labelledby="mh-story-title">
            <div className="mh-wrap mh-grid mh-story-grid">
                {/* The name, set like a dictionary entry. */}
                <div className="mh-story-entry" data-reveal>
                    <p className="mh-story-word" lang="el">
                        <span className="mh-line"><span>μέριμνα</span></span>
                    </p>
                    <p className="mh-story-say">
                        <span>mé·rim·na</span>
                        <span>Greek, noun</span>
                    </p>
                    <p className="mh-story-sense">
                        Care; the attentive concern given to another person’s wellbeing.
                    </p>
                </div>

                <div className="mh-story-body">
                    <h2 id="mh-story-title" className="mh-title" data-reveal>
                        The questions came first
                    </h2>
                    <div className="mh-story-text">
                        <p data-reveal style={delay(80)}>
                            Before Merimna had a name, those questions were already being asked. My partner
                            works in supported living, and the same ones came up again and again whenever we
                            talked about the work.
                        </p>
                        <p data-reveal style={delay(150)}>
                            None of them is hard to ask. Answering them meant opening spreadsheets, files and
                            messages, and hoping they still agreed. Merimna is built so the answer is already
                            there, for everyone who needs it.
                        </p>
                        <p className="mh-story-close" data-reveal style={delay(220)}>
                            The name is the Greek word for care. The care itself comes from people. Merimna
                            looks after what they need to know.
                        </p>
                        <div className="mh-signature" data-reveal style={delay(290)}>
                            <span className="mh-signature-mark" aria-hidden="true">
                                <MerimnaLogo className="h-5 w-5" />
                            </span>
                            <span>
                                <span className="mh-signature-name">Anastasios Michailidis</span>
                                <span className="mh-signature-role">Founder</span>
                            </span>
                        </div>
                    </div>
                </div>
            </div>
        </section>
    )
}
