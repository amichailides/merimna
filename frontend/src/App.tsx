import { useEffect } from 'react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'

import { TooltipProvider } from '@/components/ui/tooltip'
import { RequireAuth } from './auth/RequireAuth'
import { RoleBasedRedirect } from './auth/RoleBasedRedirect'
import { useAuth } from './auth/useAuth'
import { AppLayout } from '@/layouts/AppLayout'
import { LoginPage } from './pages/LoginPage'
import { DashboardPage } from './pages/DashboardPage'
import { EmployeesPage } from './pages/EmployeesPage'
import { EmployeeDetailsPage } from './pages/EmployeeDetailsPage'

function App() {
  const { initializeAuth } = useAuth()

  useEffect(() => {
    initializeAuth()
  }, [])

  return (
    <TooltipProvider delayDuration={150}>
      <BrowserRouter>
        <Routes>
          <Route path="/login" element={<LoginPage />} />

          <Route element={<RequireAuth />}>
            <Route element={<AppLayout />}>
              <Route path="/" element={<RoleBasedRedirect />} />
              <Route path="/dashboard" element={<DashboardPage />} />
              <Route path="/employees" element={<EmployeesPage />} />
              <Route
                path="/employees/:publicId"
                element={<EmployeeDetailsPage />}
              />
            </Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  )
}

export default App