import type { EmployeeActivityDTO } from '@/api/types'

type AuditAction = NonNullable<EmployeeActivityDTO['action']>
type ActivityMetadata = EmployeeActivityDTO['metadata']

function asMetadataRecord(metadata: ActivityMetadata): Record<string, unknown> | null {
    if (!metadata || typeof metadata !== 'object') return null

    return metadata as Record<string, unknown>
}

function formatDate(value: unknown): string | null {
    if (typeof value !== 'string' || value.length === 0) return null

    return new Date(value).toLocaleDateString('en-GB', {
        day: 'numeric',
        month: 'short',
        year: 'numeric',
    })
}

export function formatActivityTimestamp(isoInstant: string | undefined): string {
    if (!isoInstant) return ''

    return new Date(isoInstant).toLocaleString('en-GB', {
        day: 'numeric',
        month: 'short',
        year: 'numeric',
        hour: '2-digit',
        minute: '2-digit',
    })
}

function formatActivityDateOnly(isoInstant: string | undefined): string | null {
    if (!isoInstant) return null

    return new Date(isoInstant).toLocaleDateString('en-GB', {
        day: 'numeric',
        month: 'short',
        year: 'numeric',
    })
}

function isSameDisplayDate(dateValue: unknown, isoInstant: string | undefined): boolean {
    const domainDate = formatDate(dateValue)
    const activityDate = formatActivityDateOnly(isoInstant)

    return domainDate !== null && activityDate !== null && domainDate === activityDate
}

function getString(metadata: ActivityMetadata, key: string): string | null {
    const metadataRecord = asMetadataRecord(metadata)
    if (!metadataRecord) return null

    const value = metadataRecord[key]
    return typeof value === 'string' && value.length > 0 ? value : null
}

function formatFieldName(fieldName: string): string {
    return fieldName
        .replace(/([A-Z])/g, ' $1')
        .toLowerCase()
        .trim()
}

function formatEmployeeUpdated(metadata: ActivityMetadata): string | null {
    const metadataRecord = asMetadataRecord(metadata)
    const changes = metadataRecord?.changes

    if (!Array.isArray(changes) || changes.length === 0) return null

    const fields = changes
        .map(change => {
            if (!change || typeof change !== 'object') return null

            const obj = change as Record<string, unknown>
            return typeof obj.fieldName === 'string' ? obj.fieldName : null
        })
        .filter((field): field is string => field !== null)
        .map(formatFieldName)

    return fields.length > 0 ? `Changed ${fields.join(', ')}` : null
}

function formatAssignmentCreated(
    metadata: ActivityMetadata,
    occurredAt: string | undefined,
): string {
    const metadataRecord = asMetadataRecord(metadata)

    const houseUnitDisplayName = getString(metadata, 'houseUnitDisplayName')
    const startDate = formatDate(metadataRecord?.startDate)
    const showStartDate = !isSameDisplayDate(metadataRecord?.startDate, occurredAt)

    if (houseUnitDisplayName && startDate && showStartDate) {
        return `Officially assigned to ${houseUnitDisplayName} from ${startDate}`
    }

    if (houseUnitDisplayName) {
        return `Officially assigned to ${houseUnitDisplayName}`
    }

    if (startDate && showStartDate) {
        return `Official assignment started on ${startDate}`
    }

    return 'Official house unit assignment was created'
}

function formatAssignmentTerminated(metadata: ActivityMetadata): string {
    const metadataRecord = asMetadataRecord(metadata)

    const houseUnitDisplayName = getString(metadata, 'houseUnitDisplayName')
    const endDate = formatDate(metadataRecord?.endDate)

    if (houseUnitDisplayName && endDate) {
        return `Official assignment to ${houseUnitDisplayName} ended on ${endDate}`
    }

    if (houseUnitDisplayName) {
        return `Official assignment to ${houseUnitDisplayName} ended`
    }

    if (endDate) {
        return `Official assignment ended on ${endDate}`
    }

    return 'Official house unit assignment was terminated'
}

function formatAssignmentCancelled(metadata: ActivityMetadata): string {
    const metadataRecord = asMetadataRecord(metadata)

    const houseUnitDisplayName = getString(metadata, 'houseUnitDisplayName')
    const startDate = formatDate(metadataRecord?.startDate)

    if (houseUnitDisplayName && startDate) {
        return `Scheduled assignment to ${houseUnitDisplayName} from ${startDate} was cancelled`
    }

    if (houseUnitDisplayName) {
        return `Official assignment to ${houseUnitDisplayName} was cancelled`
    }

    return 'Official house unit assignment was cancelled'
}

function formatPlacementCreated(
    metadata: ActivityMetadata,
    occurredAt: string | undefined,
): string {
    const metadataRecord = asMetadataRecord(metadata)

    const houseUnitDisplayName = getString(metadata, 'houseUnitDisplayName')
    const reasonDisplayName = getString(metadata, 'reasonDisplayName')
    const startDate = formatDate(metadataRecord?.startDate)
    const endDate = formatDate(metadataRecord?.endDate)
    const showStartDate = !isSameDisplayDate(metadataRecord?.startDate, occurredAt)

    const reason = reasonDisplayName ?? 'Temporary placement'

    if (houseUnitDisplayName && endDate) {
        return `${reason} at ${houseUnitDisplayName} until ${endDate}`
    }

    if (houseUnitDisplayName && startDate && showStartDate) {
        return `${reason} at ${houseUnitDisplayName} from ${startDate}`
    }

    if (houseUnitDisplayName) {
        return `${reason} at ${houseUnitDisplayName}`
    }

    if (startDate && showStartDate) {
        return `${reason} started on ${startDate}`
    }

    return 'Temporary placement was started'
}

function formatPlacementTerminated(metadata: ActivityMetadata): string {
    const metadataRecord = asMetadataRecord(metadata)

    const houseUnitDisplayName = getString(metadata, 'houseUnitDisplayName')
    const reasonDisplayName = getString(metadata, 'reasonDisplayName')
    const endDate = formatDate(metadataRecord?.endDate)

    const reason = reasonDisplayName ?? 'Temporary placement'

    if (houseUnitDisplayName && endDate) {
        return `${reason} at ${houseUnitDisplayName} ended on ${endDate}`
    }

    if (houseUnitDisplayName) {
        return `${reason} at ${houseUnitDisplayName} ended`
    }

    if (endDate) {
        return `${reason} ended on ${endDate}`
    }

    return 'Temporary placement was ended'
}

export function formatActivitySubtitle(activity: EmployeeActivityDTO): string | null {
    const action = activity.action as AuditAction | undefined
    const metadata = activity.metadata

    if (!action || !metadata) return null

    switch (action) {
        case 'EMPLOYEE_CREATED':
            return 'Employee profile was created'

        case 'EMPLOYEE_UPDATED':
            return formatEmployeeUpdated(metadata)

        case 'EMPLOYEE_TERMINATED':
            return 'Employment was terminated'

        case 'EMPLOYEE_REACTIVATED':
            return 'Employee was marked as active again'

        case 'ASSIGNMENT_CREATED':
            return formatAssignmentCreated(metadata, activity.occurredAt)

        case 'ASSIGNMENT_TERMINATED':
            return formatAssignmentTerminated(metadata)

        case 'ASSIGNMENT_CANCELLED':
            return formatAssignmentCancelled(metadata)

        case 'PLACEMENT_CREATED':
            return formatPlacementCreated(metadata, activity.occurredAt)

        case 'PLACEMENT_TERMINATED':
            return formatPlacementTerminated(metadata)

        default:
            return null
    }
}