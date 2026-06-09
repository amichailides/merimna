import { create } from 'zustand'
import type { AuthUser } from '../auth/authTypes'

type AuthState = {
  accessToken: string | null
  user: AuthUser | null
  isAuthLoading: boolean
  setAuth: (token: string, user: AuthUser) => void
  clearAuth: () => void
  setAuthLoading: (isLoading: boolean) => void
}

export const useAuthStore = create<AuthState>((set) => ({
  accessToken: null,
  user: null,
  isAuthLoading: true,

  setAuth: (token, user) => set({ accessToken: token, user }),
  clearAuth: () => set({ accessToken: null, user: null }),
  setAuthLoading: (isLoading) => set({ isAuthLoading: isLoading }),
}))