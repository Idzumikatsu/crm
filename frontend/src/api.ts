import { useAuth } from './auth';
import { useCallback } from 'react';

export function useApiFetch() {
  const { logout } = useAuth();
  return useCallback(
    async (path: string, options: RequestInit = {}) => {
      const headers = new Headers(options.headers || {});
      const base = (import.meta.env.VITE_API_URL || '/').replace(/\/$/, '');
      const url = `${base}${path}`;
      const res = await fetch(url, { ...options, headers });
      if (res.status === 401 || res.status === 403) {
        logout();
      }
      return res;
    },
    [logout]
  );
}
