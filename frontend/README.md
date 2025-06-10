# Frontend

Single Page Application built with **React 19** and **Vite**.
Requires Node 20 or 22. Run `npm install` (or `npm ci`) before any npm script (`npm run build`, `npm run lint`, `npm run dev`).

## Development

```bash
cd frontend
npm install
npm run dev
```

The dev server listens on `http://localhost:5173` and proxies API requests to the backend on port 8080.
`npm run dev` also starts Tailwind in watch mode so styles update instantly during development.

The application now uses a shared `<Layout>` component for navigation. Old static HTML pages have been removed from the backend.

Before a production build, create `frontend/.env` and define `VITE_API_URL` as described in [docs/ENVIRONMENT.md](../docs/ENVIRONMENT.md). This variable sets the backend base URL. If not set, requests use a relative path.

## Production build

Run `npm run build` to generate static files in the `dist/` directory. These files can be served by NGINX or any static web server.

## Error boundary

Unexpected rendering errors are caught by a simple `<ErrorBoundary>` component.
`App` wraps all routes in this boundary by default. You can also wrap
individual parts of the UI with `<ErrorBoundary>` when needed.
