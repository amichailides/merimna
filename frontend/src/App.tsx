import { BrowserRouter, Route, Routes } from 'react-router-dom'
import { RequireAuth } from './auth/RequireAuth'
import { RoleBasedRedirect } from './auth/RoleBasedRedirect'
import { LoginPage } from './pages/LoginPage'

function App() {
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