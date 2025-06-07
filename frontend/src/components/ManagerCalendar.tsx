import { useEffect, useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import { type EventInput } from '@fullcalendar/core';

interface Teacher {
  id: string;
  name: string;
}

interface Lesson {
  id: string;
  dateTime: string;
  duration: number;
  group?: { name: string };
}

export const ManagerCalendar = () => {
  const [teachers, setTeachers] = useState<Teacher[]>([]);
  const [teacherId, setTeacherId] = useState<string | null>(null);
  const [events, setEvents] = useState<EventInput[]>([]);
  const [modalLesson, setModalLesson] = useState<Lesson | null>(null);

  useEffect(() => {
    fetch('/api/teachers')
      .then((r) => r.json())
      .then((data: Teacher[]) => {
        setTeachers(data);
        if (data.length > 0) setTeacherId(String(data[0].id));
      });
  }, []);

  useEffect(() => {
    if (!teacherId) return;
    fetch(`/api/lessons?teacherId=${teacherId}`)
      .then((r) => r.json())
      .then((data: Lesson[]) =>
        setEvents(
          data.map((l) => ({
            id: String(l.id),
            title: l.group?.name ?? 'Lesson',
            start: l.dateTime,
            end: new Date(new Date(l.dateTime).getTime() + l.duration * 60000).toISOString(),
            extendedProps: l,
          }))
        )
      );
  }, [teacherId]);

  return (
    <div>
      <div className="mb-2">
        <label htmlFor="teacher" className="mr-2">Teacher:</label>
        <select
          id="teacher"
          value={teacherId ?? ''}
          onChange={(e) => setTeacherId(e.target.value)}
          className="border p-1"
        >
          {teachers.map((t) => (
            <option key={t.id} value={t.id}>{t.name}</option>
          ))}
        </select>
      </div>
      <FullCalendar
        plugins={[dayGridPlugin, timeGridPlugin]}
        initialView="timeGridWeek"
        events={events}
        eventClick={(info) => {
          setModalLesson(info.event.extendedProps as Lesson);
        }}
      />
      {modalLesson && (
        <dialog open className="p-4 rounded bg-white shadow-lg">
          <h2 className="text-lg font-bold mb-2">Lesson #{modalLesson.id}</h2>
          <p>Group: {modalLesson.group?.name ?? 'n/a'}</p>
          <p>Start: {new Date(modalLesson.dateTime).toLocaleString()}</p>
          <p>Duration: {modalLesson.duration} min</p>
          <button
            className="mt-2 px-2 py-1 border"
            onClick={() => setModalLesson(null)}
          >
            Close
          </button>
        </dialog>
      )}
    </div>
  );
};
