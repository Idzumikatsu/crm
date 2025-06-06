import { useEffect, useState } from 'react';

export interface Student {
  id: number;
  name: string;
  email: string;
}

interface Props {
  value: number | null;
  onChange: (id: number | null) => void;
}

export const StudentCombobox = ({ value, onChange }: Props) => {
  const [students, setStudents] = useState<Student[]>([]);

  useEffect(() => {
    fetch('/api/students')
      .then((r) => r.json())
      .then((data: Student[]) => setStudents(data));
  }, []);

  return (
    <select
      className="border p-1"
      value={value ?? ''}
      onChange={(e) => onChange(e.target.value ? Number(e.target.value) : null)}
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
