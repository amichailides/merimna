import { axiosInstance } from './axiosInstance'

import type {
    EmployeePlacementSearchDTO,
    PageResponseEmployeePlacementReadOnlyDTO,
} from './types'

type GetPlacementsParams = EmployeePlacementSearchDTO & {
    page?: number
    size?: number
    sort?: string
}

export async function getPlacements(params: GetPlacementsParams) {
    const response =
        await axiosInstance.get<PageResponseEmployeePlacementReadOnlyDTO>(
            '/placements',
            { params }
        )

    return response.data
}