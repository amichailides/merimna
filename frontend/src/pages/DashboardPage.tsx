import { useAuth } from '@/auth/useAuth'
import { AdminDashboard } from '@/components/dashboard/AdminDashboard'
import { StaffDashboard } from '@/components/dashboard/StaffDashboard'

export function DashboardPage() {
    const { user, isAuthLoading } = useAuth()

    if (isAuthLoading) {
        return <div>Loading...</div>
    }

    if (user?.role === 'ADMIN') {
        return <AdminDashboard />
    }

    return <StaffDashboard />
}