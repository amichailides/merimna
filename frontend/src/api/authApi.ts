import { axiosInstance } from './axiosInstance'

export type LoginRequest = {
  email: string
  password: string
}

export type LoginResponse = {
  accessToken: string
}

export async function loginUser(credentials: LoginRequest): Promise<LoginResponse> {
  const response = await axiosInstance.post<LoginResponse>('/auth/login', credentials)

  return response.data
}