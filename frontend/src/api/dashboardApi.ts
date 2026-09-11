import { axiosInstance } from './axiosInstance'

import type { AdminDashboardReadOnlyDTO } from './types'

export async function getAdminDashboard(): Promise<AdminDashboardReadOnlyDTO> {
    const response = await axiosInstance.get<AdminDashboardReadOnlyDTO>(
        '/dashboard/admin'
    )

    return response.data
}
