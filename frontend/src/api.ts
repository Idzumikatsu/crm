import { useCallback } from 'react';

export function useApiFetch() {
  return useCallback(
    async (path: string, options: RequestInit = {}) => {
      const headers = new Headers(options.headers || {});
      const base = (import.meta.env.VITE_API_URL || '/').replace(/\/$/, '');
      const url = `${base}${path}`;
      return fetch(url, { ...options, headers });
    },
    []
  );
}
