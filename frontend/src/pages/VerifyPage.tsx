import { useEffect, useState } from 'react';
import { Link, useSearchParams } from 'react-router-dom';
import { useApiFetch } from '../api';

export const VerifyPage = () => {
  const [searchParams] = useSearchParams();
  const [status, setStatus] = useState<'pending' | 'success' | 'error'>('pending');
  const apiFetch = useApiFetch();

  useEffect(() => {
    const token = searchParams.get('token');
    if (!token) {
      setStatus('error');
      return;
    }
    apiFetch(`/api/auth/verify?token=${token}`)
      .then((r) => (r.ok ? setStatus('success') : setStatus('error')))
      .catch(() => setStatus('error'));
  }, [searchParams, apiFetch]);

  if (status === 'pending') return <div className="p-4">Verifying...</div>;
  if (status === 'success')
    return (
      <div className="p-4">
        Account verified. You can now{' '}
        <Link to="/login" className="text-blue-600 underline">
          log in
        </Link>
        .
      </div>
    );
  return <div className="p-4 text-red-600">Verification failed.</div>;
};
