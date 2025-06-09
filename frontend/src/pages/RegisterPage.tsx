import { useState, type FormEvent } from 'react';
import { Link } from 'react-router-dom';

export const RegisterPage = () => {
  const [email, setEmail] = useState('');
  const [password, setPassword] = useState('');
  const [confirm, setConfirm] = useState('');
  const [secret, setSecret] = useState('');
  const [message, setMessage] = useState('');
  const [error, setError] = useState('');

  const handleSubmit = async (e: FormEvent) => {
    e.preventDefault();
    setError('');
    setMessage('');
    if (!email || !password || !confirm) {
      setError('All fields are required');
      return;
    }
    if (password !== confirm) {
      setError('Passwords do not match');
      return;
    }
    const res = await fetch('/api/auth/register', {
      method: 'POST',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ username: email, password, role: 'STUDENT' }),
    });
    if (res.ok) {
      const data: { secret: string } = await res.json();
      setSecret(data.secret);
      setMessage('Check your email to verify the account.');
    } else {
      setError('Registration failed');
    }
  };

  return (
    <div className="flex items-center justify-center h-screen">
      <form onSubmit={handleSubmit} className="space-y-2 p-4 border rounded">
        <input
          className="border p-1 block w-full"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
        <input
          type="password"
          className="border p-1 block w-full"
          placeholder="Password"
          value={password}
          onChange={(e) => setPassword(e.target.value)}
        />
        <input
          type="password"
          className="border p-1 block w-full"
          placeholder="Confirm Password"
          value={confirm}
          onChange={(e) => setConfirm(e.target.value)}
        />
        {error && <div className="text-red-600">{error}</div>}
        {message && <div className="text-green-600">{message}</div>}
        {secret && (
          <div className="text-sm break-all border p-2 bg-gray-100 rounded">
            <p>Your 2FA secret:</p>
            <p className="font-mono">{secret}</p>
            <p>Add it to your authenticator app.</p>
          </div>
        )}
        <button type="submit" className="bg-blue-500 text-white px-2 py-1">
          Register
        </button>
        <div>
          <Link to="/login" className="text-blue-600 underline">
            Back to login
          </Link>
        </div>
      </form>
    </div>
  );
};
