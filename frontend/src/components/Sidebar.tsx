import { NavLink } from 'react-router-dom';

const linkClass = ({ isActive }: { isActive: boolean }) =>
  isActive ? 'font-bold text-blue-500' : 'text-gray-700';

export const Sidebar = () => {
  return (
    <aside className="w-48 p-4 border-r h-screen flex flex-col">
      <nav className="flex-1 space-y-2">
        <NavLink className={linkClass} to="/">Dashboard</NavLink>
        <NavLink className={linkClass} to="/calendar">Calendar</NavLink>
        <NavLink className={linkClass} to="/manager">Timeline</NavLink>
        <NavLink className={linkClass} to="/students">Students</NavLink>
        <NavLink className={linkClass} to="/templates">Templates</NavLink>
        <NavLink className={linkClass} to="/settings">Settings</NavLink>
      </nav>
      
    </aside>
  );
};
