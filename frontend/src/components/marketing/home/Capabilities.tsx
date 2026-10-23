import { delay } from './useReveal'

// Every item here has a working screen in the application today.
const groups = [
    {
        name: 'Homes',
        items: ['House units and their capacity', 'Occupancy at a glance', 'Assigned and covering staff for each home'],
    },
    {
        name: 'Residents',
        items: [
            'One profile for each resident',
            'Search by name or AMKA',
            'Medications with dose, times and instructions',
            'Allergies and reactions',
            'Legal representatives and their contact details',
            'Current and discharged residents',
        ],
    },
    {
        name: 'Staff',
        items: [
            'Employee profiles and positions',
            'Official home and temporary cover',
            'Onboarding with account invitations',
            'Deactivation and reactivation',
        ],
    },
    {
        name: 'Oversight',
        items: [
            'An organisation-wide dashboard',
            'Recent activity across your homes',
            'A change history for each employee',
            'Sign-in and account security events',
        ],
    },
]

export function Capabilities() {
    return (
        <section className="mh-capability-band" aria-labelledby="mh-capabilities-title">
            <div className="mh-wrap mh-grid mh-index">
                <div className="mh-index-head">
                    <h2 id="mh-capabilities-title" className="mh-title" data-reveal>
                        Everything in one place
                    </h2>
                    <p className="mh-statement" data-reveal style={delay(90)}>
                        <span>The essentials of running supported living homes, in one system.</span>{' '}
                        Here is what Merimna covers today.
                    </p>
                </div>

                <div className="mh-index-groups">
                    {groups.map((group, index) => (
                        <section key={group.name} className="mh-index-group" data-reveal style={delay(index * 80)}>
                            <h3 className="mh-index-name">{group.name}</h3>
                            <ul>
                                {group.items.map((item) => (
                                    <li key={item}>{item}</li>
                                ))}
                            </ul>
                        </section>
                    ))}
                </div>
            </div>
        </section>
    )
}
