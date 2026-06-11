import { PERMISSIONS } from "@/auth/permissions";
import type { NavGroup } from "./navTypes";

export const STAFF_NAV_GROUPS: NavGroup[] = [
  {
    label: "Overview",
    items: [{ label: "Dashboard", to: "/dashboard" }],
  },
  {
    label: "Care",
    items: [
      {
        label: "My Unit",
        to: "/my-unit",
        requiredPermission: PERMISSIONS.HOUSE_UNIT_READ,
      },
      {
        label: "Residents",
        to: "/residents",
        requiredPermission: PERMISSIONS.BENEFICIARY_READ,
      },
      {
        label: "Medication Plans",
        to: "/medication-plans",
        requiredPermission: PERMISSIONS.BENEFICIARY_READ,
      },
    ],
  },
];