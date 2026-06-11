import type { Permission } from "@/auth/permissions";

export type NavItem = {
  label: string;
  to: string;
  requiredPermission?: Permission;
};

export type NavGroup = {
  label: string;
  items: NavItem[];
};