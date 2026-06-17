import type { Permission } from "@/auth/permissions";
import type { LucideIcon } from "lucide-react";

export type NavItem = {
  label: string;
  to: string;
  icon?: LucideIcon;
  requiredPermission?: Permission;
};

export type NavGroup = {
  label: string;
  items: NavItem[];
};