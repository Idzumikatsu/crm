import {
  createContext,
  useContext,
  useEffect,
  useCallback,
  useState,
  type ReactNode,
} from 'react';

export interface UserInfo {
  id: string;
  username: string;
  role: string;
  twoFaEnabled?: boolean;
}

export interface AuthContextValue {
  user: UserInfo | null;
  token: string | null;
  login: (t: string) => Promise<void>;
  logout: () => void;
  setUser: (u: UserInfo | null) => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthLoader = () => null;

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<UserInfo | null>(null);
  const [token, setToken] = useState<string | null>(null);
  const [loaded, setLoaded] = useState(false);

  const fetchUser = useCallback(async (tok: string) => {
    try {
      const res = await fetch('/api/users/me', {
        headers: { Authorization: `Bearer ${tok}` },
      });
      if (res.ok) {
        const data: UserInfo = await res.json();
        setUser(data);
      } else {
        logout();
      }
    } catch {
      logout();
    }
  }, [logout]);

  useEffect(() => {
    const stored = localStorage.getItem('token');
    if (stored) {
      setToken(stored);
      fetchUser(stored).finally(() => setLoaded(true));
    } else {
      setLoaded(true);
    }
  }, [fetchUser]);

  const login = async (t: string) => {
    localStorage.setItem('token', t);
    setToken(t);
    await fetchUser(t);
  };

  const logout = useCallback(() => {
    localStorage.removeItem('token');
    setToken(null);
    setUser(null);
  }, []);

  const value: AuthContextValue = { user, token, login, logout, setUser };
  return (
    <AuthContext.Provider value={value}>
      {loaded ? children : <AuthLoader />}
    </AuthContext.Provider>
  );
};
// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('AuthProvider missing');
  return ctx;
};
