import {
  LayoutDashboard,
  Users,
  HeartPulse,
  Building2,
  ClipboardList,
  ArrowLeftRight,
} from 'lucide-react'
import { PERMISSIONS } from '@/auth/permissions'
import type { NavGroup } from './navTypes'

export const ADMIN_NAV_GROUPS: NavGroup[] = [
  {
    label: 'Overview',
    items: [
      { label: 'Dashboard', to: '/dashboard', icon: LayoutDashboard },
    ],
  },
  {
    label: 'People',
    items: [
      {
        label: 'Employees',
        to: '/employees',
        icon: Users,
        requiredPermission: PERMISSIONS.EMPLOYEE_READ,
      },
      {
        label: 'Beneficiaries',
        to: '/beneficiaries',
        icon: HeartPulse,
        requiredPermission: PERMISSIONS.BENEFICIARY_READ,
      },
    ],
  },
  {
    label: 'Facilities',
    items: [
      {
        label: 'House Units',
        to: '/house-units',
        icon: Building2,
        requiredPermission: PERMISSIONS.HOUSE_UNIT_READ,
      },
    ],
  },
  {
    label: 'Staffing',
    items: [
      {
        label: 'Assignments',
        to: '/assignments',
        icon: ClipboardList,
        requiredPermission: PERMISSIONS.ASSIGNMENT_READ,
      },
      {
        label: 'Placements',
        to: '/placements',
        icon: ArrowLeftRight,
        requiredPermission: PERMISSIONS.PLACEMENT_READ,
      },
    ],
  },
]