export type Role = 'ADMIN' | 'STAFF'

export type AuthUser = {
  userPublicId: string
  email: string
  role: Role
  employeePublicId?: string
  permissions: string[]
}