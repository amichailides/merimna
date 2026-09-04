import { useEffect } from 'react'
import { BrowserRouter, Route, Routes } from 'react-router-dom'

import { TooltipProvider } from '@/components/ui/tooltip'
import { RequireAuth } from './auth/RequireAuth'
import { useAuth } from './auth/useAuth'
import { AppLayout } from '@/layouts/AppLayout'
import { LoginPage } from './pages/LoginPage'
import { DashboardPage } from './pages/DashboardPage'
import { EmployeesPage } from './pages/EmployeesPage'
import { EmployeeDetailsPage } from './pages/EmployeeDetailsPage'
import LandingPage from "@/pages/LandingPage";
import { EmployeeOnboardingPage } from './pages/EmployeeOnboardingPage'
import { AcceptInvitationPage } from './pages/AcceptInvitationPage'
import { ForgotPasswordPage } from './pages/ForgotPasswordPage'
import { ResetPasswordPage } from './pages/ResetPasswordPage'
import { BeneficiariesPage } from './pages/BeneficiariesPage'

function App() {
  const { initializeAuth } = useAuth()

  useEffect(() => {
    initializeAuth()
  }, [])

  return (
    <TooltipProvider delayDuration={150}>
      <BrowserRouter>
        <Routes>
          <Route path="/" element={<LandingPage />} />
          <Route path="/login" element={<LoginPage />} />
          <Route path="/accept-invitation" element={<AcceptInvitationPage />} />
          <Route path="/forgot-password" element={<ForgotPasswordPage />} />
          <Route path="/reset-password" element={<ResetPasswordPage />} />

          <Route element={<RequireAuth />}>
            <Route element={<AppLayout />}>
              <Route path="/dashboard" element={<DashboardPage />} />
              <Route path="/employees" element={<EmployeesPage />} />
              <Route
                path="/employees/:publicId"
                element={<EmployeeDetailsPage />}
              />
              <Route path="/employees/new" element={<EmployeeOnboardingPage />}/>
              <Route path="/beneficiaries" element={<BeneficiariesPage />} />
            </Route>
          </Route>
        </Routes>
      </BrowserRouter>
    </TooltipProvider>
  )
}

export default App