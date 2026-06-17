import { Navigate } from 'react-router-dom'
import { useAuth } from './useAuth'

export function RoleBasedRedirect() {
  const { user, isAuthenticated, isAuthLoading } = useAuth()

  if (isAuthLoading) {
    return <div>Loading...</div>
  }

  if (!isAuthenticated || !user) {
    return <Navigate to="/login" replace />
  }

  return <Navigate to="/dashboard" replace />
}