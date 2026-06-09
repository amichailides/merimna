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

  switch (user.role) {
    case 'ADMIN':
      return <Navigate to="/admin/dashboard" replace />

    case 'STAFF':
      return <Navigate to="/dashboard" replace />

    default:
      return <Navigate to="/dashboard" replace />
  }
}