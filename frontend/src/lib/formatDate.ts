export function formatDate(date: string | null | undefined): string {
    if (!date) return '—'

    return new Intl.DateTimeFormat('en-GB', {
        day: 'numeric',
        month: 'short',
        year: 'numeric',
    }).format(new Date(date))
}

export function formatDateRange(
    startDate: string | null | undefined,
    endDate: string | null | undefined,
    ongoingLabel = 'Present',
): string {
    return `${formatDate(startDate)} → ${endDate ? formatDate(endDate) : ongoingLabel}`
}