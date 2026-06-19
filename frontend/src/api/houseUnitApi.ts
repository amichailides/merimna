import { axiosInstance } from './axiosInstance'
import type { HouseUnitReadOnlyDTO } from './types'

export async function getHouseUnits(): Promise<HouseUnitReadOnlyDTO[]> {
    const response = await axiosInstance.get<HouseUnitReadOnlyDTO[]>('/house-units')
    return response.data
}