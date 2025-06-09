import { createContext, useContext, useEffect, useState, type ReactNode } from 'react';
import { useNavigate } from 'react-router-dom';

export interface UserInfo {
  id: string;
  username: string;
  role: string;
  twoFaEnabled?: boolean;
}

export interface AuthContextValue {
  token: string | null;
  user: UserInfo | null;
  login: (t: string) => void;
  logout: () => void;
  setUser: (u: UserInfo) => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [token, setToken] = useState<string | null>(() => localStorage.getItem('token'));
  const [user, setUser] = useState<UserInfo | null>(null);
  const navigate = useNavigate();

  const login = (t: string) => {
    localStorage.setItem('token', t);
    setToken(t);
    navigate('/');
  };

  const logout = () => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
    navigate('/login');
  };

  useEffect(() => {
    if (!token) {
      setUser(null);
      navigate('/login');
      return;
    }
    fetch('/api/users/me', { headers: { Authorization: `Bearer ${token}` } })
      .then((r) => (r.ok ? r.json() : Promise.reject()))
      .then((u: UserInfo) => setUser(u))
      .catch(() => setUser(null));
  }, [token, navigate]);

  const value: AuthContextValue = { token, user, login, logout, setUser };
  return <AuthContext.Provider value={value}>{children}</AuthContext.Provider>;
};

export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('AuthProvider missing');
  return ctx;
};
