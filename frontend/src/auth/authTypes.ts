export type Role = 'ADMIN' | 'STAFF'

export type AuthUser = {
  publicId: string
  email: string
  role: Role
  employeePublicId?: string
}