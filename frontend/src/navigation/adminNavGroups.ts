import { PERMISSIONS } from "@/auth/permissions";
import type { NavGroup } from "./navTypes";

export const ADMIN_NAV_GROUPS: NavGroup[] = [
  {
    label: "Overview",
    items: [{ label: "Dashboard", to: "/admin/dashboard" }],
  },
  {
    label: "People",
    items: [
      {
        label: "Employees",
        to: "/admin/employees",
        requiredPermission: PERMISSIONS.EMPLOYEE_READ,
      },
      {
        label: "Beneficiaries",
        to: "/admin/beneficiaries",
        requiredPermission: PERMISSIONS.BENEFICIARY_READ,
      },
    ],
  },
  {
    label: "Facilities",
    items: [
      {
        label: "House Units",
        to: "/admin/house-units",
        requiredPermission: PERMISSIONS.HOUSE_UNIT_READ,
      },
    ],
  },
  {
    label: "Staffing",
    items: [
      {
        label: "Assignments",
        to: "/admin/assignments",
        requiredPermission: PERMISSIONS.ASSIGNMENT_READ,
      },
      {
        label: "Placements",
        to: "/admin/placements",
        requiredPermission: PERMISSIONS.PLACEMENT_READ,
      },
    ],
  },
];