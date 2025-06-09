import { Outlet } from 'react-router-dom';
import { Sidebar } from './Sidebar';

export const Layout = () => (
  <div className="flex">
    <Sidebar />
    <div className="p-4 flex-1">
      <Outlet />
    </div>
  </div>
);
