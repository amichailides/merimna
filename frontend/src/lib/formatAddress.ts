type AddressLike = {
    street?: string | null
    streetNumber?: string | null
    city?: string | null
    zipCode?: string | null
}

export function formatAddress(address: AddressLike | null | undefined) {
    if (!address) return null
    const streetLine = [address.street, address.streetNumber].filter(Boolean).join(' ')
    const cityLine = [address.city, address.zipCode].filter(Boolean).join(' ')
    return [streetLine, cityLine].filter(Boolean).join(', ') || null
}