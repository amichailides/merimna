import { axiosInstance } from './axiosInstance'

import type {
    BeneficiaryCreateDTO,
    BeneficiaryDetailsDTO,
    BeneficiaryListDTO,
    BeneficiarySearchDTO,
    BeneficiaryUpdateDTO,
    DischargeRequestDTO,
    PageResponseBeneficiaryListDTO,
} from './types'

type BeneficiaryPageParams = {
    page?: number
    size?: number
    sort?: string
}

export async function getBeneficiaries(
    criteria?: BeneficiarySearchDTO,
    pageParams?: BeneficiaryPageParams
): Promise<PageResponseBeneficiaryListDTO> {
    const response = await axiosInstance.get<PageResponseBeneficiaryListDTO>(
        '/beneficiaries',
        { params: { ...criteria, ...pageParams } }
    )

    return response.data
}

export async function getBeneficiaryByPublicId(
    publicId: string
): Promise<BeneficiaryDetailsDTO> {
    const response = await axiosInstance.get<BeneficiaryDetailsDTO>(
        `/beneficiaries/${publicId}`
    )

    return response.data
}

export async function createBeneficiary(
    payload: BeneficiaryCreateDTO
): Promise<BeneficiaryDetailsDTO> {
    const response = await axiosInstance.post<BeneficiaryDetailsDTO>(
        '/beneficiaries',
        payload
    )

    return response.data
}

export async function updateBeneficiary(
    publicId: string,
    payload: BeneficiaryUpdateDTO
): Promise<BeneficiaryDetailsDTO> {
    const response = await axiosInstance.patch<BeneficiaryDetailsDTO>(
        `/beneficiaries/${publicId}`,
        payload
    )

    return response.data
}

export async function dischargeBeneficiary(
    publicId: string,
    payload: DischargeRequestDTO
): Promise<BeneficiaryDetailsDTO> {
    const response = await axiosInstance.post<BeneficiaryDetailsDTO>(
        `/beneficiaries/${publicId}/discharge`,
        payload
    )

    return response.data
}

export async function changeBeneficiaryHouseUnit(
    beneficiaryPublicId: string,
    houseUnitPublicId: string
): Promise<BeneficiaryListDTO> {
    const response = await axiosInstance.patch<BeneficiaryListDTO>(
        `/beneficiaries/${beneficiaryPublicId}/house-unit/${houseUnitPublicId}`
    )

    return response.data
}
