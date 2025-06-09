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
      return fetch(path, { ...options, headers });
    },
    [token]
  );
}
