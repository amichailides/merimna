import type { components } from './schema'

export type LoginRequest = components['schemas']['LoginRequest']
export type AuthResponse = components['schemas']['AuthResponse']

export type EmployeeListDTO = components['schemas']['EmployeeListDTO']
export type EmployeeDetailsDTO = components['schemas']['EmployeeDetailsDTO']
export type EmployeeSearchDTO = components['schemas']['EmployeeSearchDTO']
export type EmployeeActivityDTO = components['schemas']['EmployeeActivityDTO']

export type PageResponseEmployeeListDTO =
  components['schemas']['PageResponseEmployeeListDTO']

export type PageResponseEmployeeActivityDTO =
  components['schemas']['PageResponseEmployeeActivityDTO']

export type EmployeePositionReadOnlyDTO =
  components['schemas']['EmployeePositionReadOnlyDTO']

export type HouseUnitReadOnlyDTO =
  components['schemas']['HouseUnitReadOnlyDTO']

export type EmployeePlacementReadOnlyDTO =
  components['schemas']['EmployeePlacementReadOnlyDTO']

export type EmployeePlacementSearchDTO =
  components['schemas']['EmployeePlacementSearchDTO']

export type PageResponseEmployeePlacementReadOnlyDTO =
  components['schemas']['PageResponseEmployeePlacementReadOnlyDTO']

export type EmployeeUpdateDTO = components['schemas']['EmployeeUpdateDTO']
