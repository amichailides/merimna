import { useEffect } from 'react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { RequireAuth } from './auth/RequireAuth'
import { RoleBasedRedirect } from './auth/RoleBasedRedirect'
import { LoginPage } from './pages/LoginPage'
import { useAuthStore } from './stores/authStore'
import { refreshAccessToken } from './api/authApi'
import { decodeUserFromToken } from './auth/decodeUserFromToken'

function App() {
  useEffect(() => {
    async function initializeAuth() {
      try {
        const response = await refreshAccessToken()
        const user = decodeUserFromToken(response.accessToken)

        useAuthStore.getState().setAuth(response.accessToken, user)
      } catch {
        useAuthStore.getState().clearAuth()
      } finally {
        useAuthStore.getState().setAuthLoading(false)
      }
    }

    initializeAuth()
  }, [])

  return (
    <BrowserRouter>
      <Routes>
        <Route path="/login" element={<LoginPage />} />

        <Route element={<RequireAuth />}>
          <Route path="/" element={<RoleBasedRedirect />} />
          <Route path="/dashboard" element={<div>Staff dashboard</div>} />
          <Route path="/admin/dashboard" element={<div>Admin dashboard</div>} />
        </Route>
      </Routes>
    </BrowserRouter>
  )
}

export default App