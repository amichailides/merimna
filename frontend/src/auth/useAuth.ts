import { loginUser, logoutUser, refreshAccessToken, type LoginRequest } from '../api/authApi'
import { decodeUserFromToken } from './decodeUserFromToken'
import { useAuthStore } from '../stores/authStore'


export function useAuth() {
  const accessToken = useAuthStore((state) => state.accessToken)
  const user = useAuthStore((state) => state.user)
  const setAuth = useAuthStore((state) => state.setAuth)
  const clearAuth = useAuthStore((state) => state.clearAuth)
  const isAuthLoading = useAuthStore((state) => state.isAuthLoading)
  const setAuthLoading = useAuthStore((state) => state.setAuthLoading)



  const login = async (credentials: LoginRequest) => {
    const response = await loginUser(credentials)
    const user = decodeUserFromToken(response.accessToken)

    setAuth(response.accessToken, user)
  }

  const logout = async () => {
    try {
      await logoutUser()
    } finally {
      clearAuth()
    }
  }

  const initializeAuth = async () => {
  try {
    const response = await refreshAccessToken()
    const user = decodeUserFromToken(response.accessToken)

    setAuth(response.accessToken, user)
  } catch {
    clearAuth()
  } finally {
    setAuthLoading(false)
  }
}

  return {
    accessToken,
    user,
    isAuthenticated: accessToken !== null && user !== null,
    login,
    logout,
    isAuthLoading,
    initializeAuth,
  }
}