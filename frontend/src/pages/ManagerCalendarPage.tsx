import { Sidebar } from '../components/Sidebar';
import { ManagerCalendar } from '../components/ManagerCalendar';

export const ManagerCalendarPage = () => (
  <div className="flex">
    <Sidebar />
    <div className="flex-1 p-4">
      <ManagerCalendar />
    </div>
  </div>
);
