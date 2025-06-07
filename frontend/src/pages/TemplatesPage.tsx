import { useEffect, useState } from 'react';
import { Sidebar } from '../components/Sidebar';
import { TemplateDialog, Template } from '../components/TemplateDialog';

export const TemplatesPage = () => {
  const [templates, setTemplates] = useState<Template[]>([]);
  const [editing, setEditing] = useState<Template | null>(null);
  const [creating, setCreating] = useState(false);

  const load = () => {
    fetch('/api/templates')
      .then((r) => r.json())
      .then((data: Template[]) => setTemplates(data));
  };

  useEffect(load, []);

  const remove = (id: number) => {
    fetch(`/api/templates/${id}`, { method: 'DELETE' }).then(load);
  };

  return (
    <div className="flex">
      <Sidebar />
      <div className="flex-1 p-4">
        <div className="mb-2 text-right">
          <button className="border px-2" onClick={() => setCreating(true)}>
            New
          </button>
        </div>
        <table className="w-full border">
          <thead>
            <tr>
              <th className="border p-1 text-left">Code</th>
              <th className="border p-1 text-left">Lang</th>
              <th className="border p-1 text-left">Subject</th>
              <th className="border p-1">Actions</th>
            </tr>
          </thead>
          <tbody>
            {templates.map((t) => (
              <tr key={t.id} className="border-t">
                <td className="p-1">{t.code}</td>
                <td className="p-1">{t.lang}</td>
                <td className="p-1">{t.subject}</td>
                <td className="p-1 text-center space-x-2">
                  <button className="px-1 border" onClick={() => setEditing(t)}>
                    Edit
                  </button>
                  <button className="px-1 border" onClick={() => remove(t.id!)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {creating && (
          <TemplateDialog
            onClose={() => setCreating(false)}
            onSaved={() => {
              setCreating(false);
              load();
            }}
          />
        )}
        {editing && (
          <TemplateDialog
            template={editing}
            onClose={() => setEditing(null)}
            onSaved={() => {
              setEditing(null);
              load();
            }}
          />
        )}
      </div>
    </div>
  );
};
