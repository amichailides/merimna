import { LayoutDashboard, Building2, HeartPulse } from "lucide-react";
import { PERMISSIONS } from "@/auth/permissions";
import type { NavGroup } from "./navTypes";

export const STAFF_NAV_GROUPS: NavGroup[] = [
  {
    label: "Overview",
    items: [
      { label: "Dashboard", to: "/dashboard", icon: LayoutDashboard },
    ],
  },
  {
    label: "Care",
    items: [
      {
        label: "My Unit",
        to: "/my-unit",
        icon: Building2,
        requiredPermission: PERMISSIONS.HOUSE_UNIT_READ,
      },
      {
        label: "Residents",
        to: "/residents",
        icon: HeartPulse,
        requiredPermission: PERMISSIONS.BENEFICIARY_READ,
      },
    ],
  },
];