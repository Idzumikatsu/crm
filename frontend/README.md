# Frontend

Single Page Application built with **React 19** and **Vite**.
Requires Node 20 or 22. Run `npm install` (or `npm ci`) before any npm script such as `npm run build` or `npm run lint`.

## Build

```bash
cd frontend
npm install
npm run build
```

This command generates static files in the `dist/` directory. They can be served by NGINX or any other web server.

For local testing use `npm run dev`, which starts Vite in watch mode and proxies requests to the backend on port 8080.

The application now uses a shared `<Layout>` component for navigation. Old static HTML pages have been removed from the backend.

Before building, create `frontend/.env` and define `VITE_API_URL` as described in [docs/ENVIRONMENT.md](../docs/ENVIRONMENT.md). This variable sets the backend base URL. If not set, requests use a relative path.

## Error boundary

Unexpected rendering errors are caught by a simple `<ErrorBoundary>` component.
`App` wraps all routes in this boundary by default. You can also wrap
individual parts of the UI with `<ErrorBoundary>` when needed.
