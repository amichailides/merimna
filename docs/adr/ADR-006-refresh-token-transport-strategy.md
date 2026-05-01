# ADR-006: Hybrid Refresh Token Transport for Browser and Non-Browser Clients

## Status

Accepted

## Date

2026-05-01

---

## Context

The project uses JWT authentication with:

- short-lived **access tokens**
- opaque **refresh tokens**
- refresh tokens stored in the database only as hashes
- `/auth/refresh` endpoint for issuing new access tokens
- `/auth/logout` endpoint for revoking refresh tokens

The API may be used by different types of clients:

- browser frontend
- mobile app
- Postman / development clients
- other API consumers

For browser clients, storing refresh tokens in `localStorage` is avoided because JavaScript-accessible storage increases
the impact of XSS vulnerabilities.

A full Backend-for-Frontend (BFF) architecture was considered, but it would introduce additional complexity that is not
justified at the current stage of the project.

---

## Problem

Refresh tokens need to be transported in a way that is safer for browser clients, still convenient for mobile apps and
API clients, compatible with Postman/testing workflows, and not overly complex for the current project scope.

The main decision is whether the refresh token should be transported through the request/response body, through an
HttpOnly cookie, or through both.

---

## Considered Options

**1. Request body only**

- ✅ Simple
- ✅ Works well for mobile apps, Postman, and API consumers
- ❌ Browser frontend would need to store the refresh token manually
- ❌ Less ideal for browser security if stored in `localStorage`

**2. HttpOnly cookie only**

- ✅ Better browser security
- ✅ Refresh token is not accessible through JavaScript
- ❌ Less convenient for mobile apps and non-browser clients
- ❌ Makes API testing and generic API usage less flexible

**3. Hybrid transport** ✅ SELECTED

- Login returns the refresh token in the response body
- Login also sets the refresh token as an HttpOnly cookie
- Refresh/logout prefer the cookie when present
- Refresh/logout fall back to the request body when no cookie is present

**4. Backend-for-Frontend (BFF)**

- ✅ Strongest browser-oriented security model
- ✅ Refresh token never reaches the browser
- ❌ Requires additional backend/session layer
- ❌ Adds operational and architectural complexity
- ❌ Premature for the current project stage

---

## Decision

Merimna will use **hybrid refresh token transport**.

On login, the backend returns the refresh token in the response body and also sets it as an HttpOnly cookie.

The login response contains:

- body: `accessToken` + `refreshToken`
- cookie: `refresh_token=...; HttpOnly; Secure; SameSite=Strict`

On refresh and logout, the backend first tries to read the refresh token from the HttpOnly cookie. If no cookie is
present, it falls back to the request body.

In other words:

`refreshToken = cookie token if present, otherwise request body token`

Refresh tokens remain opaque and are stored in the database only as hashes.

The cookie path must match the public API path. Since the application uses `server.servlet.context-path=/api`, the
refresh token cookie path is `/api/auth`, not just `/auth`.

---

## Consequences

* ✅ Browser clients can use HttpOnly cookies for refresh token transport
* ✅ Refresh tokens do not need to be stored in `localStorage`
* ✅ Mobile apps can still use the refresh token from the response body
* ✅ Postman and external API consumers remain easy to support
* ✅ The API avoids full BFF complexity for now
* ✅ The design remains future-friendly

Cookie configuration must be handled carefully:

- `HttpOnly`
- `Secure`
- `SameSite`
- correct cookie path, e.g. `/api/auth`

Logout revokes the refresh token when available and clears the refresh token cookie.

Logout is treated as an idempotent cleanup action:

`missing/unknown refresh token → clear cookie and return 204`

Refresh remains strict:

`invalid / expired / revoked refresh token → reject request`

---

## Future

Refresh token rotation may be introduced later.

In that case:

- each refresh request may issue a new refresh token
- the previous refresh token may be revoked/replaced
- `replacedByTokenPublicId` can be used to track token replacement

A full BFF architecture may also be reconsidered if the project evolves into a browser-heavy production system with
stronger frontend security requirements.