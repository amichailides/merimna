import { loginUser, logoutUser, type LoginRequest } from '../api/authApi'
import { decodeUserFromToken } from './decodeUserFromToken'
import { useAuthStore } from '../stores/authStore'

export function useAuth() {
  const accessToken = useAuthStore((state) => state.accessToken)
  const user = useAuthStore((state) => state.user)
  const setAuth = useAuthStore((state) => state.setAuth)
  const clearAuth = useAuthStore((state) => state.clearAuth)

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

  return {
    accessToken,
    user,
    isAuthenticated: accessToken !== null && user !== null,
    login,
    logout,
  }
}