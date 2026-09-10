import { axiosInstance } from './axiosInstance'

import type {
    MedicationCreateDTO,
    MedicationDiscontinueDTO,
    MedicationReadOnlyDTO,
    MedicationUpdateDTO,
} from './types'

export async function getMedications(
    beneficiaryPublicId: string,
    includeInactive = false
): Promise<MedicationReadOnlyDTO[]> {
    const response = await axiosInstance.get<MedicationReadOnlyDTO[]>(
        `/beneficiaries/${beneficiaryPublicId}/medications`,
        {
            params: {
                includeInactive,
            },
        }
    )

    return response.data
}

export async function getMedicationByPublicId(
    beneficiaryPublicId: string,
    medicationPublicId: string
): Promise<MedicationReadOnlyDTO> {
    const response = await axiosInstance.get<MedicationReadOnlyDTO>(
        `/beneficiaries/${beneficiaryPublicId}/medications/${medicationPublicId}`
    )

    return response.data
}

export async function addMedication(
    beneficiaryPublicId: string,
    payload: MedicationCreateDTO
): Promise<MedicationReadOnlyDTO> {
    const response = await axiosInstance.post<MedicationReadOnlyDTO>(
        `/beneficiaries/${beneficiaryPublicId}/medications`,
        payload
    )

    return response.data
}

export async function updateMedication(
    beneficiaryPublicId: string,
    medicationPublicId: string,
    payload: MedicationUpdateDTO
): Promise<MedicationReadOnlyDTO> {
    const response = await axiosInstance.patch<MedicationReadOnlyDTO>(
        `/beneficiaries/${beneficiaryPublicId}/medications/${medicationPublicId}`,
        payload
    )

    return response.data
}

export async function discontinueMedication(
    beneficiaryPublicId: string,
    medicationPublicId: string,
    payload: MedicationDiscontinueDTO
): Promise<MedicationReadOnlyDTO> {
    const response = await axiosInstance.post<MedicationReadOnlyDTO>(
        `/beneficiaries/${beneficiaryPublicId}/medications/${medicationPublicId}/discontinue`,
        payload
    )

    return response.data
}