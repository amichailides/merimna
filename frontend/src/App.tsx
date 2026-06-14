import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { RequireAuth } from './auth/RequireAuth'
import { RoleBasedRedirect } from './auth/RoleBasedRedirect'
import { LoginPage } from './pages/LoginPage'
import { useEffect } from 'react'
import { useAuth } from './auth/useAuth'
import { AppLayout } from "@/layouts/AppLayout";
import { EmployeesPage } from './pages/EmployeesPage'
import { EmployeeDetailsPage } from './pages/EmployeeDetailsPage'

function App() {
  const { initializeAuth } = useAuth()

  useEffect(() => {
    initializeAuth()
  }, [])

  return (
  <BrowserRouter>
    <Routes>
      <Route path="/login" element={<LoginPage />} />

      <Route element={<RequireAuth />}>
        <Route element={<AppLayout />}>
          <Route path="/" element={<RoleBasedRedirect />} />
          <Route path="/dashboard" element={<div>Staff dashboard</div>} />
          <Route path="/admin/dashboard" element={<div>Admin dashboard</div>} />
          <Route path='/admin/employees' element={<EmployeesPage />} />
          <Route path='/admin/employees/:publicId' element={<EmployeeDetailsPage />} />
        </Route>
      </Route>
    </Routes>
  </BrowserRouter>
);
}

export default App