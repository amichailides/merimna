import { jwtDecode } from 'jwt-decode'
import type { AuthUser, Role } from './authTypes'

type JwtPayload = {
  sub: string
  role: Role
  userPublicId: string
  employeePublicId?: string
  exp: number
  iat: number
}

export function decodeUserFromToken(token: string): AuthUser {
  const payload = jwtDecode<JwtPayload>(token)

  return {
    publicId: payload.userPublicId,
    email: payload.sub,
    role: payload.role,
    employeePublicId: payload.employeePublicId,
  }
}