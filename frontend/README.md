# Merimna Frontend

The React client for **Merimna**, a supported-living management platform.

This workspace currently focuses on employee administration, including employee search and filtering, profile management, assignments, temporary placements, lifecycle actions, and activity history.

## Tech Stack

* React 19
* TypeScript
* Vite
* React Router
* Tailwind CSS
* shadcn/ui and Radix UI
* Axios
* Zustand
* React Hook Form
* Zod
* OpenAPI-generated TypeScript types

## Project Structure

```text
src/
├── api/          API clients, generated types, and data hooks
├── auth/         Authentication, permissions, and protected routes
├── components/   Shared and domain-specific UI components
├── layouts/      Application layouts
├── lib/          Formatting and utility functions
├── navigation/   Admin and staff navigation definitions
├── pages/        Route-level pages
└── stores/       Global Zustand stores
```

## Local Development

### Requirements

* Node.js
* npm
* A running Merimna backend

Install dependencies:

```bash
npm install
```

Create a `.env` file inside the `frontend` directory:

```env
VITE_API_URL=http://localhost:8080/api
```

Start the development server:

```bash
npm run dev
```

The frontend will normally be available at:

```text
http://localhost:5173
```

## Available Scripts

* `npm run dev` — start the Vite development server
* `npm run build` — type-check the project and create a production build
* `npm run lint` — run ESLint across the frontend codebase
* `npm run preview` — preview the production build locally
* `npm run generate:api` — regenerate TypeScript types from the backend OpenAPI specification

## API Integration

API requests are handled through Axios and custom React hooks under:

```text
src/api/
```

Authentication state is managed with Zustand. Protected routes and navigation items are rendered according to the authenticated user's permissions.

Frontend API types are generated from the backend OpenAPI document:

```text
http://localhost:8080/api/v3/api-docs
```

To regenerate them, start the backend and run:

```bash
npm run generate:api
```

The generated schema is written to:

```text
src/api/schema.d.ts
```

Application-facing API types are re-exported from:

```text
src/api/types.ts
```

The generated schema file should not be edited manually.
