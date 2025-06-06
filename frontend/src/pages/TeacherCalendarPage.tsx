import { TeacherCalendar } from '../components/TeacherCalendar';
import { Sidebar } from '../components/Sidebar';

export const TeacherCalendarPage = () => (
  <div className="flex">
    <Sidebar />
    <div className="flex-1 p-4">
      <TeacherCalendar />
    </div>
  </div>
);
