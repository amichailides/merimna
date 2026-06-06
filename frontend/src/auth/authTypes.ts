export type Role = 'ADMIN' | 'STAFF'

export type AuthUser = {
  publicId: string
  username: string
  role: Role
  employeePublicId?: string
}