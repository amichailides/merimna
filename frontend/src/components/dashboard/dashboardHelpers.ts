export function getMetadataString(
    metadata: unknown,
    key: string,
): string | null {
    if (
        !metadata ||
        typeof metadata !== 'object' ||
        Array.isArray(metadata)
    ) {
        return null
    }

    const value = (metadata as Record<string, unknown>)[key]

    return typeof value === 'string' ? value : null
}

export function formatActivityDate(
    occurredAt?: string,
): string {
    if (!occurredAt) {
        return ''
    }

    return new Intl.DateTimeFormat('en-GB', {
        day: '2-digit',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    }).format(new Date(occurredAt))
}
