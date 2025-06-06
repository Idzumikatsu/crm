import { useState, type FormEvent } from 'react';
import { useAuth } from '../auth';

export const LoginPage = () => {
  const { login } = useAuth();
  const [value, setValue] = useState('');

  const handleSubmit = (e: FormEvent) => {
    e.preventDefault();
    login(value || 'demo-token');
  };

  return (
    <div className="flex items-center justify-center h-screen">
      <form onSubmit={handleSubmit} className="space-y-2 p-4 border rounded">
        <input
          className="border p-1"
          placeholder="Token"
          value={value}
          onChange={(e) => setValue(e.target.value)}
        />
        <button type="submit" className="bg-blue-500 text-white px-2 py-1">
          Login
        </button>
      </form>
    </div>
  );
};
