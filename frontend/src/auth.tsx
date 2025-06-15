import { createContext, useContext, useState, type ReactNode } from 'react';

export interface UserInfo {
  id: string;
  username: string;
  role: string;
  twoFaEnabled?: boolean;
}

export interface AuthContextValue {
  user: UserInfo | null;
  login: (t: string) => void;
  logout: () => void;
  setUser: (u: UserInfo | null) => void;
}

const AuthContext = createContext<AuthContextValue | undefined>(undefined);

export const AuthLoader = () => null;

export const AuthProvider = ({ children }: { children: ReactNode }) => {
  const [user, setUser] = useState<UserInfo | null>(null);

  const login = () => {};
  const logout = () => {};

  const value: AuthContextValue = { user, login, logout, setUser };
  return (
    <AuthContext.Provider value={value}>
      <AuthLoader />
      {children}
    </AuthContext.Provider>
  );
};
// eslint-disable-next-line react-refresh/only-export-components
export const useAuth = () => {
  const ctx = useContext(AuthContext);
  if (!ctx) throw new Error('AuthProvider missing');
  return ctx;
};
