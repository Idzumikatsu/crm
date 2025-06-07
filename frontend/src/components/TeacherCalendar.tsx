import { useEffect, useState } from 'react';
import FullCalendar from '@fullcalendar/react';
import dayGridPlugin from '@fullcalendar/daygrid';
import timeGridPlugin from '@fullcalendar/timegrid';
import { type EventInput } from '@fullcalendar/core';

interface Lesson {
  id: string;
  dateTime: string;
  duration: number;
  group?: { name: string };
}

export const TeacherCalendar = () => {
  const [events, setEvents] = useState<EventInput[]>([]);

  useEffect(() => {
    fetch('/api/teacher/lessons?teacherId=1')
      .then((r) => r.json())
      .then((data: Lesson[]) => {
        setEvents(
          data.map((l) => ({
            id: String(l.id),
            title: l.group?.name ?? 'Lesson',
            start: l.dateTime,
            end: new Date(new Date(l.dateTime).getTime() + l.duration * 60000).toISOString(),
          }))
        );
      });
  }, []);

  return (
    <FullCalendar plugins={[dayGridPlugin, timeGridPlugin]} initialView="timeGridWeek" events={events} />
  );
};
