import { axiosInstance, refreshClient  } from './axiosInstance'

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

export async function logoutUser(): Promise<void> {
  await axiosInstance.post('/auth/logout')
}


export async function refreshAccessToken(): Promise<LoginResponse> {
  const response = await refreshClient.post<LoginResponse>('/auth/refresh')

  return response.data
}