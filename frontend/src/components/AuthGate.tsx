import { Navigate } from 'react-router-dom';
import { useAuth } from '../auth';
import type { ReactElement } from 'react';

export const AuthGate = ({ children }: { children: ReactElement }) => {
  const { token } = useAuth();
  if (!token) return <Navigate to="/login" replace />;
  return children;
};
