import { axiosInstance } from './axiosInstance'
import type { PageResponseEmployeeActivityDTO } from './types'

interface GetEmployeeActivityParams {
    page?: number
    size?: number
}

export async function getEmployeeActivity(
    employeePublicId: string,
    params: GetEmployeeActivityParams = {},
): Promise<PageResponseEmployeeActivityDTO> {
    const { data } = await axiosInstance.get<PageResponseEmployeeActivityDTO>(
        `/employees/${employeePublicId}/activity`,
        {
            params: {
                page: params.page ?? 0,
                size: params.size ?? 5,
            },
        },
    )

    return data
}