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

Create a `.env` file from `.env.example` to override `VITE_API_URL` when building for production. This variable should contain the base URL of the backend. If not set, requests are sent relative to the current host.

## Production build

Run `npm run build` to generate static files in the `dist/` directory. These files can be served by NGINX or any static web server.

## Error boundary

Unexpected rendering errors are caught by a simple `<ErrorBoundary>` component.
`App` wraps all routes in this boundary by default. You can also wrap
individual parts of the UI with `<ErrorBoundary>` when needed.
