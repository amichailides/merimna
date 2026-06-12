import { axiosInstance } from './axiosInstance'

import type {
  EmployeeDetailsDTO,
  EmployeeSearchDTO,
  PageResponseEmployeeListDTO,
} from './types'

type EmployeePageParams = {
  page?: number
  size?: number
  sort?: string
}

export async function getEmployees(
  criteria?: EmployeeSearchDTO,
  pageParams?: EmployeePageParams
): Promise<PageResponseEmployeeListDTO> {
  const response = await axiosInstance.get<PageResponseEmployeeListDTO>(
    '/employees',
    {
      params: {
        ...criteria,
        ...pageParams
      },   
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