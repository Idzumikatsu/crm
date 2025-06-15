import { HashRouter, Route, Routes } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { Layout } from './components/Layout';
import { ErrorBoundary } from './components/ErrorBoundary';
import { DashboardPage } from './pages/DashboardPage';
import { SettingsPage } from './pages/SettingsPage';
import { TeacherCalendarPage } from './pages/TeacherCalendarPage';
import { ManagerCalendarPage } from './pages/ManagerCalendarPage';
import { StudentsPage } from './pages/StudentsPage';
import { TemplatesPage } from './pages/TemplatesPage';
import { NotFoundPage } from './pages/NotFoundPage';

const queryClient = new QueryClient();

function App() {
  return (
    <ErrorBoundary>
      <HashRouter>
        <QueryClientProvider client={queryClient}>
            <Routes>
          <Route path="/" element={<Layout />}>
            <Route index element={<DashboardPage />} />
            <Route path="calendar" element={<TeacherCalendarPage />} />
            <Route path="manager" element={<ManagerCalendarPage />} />
            <Route path="students" element={<StudentsPage />} />
            <Route path="templates" element={<TemplatesPage />} />
            <Route path="settings" element={<SettingsPage />} />
          </Route>
          <Route path="*" element={<NotFoundPage />} />
        </Routes>
        </QueryClientProvider>
      </HashRouter>
    </ErrorBoundary>
  );
}

export default App;
