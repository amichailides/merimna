import { axiosInstance } from './axiosInstance'
import type { EmployeePositionReadOnlyDTO } from './types'

export async function getPositions(): Promise<EmployeePositionReadOnlyDTO[]> {
    const response = await axiosInstance.get<EmployeePositionReadOnlyDTO[]>('/employee-positions')
    return response.data
}