import { axiosInstance } from './axiosInstance'

import type {
  EmployeeDetailsDTO,
  EmployeeSearchDTO,
  PageResponseEmployeeListDTO,
} from './types'

export async function getEmployees(
  criteria?: EmployeeSearchDTO
): Promise<PageResponseEmployeeListDTO> {
  const response = await axiosInstance.get<PageResponseEmployeeListDTO>(
    '/employees',
    {
      params: criteria,
    }
  )

  return response.data
}

export async function getEmployeeByPublicId(
  publicId: string
): Promise<EmployeeDetailsDTO> {
  const response = await axiosInstance.get<EmployeeDetailsDTO>(
    `/employees/${publicId}`
  )

  return response.data
}