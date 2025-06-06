import { Sidebar } from '../components/Sidebar';

export const DashboardPage = () => {
  return (
    <div className="flex">
      <Sidebar />
      <div className="p-4 flex-1">Welcome to dashboard!</div>
    </div>
  );
};
