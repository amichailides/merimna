import { axiosInstance } from './axiosInstance'
import type { PageResponseEmployeeActivityDTO } from './types'

export async function getEmployeeActivity(
    employeePublicId: string,
): Promise<PageResponseEmployeeActivityDTO> {
    const { data } = await axiosInstance.get<PageResponseEmployeeActivityDTO>(
        `/employees/${employeePublicId}/activity`,
        { params: { page: 0 } },
    )

    return data
}