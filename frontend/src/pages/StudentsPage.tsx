import { useEffect, useState } from 'react';
import { Sidebar } from '../components/Sidebar';
import type { Student } from '../components/StudentCombobox';
import { StudentDialog } from '../components/StudentDialog';
import { useApiFetch } from '../api';

export const StudentsPage = () => {
  const [students, setStudents] = useState<Student[]>([]);
  const [search, setSearch] = useState('');
  const [editing, setEditing] = useState<Student | null>(null);
  const [creating, setCreating] = useState(false);
  const apiFetch = useApiFetch();

  const load = () => {
    apiFetch('/api/students')
      .then((r) => r.json())
      .then((data: Student[]) => setStudents(data));
  };

  useEffect(load, [apiFetch]);

  const filtered = students.filter(
    (s) =>
      s.name.toLowerCase().includes(search.toLowerCase()) ||
      s.email.toLowerCase().includes(search.toLowerCase())
  );

  const remove = (id: string) => {
    apiFetch(`/api/students/${id}`, { method: 'DELETE' }).then(load);
  };

  return (
    <div className="flex">
      <Sidebar />
      <div className="flex-1 p-4">
        <div className="flex justify-between mb-2">
          <input
            className="border p-1 flex-1 mr-2"
            placeholder="Search"
            value={search}
            onChange={(e) => setSearch(e.target.value)}
          />
          <button className="border px-2" onClick={() => setCreating(true)}>
            New
          </button>
        </div>
        <table className="w-full border">
          <thead>
            <tr>
              <th className="border p-1 text-left">Name</th>
              <th className="border p-1 text-left">Email</th>
              <th className="border p-1">Actions</th>
            </tr>
          </thead>
          <tbody>
            {filtered.map((s) => (
              <tr key={s.id} className="border-t">
                <td className="p-1">{s.name}</td>
                <td className="p-1">{s.email}</td>
                <td className="p-1 text-center space-x-2">
                  <button
                    className="px-1 border"
                    onClick={() => setEditing(s)}
                  >
                    Edit
                  </button>
                  <button className="px-1 border" onClick={() => remove(s.id)}>
                    Delete
                  </button>
                </td>
              </tr>
            ))}
          </tbody>
        </table>
        {creating && (
          <StudentDialog
            onClose={() => setCreating(false)}
            onSaved={() => {
              setCreating(false);
              load();
            }}
          />
        )}
        {editing && (
          <StudentDialog
            student={editing}
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
