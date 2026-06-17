import { useAuth } from '@/auth/useAuth'

export function DashboardPage() {
    const { user, isAuthLoading } = useAuth()

    if (isAuthLoading) {
        return <div>Loading...</div>
    }

    if (user?.role === 'ADMIN') {
        return <div>Admin dashboard</div>
    }

    return <div>Staff dashboard</div>
}