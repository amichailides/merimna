export const links = {
    demo: '/login',
    github: 'https://github.com/amichailides/merimna',
    issues: 'https://github.com/amichailides/merimna/issues',
    adrs: 'https://github.com/amichailides/merimna/tree/main/docs/adr',
    api: 'https://api.merimna.care/api/scalar',
    email: 'mailto:michailides.a@gmail.com',
    emailAddress: 'michailides.a@gmail.com',
    linkedin: 'https://www.linkedin.com/in/amichailides/',
    security: 'https://github.com/amichailides/merimna#security--access-control',
    contact: 'mailto:michailides.a@gmail.com?subject=Merimna%20for%20our%20homes',
}

export function adrLink(file: string) {
    return `https://github.com/amichailides/merimna/blob/main/docs/adr/${file}`
}
