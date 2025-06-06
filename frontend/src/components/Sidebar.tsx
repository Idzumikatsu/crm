import { NavLink } from 'react-router-dom';
import { useAuth } from '../auth';

const linkClass = ({ isActive }: { isActive: boolean }) =>
  isActive ? 'font-bold text-blue-500' : 'text-gray-700';

export const Sidebar = () => {
  const { logout } = useAuth();
  return (
    <aside className="w-48 p-4 border-r h-screen flex flex-col">
      <nav className="flex-1 space-y-2">
        <NavLink className={linkClass} to="/">Dashboard</NavLink>
        <NavLink className={linkClass} to="/calendar">Calendar</NavLink>
        <NavLink className={linkClass} to="/manager">Timeline</NavLink>
        <NavLink className={linkClass} to="/settings">Settings</NavLink>
      </nav>
      <button onClick={logout} className="text-red-600 mt-auto">Logout</button>
    </aside>
  );
};
