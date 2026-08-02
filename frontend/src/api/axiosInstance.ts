import axios from 'axios'
import type { InternalAxiosRequestConfig } from 'axios'
import { useAuthStore } from '../stores/authStore'
import { decodeUserFromToken } from '../auth/decodeUserFromToken'

type RetryAxiosRequestConfig = InternalAxiosRequestConfig & {
  _retry?: boolean
}

export const axiosInstance = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  withCredentials: true,
})

export const publicClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  withCredentials: true,
})

export const refreshClient = axios.create({
  baseURL: import.meta.env.VITE_API_URL || 'http://localhost:8080/api',
  withCredentials: true,
})

let refreshPromise: Promise<string> | null = null

async function refreshAccessTokenOnce(): Promise<string> {
  if (!refreshPromise) {
    refreshPromise = refreshClient
      .post('/auth/refresh')
      .then((response) => {
        const newToken = response.data.accessToken
        const user = decodeUserFromToken(newToken)

        useAuthStore.getState().setAuth(newToken, user)

        return newToken
      })
      .finally(() => {
        refreshPromise = null
      })
  }

  return refreshPromise
}

axiosInstance.interceptors.request.use((config) => {
  const token = useAuthStore.getState().accessToken

  if (token) {
    config.headers.Authorization = `Bearer ${token}`
  }

  return config
})

axiosInstance.interceptors.response.use(
  (response) => response,
  async (error) => {
    const originalRequest =
      error.config as RetryAxiosRequestConfig | undefined

    if (
      error.response?.status === 401 &&
      originalRequest &&
      !originalRequest._retry
    ) {
      originalRequest._retry = true

      try {
        const newToken = await refreshAccessTokenOnce()

        originalRequest.headers.Authorization = `Bearer ${newToken}`

        return axiosInstance(originalRequest)
      } catch {
        useAuthStore.getState().clearAuth()
        window.location.href = '/login'

        return Promise.reject(error)
      }
    }

    return Promise.reject(error)
  },
)