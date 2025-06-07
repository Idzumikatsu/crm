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

## Production build

Run `npm run build` to generate static files in the `dist/` directory. These files can be served by NGINX or any static web server.
