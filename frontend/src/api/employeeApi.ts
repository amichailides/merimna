import { axiosInstance } from './axiosInstance'

import type {
  EmployeeDetailsDTO,
  EmployeeSearchDTO,
  EmployeeUpdateDTO,
  PageResponseEmployeeListDTO,
  EmployeeTerminateDTO,
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

export async function updateEmployee(
  publicId: string,
  payload: EmployeeUpdateDTO
): Promise<EmployeeDetailsDTO> {
  const response = await axiosInstance.patch<EmployeeDetailsDTO>(
    `/employees/${publicId}`,
    payload
  )

  return response.data
}

export async function terminateEmployee(
  publicId: string,
  payload: EmployeeTerminateDTO
): Promise<EmployeeDetailsDTO> {
  const response = await axiosInstance.post<EmployeeDetailsDTO>(
    `/employees/${publicId}/terminate`,
    payload
  )

  return response.data
}

export async function reactivateEmployee(
  publicId: string
): Promise<EmployeeDetailsDTO> {
  const response = await axiosInstance.post<EmployeeDetailsDTO>(
    `/employees/${publicId}/reactivate`
  )

  return response.data
}