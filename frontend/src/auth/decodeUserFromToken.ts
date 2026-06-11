import { jwtDecode } from 'jwt-decode'
import type { AuthUser, Role } from './authTypes'

type JwtPayload = {
  sub: string
  role: Role
  userPublicId: string
  employeePublicId?: string
  permissions?: string[]
  exp: number
  iat: number
}

export function decodeUserFromToken(token: string): AuthUser {
  const payload = jwtDecode<JwtPayload>(token)

  return {
    userPublicId: payload.userPublicId,
    email: payload.sub,
    role: payload.role,
    employeePublicId: payload.employeePublicId,
    permissions: payload.permissions ?? [],
  }
}