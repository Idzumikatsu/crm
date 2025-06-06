import { useEffect, useState } from 'react';
import { Student } from './StudentCombobox';

interface Props {
  student?: Student;
  onClose: () => void;
  onSaved: () => void;
}

export const StudentDialog = ({ student, onClose, onSaved }: Props) => {
  const [name, setName] = useState(student?.name ?? '');
  const [email, setEmail] = useState(student?.email ?? '');

  useEffect(() => {
    setName(student?.name ?? '');
    setEmail(student?.email ?? '');
  }, [student]);

  const save = () => {
    const body = JSON.stringify({ id: student?.id, name, email });
    fetch(`/api/students${student ? '/' + student.id : ''}`, {
      method: student ? 'PUT' : 'POST',
      headers: { 'Content-Type': 'application/json' },
      body,
    }).then(() => onSaved());
  };

  return (
    <dialog open className="p-4 rounded bg-white shadow-lg">
      <h2 className="text-lg font-bold mb-2">
        {student ? 'Edit student' : 'New student'}
      </h2>
      <div className="space-y-2">
        <input
          className="border p-1 block w-full"
          placeholder="Name"
          value={name}
          onChange={(e) => setName(e.target.value)}
        />
        <input
          className="border p-1 block w-full"
          placeholder="Email"
          value={email}
          onChange={(e) => setEmail(e.target.value)}
        />
      </div>
      <div className="mt-4 space-x-2">
        <button className="px-2 py-1 border" onClick={save}>
          Save
        </button>
        <button className="px-2 py-1 border" onClick={onClose}>
          Cancel
        </button>
      </div>
    </dialog>
  );
};
