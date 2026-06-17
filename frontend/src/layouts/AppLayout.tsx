import { NavLink, Outlet, useNavigate } from 'react-router-dom'
import { LogOut } from 'lucide-react'
import { useAuth } from '@/auth/useAuth'
import { cn } from '@/lib/utils'
import { ADMIN_NAV_GROUPS } from '@/navigation/adminNavGroups'
import { STAFF_NAV_GROUPS } from '@/navigation/staffNavGroups'
import type { NavGroup, NavItem } from '@/navigation/navTypes'
import { MerimnaLogo } from '@/components/brand/MerimnaLogo'

export function AppLayout() {
  const navigate = useNavigate()
  const { logout, user, hasPermission } = useAuth()

  async function handleLogout() {
    await logout()
    navigate('/login')
  }

  const rawGroups: NavGroup[] =
    user?.role === 'ADMIN'
      ? ADMIN_NAV_GROUPS
      : user?.role === 'STAFF'
        ? STAFF_NAV_GROUPS
        : []

  const navGroups = rawGroups
    .map((group) => ({
      ...group,
      items: group.items.filter((item) =>
        item.requiredPermission ? hasPermission(item.requiredPermission) : true
      ),
    }))
    .filter((group) => group.items.length > 0)

  const initials = user?.email
    ? user.email.substring(0, 2).toUpperCase()
    : '??'

  return (
    <div className="flex min-h-screen flex-col bg-white">

      {/* Topbar — full width */}
      <header className="sticky top-0 z-40 flex h-14 shrink-0 items-center justify-between bg-slate-100 px-4">
        <div className="flex w-56 shrink-0 items-center gap-2.5">
          <MerimnaLogo className="text-teal-600" />
          <div className="flex flex-col leading-none">
            <span style={{ fontFamily: "'Plus Jakarta Sans', sans-serif" }} className="text-[14px] font-semibold text-slate-950">
              Merimna
            </span>
            <span className="text-[10px] text-slate-400 mt-0.5">
              Care Management
            </span>
          </div>
        </div>

        <button
          onClick={handleLogout}
          className="flex items-center gap-2 rounded-md px-2 py-1 text-[13px] text-slate-600 transition-colors hover:bg-slate-50 hover:text-slate-950"
        >
          <div className="flex h-6 w-6 items-center justify-center rounded-full bg-teal-100 text-[10px] font-semibold text-teal-700">
            {initials}
          </div>
          <span className="font-medium">{user?.email ?? '—'}</span>
          <LogOut size={13} strokeWidth={1.75} className="ml-1 text-slate-400" />
        </button>
      </header>

      <div className="flex flex-1 bg-slate-100 relative">
        {/* Sidebar */}
        <aside className="sticky top-14 hidden h-[calc(100vh-3.5rem)] w-56 shrink-0 flex-col bg-slate-100 md:flex">
          <nav className="flex-1 overflow-y-auto px-3 pt-5 pb-3">
            {navGroups.map((group, idx) => (
              <div key={group.label} className={cn(idx > 0 && 'mt-5')}>
                <p className="mb-1 px-2 text-[11px] font-medium text-slate-400">
                  {group.label}
                </p>
                <div>
                  {group.items.map((item: NavItem) => (
                    <NavLink
                      key={item.to}
                      to={item.to}
                      className={({ isActive }) =>
                        cn(
                          'flex items-center gap-2 rounded-md px-2 py-[5px] text-[13px] transition-colors',
                          isActive
                            ? 'bg-slate-200 font-medium text-slate-950'
                            : 'text-slate-600 hover:bg-slate-100 hover:text-slate-950'
                        )
                      }
                    >
                      {item.icon && (
                        <item.icon
                          size={14}
                          strokeWidth={1.75}
                          className="shrink-0 opacity-70"
                        />
                      )}
                      {item.label}
                    </NavLink>
                  ))}
                </div>
              </div>
            ))}
          </nav>
        </aside>

        {/* Main */}
        <div className="flex-1 bg-white rounded-tl-lg overflow-hidden">
          <main className="px-6 py-5">
            <Outlet />
          </main>
        </div>
      </div>
    </div>
  )
}