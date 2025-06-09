import { useAuth } from './auth';
import { useCallback } from 'react';

export function useApiFetch() {
  const { token } = useAuth();
  return useCallback(
    (path: string, options: RequestInit = {}) => {
      const headers = new Headers(options.headers || {});
      if (token) {
        headers.set('Authorization', `Bearer ${token}`);
      }
      const base = (import.meta.env.VITE_API_URL || '/').replace(/\/$/, '');
      const url = `${base}${path}`;
      return fetch(url, { ...options, headers });
    },
    [token]
  );
}
