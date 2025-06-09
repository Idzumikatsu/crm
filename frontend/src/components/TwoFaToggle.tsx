import { useAuth } from '../auth';
import { useEffect, useState } from 'react';
import { useApiFetch } from '../api';

export const TwoFaToggle = () => {
  const { token, user, setUser } = useAuth();
  const [enabled, setEnabled] = useState(false);
  const [secret, setSecret] = useState<string | null>(null);
  const apiFetch = useApiFetch();

  useEffect(() => {
    setEnabled(user?.twoFaEnabled ?? false);
  }, [user]);

  const toggle = async () => {
    if (!token) return;
    if (enabled) {
      await apiFetch('/api/users/me/2fa/disable', {
        method: 'POST',
      });
      setEnabled(false);
      setSecret(null);
      if (user) setUser({ ...user, twoFaEnabled: false });
    } else {
      const res = await apiFetch('/api/users/me/2fa/enable', {
        method: 'POST',
      });
      if (res.ok) {
        const data: { secret: string } = await res.json();
        setSecret(data.secret);
        setEnabled(true);
        if (user) setUser({ ...user, twoFaEnabled: true });
      }
    }
  };

  const qrUrl = secret
    ? `https://api.qrserver.com/v1/create-qr-code/?size=200x200&data=${encodeURIComponent(
        `otpauth://totp/ScheduleTracker:${user?.username}?secret=${secret}&issuer=ScheduleTracker`,
      )}`
    : null;

  return (
    <div className="space-y-2">
      <div className="flex items-center space-x-2">
        <span>Two-factor authentication:</span>
        <span className={enabled ? 'text-green-600' : 'text-red-600'}>
          {enabled ? 'Enabled' : 'Disabled'}
        </span>
        <button className="border px-2 py-1" onClick={toggle}>
          {enabled ? 'Disable' : 'Enable'}
        </button>
      </div>
      {secret && (
        <div className="space-y-2">
          <div className="text-sm break-all border p-2 bg-gray-100 rounded">
            Add this secret to your authenticator app:{' '}
            <span className="font-mono">{secret}</span>
          </div>
          {qrUrl && <img src={qrUrl} alt="QR Code" className="mx-auto" />}
        </div>
      )}
    </div>
  );
};
