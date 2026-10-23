// Curated presentation fixtures for the public homepage.
//
// Everything here is fictional and written for the showcases: homes, people, dates,
// contact details and medications. None of it comes from the database or from
// dev/demo-data.sql. Every scenario follows what the application really does.

export type HouseCode = 'KYPSELI' | 'PANGRATI' | 'NEA_SMYRNI'

export const houses: Record<HouseCode, { name: string; capacity: number; residents: number; assigned: number; placed: number }> = {
    KYPSELI: { name: 'Kypseli House', capacity: 5, residents: 4, assigned: 6, placed: 1 },
    PANGRATI: { name: 'Pangrati House', capacity: 4, residents: 3, assigned: 5, placed: 1 },
    NEA_SMYRNI: { name: 'Nea Smyrni House', capacity: 6, residents: 5, assigned: 5, placed: 0 },
}

export const houseCodes = Object.keys(houses) as HouseCode[]

export const ADMIN_EMAIL = 'g.zafeiriou@example.org'

// Organisation overview on 21 September 2026.
export const summary = [
    { label: 'Employees', value: 18 },
    { label: 'Beneficiaries', value: 12 },
    { label: 'House units', value: 3 },
    { label: 'Active assignments', value: 16 },
    { label: 'Active placements', value: 2 },
]

export type Activity = { title: string; subtitle: string; time: string }

export const recentActivity: Activity[] = [
    { title: 'Placement Created', subtitle: 'Employee Placement · Athina Rafailidou', time: '18 Sep 2026, 07:45' },
    { title: 'Employee Created', subtitle: 'Employee · Sofia Andreou', time: '14 Sep 2026, 11:20' },
    { title: 'Beneficiary Discharged', subtitle: 'Beneficiary · Stavros Polychronis', time: '2 Sep 2026, 12:10' },
]

export const securityEvents = [
    { title: 'Refresh token reuse detected', tone: 'warn' as const, time: '19 Sep 2026, 22:41' },
    { title: 'Password changed', tone: 'ok' as const, time: '19 Sep 2026, 09:12' },
    { title: 'Failed login attempt', tone: 'warn' as const, time: '18 Sep 2026, 07:58' },
    { title: 'Password reset', tone: 'ok' as const, time: '16 Sep 2026, 17:30' },
]

export const employees = [
    { name: 'Eleni Vasileiou', position: 'Caregiver', house: 'KYPSELI' as HouseCode, active: true },
    { name: 'Thodoris Margaritis', position: 'Caregiver', house: 'PANGRATI' as HouseCode, active: true },
    { name: 'Athina Rafailidou', position: 'Caregiver', house: 'NEA_SMYRNI' as HouseCode, active: true },
    { name: 'Stelios Karamanos', position: 'House Manager', house: 'PANGRATI' as HouseCode, active: true },
    { name: 'Christina Lambraki', position: 'Social Worker', house: 'NEA_SMYRNI' as HouseCode, active: true },
    { name: 'Markos Deligiannis', position: 'Caregiver', house: 'KYPSELI' as HouseCode, active: false },
]

export type Medication = { name: string; dosage: string; frequency: string; times: string; instructions: string }
export type Allergy = { substance: string; severity: 'Low' | 'Medium' | 'High'; reaction: string }
export type Representative = { name: string; type: string; mobile: string; email: string }

export type Resident = {
    name: string
    house: HouseCode
    active: boolean
    medications: Medication[]
    allergies: Allergy[]
    representatives: Representative[]
}

const none = { medications: [], allergies: [], representatives: [] }

export const EFTYCHIA: Resident = {
    name: 'Eftychia Savvidou',
    house: 'PANGRATI',
    active: true,
    medications: [
        { name: 'Levothyroxine', dosage: '50 mcg', frequency: 'Once daily', times: '07:00', instructions: 'On an empty stomach, 30 minutes before breakfast' },
        { name: 'Metformin', dosage: '500 mg', frequency: 'Twice daily', times: '08:00, 20:00', instructions: 'After meals' },
        { name: 'Vitamin D3', dosage: '1000 IU', frequency: 'Once daily', times: '09:00', instructions: 'With breakfast' },
    ],
    allergies: [
        { substance: 'Penicillin', severity: 'High', reaction: 'Hives and shortness of breath' },
        { substance: 'Latex', severity: 'Low', reaction: 'Skin irritation on contact' },
    ],
    representatives: [
        { name: 'Gerasimos Savvidis', type: 'Legal guardian', mobile: '690 000 0233', email: 'g.savvidis@example.org' },
    ],
}

// The medication the tour adds, and the dose change it records.
export const NEW_MEDICATION: Medication & { start: string } = {
    name: 'Amlodipine',
    dosage: '5 mg',
    frequency: 'Once daily',
    times: '08:00',
    instructions: 'With water after breakfast',
    start: '21 Sep 2026',
}

export const DOSE_CHANGE = { medication: 'Metformin', from: '500 mg', to: '850 mg' }

export const residents: Resident[] = [
    EFTYCHIA,
    { name: 'Ilias Bakalis', house: 'KYPSELI', active: true, ...none },
    { name: 'Despoina Vasileiou', house: 'NEA_SMYRNI', active: true, ...none },
    { name: 'Lefteris Kotsonis', house: 'KYPSELI', active: true, ...none },
    { name: 'Marina Georgiadou', house: 'KYPSELI', active: true, ...none },
    { name: 'Foteini Argyriou', house: 'PANGRATI', active: true, ...none },
    { name: 'Konstantina Riga', house: 'NEA_SMYRNI', active: true, ...none },
    { name: 'Antonis Liapis', house: 'PANGRATI', active: true, ...none },
    { name: 'Pavlos Dokos', house: 'KYPSELI', active: true, ...none },
    { name: 'Michalis Stavridis', house: 'NEA_SMYRNI', active: true, ...none },
    { name: 'Ioulia Petropoulou', house: 'NEA_SMYRNI', active: true, ...none },
    { name: 'Giannis Chatzis', house: 'NEA_SMYRNI', active: true, ...none },
]

// The new colleague the onboarding sequence adds.
export const NEW_HIRE = {
    firstName: 'Sofia',
    lastName: 'Andreou',
    dateOfBirth: '12 Mar 1994',
    hireDate: '14 Sep 2026',
    mobile: '690 000 0561',
    email: 'sofia.andreou@example.org',
    street: 'Agiou Sostou',
    streetNumber: '27',
    city: 'Athens',
    postalCode: '17121',
    position: 'Caregiver',
    house: 'NEA_SMYRNI' as HouseCode,
    assignmentStart: '1 Oct 2026',
    invitationExpires: '15 Sep 2026, 11:20',
}

export function initials(name: string) {
    return name
        .split(' ')
        .map((part) => part.charAt(0))
        .join('')
        .slice(0, 2)
        .toUpperCase()
}
