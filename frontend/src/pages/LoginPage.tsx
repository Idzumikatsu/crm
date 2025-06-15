import { useState, type FormEvent } from 'react';
import { Link, useNavigate, useLocation, type Location } from 'react-router-dom';
import { useAuth } from '../auth';
import { useApiFetch } from '../api';

export const LoginPage = () => {
  const { login } = useAuth();
  const apiFetch = useApiFetch();
  const navigate = useNavigate();
  const location = useLocation();
  const [username, setUsername] = useState('');
  const [password, setPassword] = useState('');
  const [code, setCode] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    const res = await apiFetch('/api/auth/login', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username, password, code }),
    });
    if (res.ok) {
      const data: { token: string; username: string; role: string } = await res.json();
      await login(data.token);
      const redirect = (location.state as { from?: Location })?.from?.pathname || '/';
      navigate(redirect, { replace: true });
    } else {
      setError('Invalid credentials');
    }
  };

  return (
    <div className="flex items-center justify-center h-screen">
      <form onSubmit={handleSubmit} className="space-y-2 p-4 border rounded">
        <input
          className="border p-1 block w-full"
          placeholder="Username"
          value={username}
          onChange={(e) => setUsername(e.target.value)}
        />
        <input
          type="password"
          className="border p-1 block w-full"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          className="border p-1 block w-full"
          placeholder="TOTP Code"
          value={code}
          onChange={(e) => setCode(e.target.value)}
        />
        {error && <div className="text-red-600">{error}</div>}
        <button type="submit" className="bg-blue-500 text-white px-2 py-1">
          Login
        </button>
        <div>
          <Link to="/register" className="text-blue-600 underline">
            Register
          </Link>
        </div>
      </form>
    </div>
  );
};
