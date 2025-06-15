import { useEffect, useState } from 'react';
import { useApiFetch } from '../api';

export const SettingsPage = () => {
  const [buffer, setBuffer] = useState(0);
  const [template, setTemplate] = useState('');
  const apiFetch = useApiFetch();

  useEffect(() => {
    apiFetch('/api/settings')
      .then((r) => r.json())
      .then((d) => {
        setBuffer(d.bufferMin ?? 0);
        setTemplate(d.template ?? '');
      })
      .catch(() => {});
  }, [apiFetch]);

  const save = () => {
    apiFetch('/api/settings', {
      method: 'PUT',
      headers: { 'Content-Type': 'application/json' },
      body: JSON.stringify({ bufferMin: buffer, template }),
    });
  };

  return (
    <div className="space-y-4">
      <div>
        <label className="block">Teacher buffer (min)</label>
        <input
          type="number"
          className="border p-1"
          value={buffer}
          onChange={(e) => setBuffer(Number(e.target.value))}
        />
      </div>
      <div>
        <label className="block">Notification template</label>
        <textarea
          className="border p-1 w-full h-32"
          value={template}
          onChange={(e) => setTemplate(e.target.value)}
        />
      </div>
      <button className="border px-2" onClick={save}>
        Save
      </button>
    </div>
  );
};
