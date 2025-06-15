import type { ReactElement } from 'react';
import { Navigate, useLocation } from 'react-router-dom';
import { useAuth } from '../auth';

export const AuthGate = ({ children }: { children: ReactElement }) => {
  const { user, token } = useAuth();
  const location = useLocation();
  if (!token) return <Navigate to="/login" state={{ from: location }} replace />;
  if (!user) return null;
  return children;
};
