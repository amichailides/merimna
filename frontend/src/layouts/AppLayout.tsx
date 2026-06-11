import { NavLink, Outlet, useNavigate } from "react-router-dom";
import { Button, buttonVariants } from "@/components/ui/button";
import { useAuth } from "@/auth/useAuth";
import { cn } from "@/lib/utils";
import { ADMIN_NAV_GROUPS } from "@/navigation/adminNavGroups";
import { STAFF_NAV_GROUPS } from "@/navigation/staffNavGroups";
import type { NavGroup } from "@/navigation/navTypes";

export function AppLayout() {
  const navigate = useNavigate();
  const { logout, user, hasPermission } = useAuth();

  async function handleLogout() {
    await logout();
    navigate("/login");
  }

  const rawGroups: NavGroup[] =
    user?.role === "ADMIN"
      ? ADMIN_NAV_GROUPS
      : user?.role === "STAFF"
        ? STAFF_NAV_GROUPS
        : [];

  const navGroups = rawGroups
    .map((group) => ({
      ...group,
      items: group.items.filter((item) =>
        item.requiredPermission ? hasPermission(item.requiredPermission) : true
      ),
    }))
    .filter((group) => group.items.length > 0);

  return (
    <div className="min-h-screen bg-slate-50 flex flex-col">
      <header className="sticky top-0 z-40 border-b bg-white/80 backdrop-blur-md">
        <div className="flex h-16 items-center justify-between px-6">
          <div>
            <h1 className="text-lg font-semibold text-slate-900 tracking-tight">
              Merimna
            </h1>
            <p className="text-xs text-slate-500">
              Care unit management platform
            </p>
          </div>

          <Button variant="outline" size="sm" onClick={handleLogout}>
            Logout
          </Button>
        </div>
      </header>

      <div className="flex flex-1">
        <aside className="sticky top-16 h-[calc(100vh-4rem)] w-64 border-r bg-white p-4 hidden md:block">
          <nav className="space-y-4">
            {navGroups.map((group) => (
              <div key={group.label}>
                <p className="mb-1 px-3 text-xs font-semibold uppercase tracking-wider text-slate-400">
                  {group.label}
                </p>

                <div className="space-y-1">
                  {group.items.map((item) => (
                    <NavLink
                      key={item.to}
                      to={item.to}
                      className={({ isActive }) =>
                        cn(
                          buttonVariants({
                            variant: isActive ? "secondary" : "ghost",
                          }),
                          "w-full justify-start font-medium transition-colors",
                          isActive
                            ? "text-slate-900 font-semibold"
                            : "text-slate-600 hover:text-slate-900"
                        )
                      }
                    >
                      {item.label}
                    </NavLink>
                  ))}
                </div>
              </div>
            ))}
          </nav>
        </aside>

        <main className="flex-1 px-6 py-6">
          <Outlet />
        </main>
      </div>
    </div>
  );
}