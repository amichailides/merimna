import { axiosInstance, publicClient, refreshClient } from './axiosInstance'
import type {
  AcceptInvitationRequest,
  AuthResponse,
  LoginRequest,
  ForgotPasswordRequest,
  ResetPasswordRequest
} from './types'

export async function loginUser(
  credentials: LoginRequest,
): Promise<AuthResponse> {
  const response = await publicClient.post<AuthResponse>(
    '/auth/login',
    credentials,
  )

  return response.data
}

export async function logoutUser(): Promise<void> {
  await axiosInstance.post('/auth/logout')
}

export async function refreshAccessToken(): Promise<AuthResponse> {
  const response = await refreshClient.post<AuthResponse>('/auth/refresh')

  return response.data
}

export async function acceptInvitation(
  request: AcceptInvitationRequest,
): Promise<void> {
  await publicClient.post('/auth/accept-invitation', request)
}

export async function forgotPassword(
  request: ForgotPasswordRequest,
): Promise<void> {
  await publicClient.post('/auth/forgot-password', request)
}

export async function resetPassword(
  request: ResetPasswordRequest,
): Promise<void> {
  await publicClient.post('/auth/reset-password', request)
}
