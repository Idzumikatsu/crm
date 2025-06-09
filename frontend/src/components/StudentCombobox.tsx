import { useEffect, useState } from 'react';
import { useApiFetch } from '../api';

export interface Student {
  id: string;
  name: string;
  email: string;
}

interface Props {
  value: string | null;
  onChange: (id: string | null) => void;
}

export const StudentCombobox = ({ value, onChange }: Props) => {
  const [students, setStudents] = useState<Student[]>([]);
  const apiFetch = useApiFetch();

  useEffect(() => {
    apiFetch('/api/students')
      .then((r) => r.json())
      .then((data: Student[]) => setStudents(data));
  }, [apiFetch]);

  return (
    <select
      className="border p-1"
      value={value ?? ''}
      onChange={(e) => onChange(e.target.value || null)}
    >
      <option value="">Select student</option>
      {students.map((s) => (
        <option key={s.id} value={s.id}>
          {s.name} ({s.email})
        </option>
      ))}
    </select>
  );
};
