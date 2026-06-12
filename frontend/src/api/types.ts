import type { components } from './schema'

export type LoginRequest = components['schemas']['LoginRequest']
export type AuthResponse = components['schemas']['AuthResponse']

export type EmployeeListDTO = components['schemas']['EmployeeListDTO']
export type EmployeeDetailsDTO = components['schemas']['EmployeeDetailsDTO']
export type EmployeeSearchDTO = components['schemas']['EmployeeSearchDTO']
export type PageResponseEmployeeListDTO =
  components['schemas']['PageResponseEmployeeListDTO']
  