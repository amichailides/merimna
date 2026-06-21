import { axiosInstance } from './axiosInstance'
import type { PageResponseEmployeeActivityDTO } from './types'

export async function getEmployeeActivity(
    employeePublicId: string,
    size = 5,
): Promise<PageResponseEmployeeActivityDTO> {
    const { data } = await axiosInstance.get<PageResponseEmployeeActivityDTO>(
        `/employees/${employeePublicId}/activity`,
        { params: { page: 0, size, sort: 'occurredAt,desc' } },
    )

    return data
}