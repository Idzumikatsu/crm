import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { QueryClient, QueryClientProvider } from '@tanstack/react-query';
import { AuthProvider } from './auth';
import { AuthGate } from './components/AuthGate';
import { LoginPage } from './pages/LoginPage';
import { RegisterPage } from './pages/RegisterPage';
import { VerifyPage } from './pages/VerifyPage';
import { DashboardPage } from './pages/DashboardPage';
import { SettingsPage } from './pages/SettingsPage';
import { TeacherCalendarPage } from './pages/TeacherCalendarPage';
import { ManagerCalendarPage } from './pages/ManagerCalendarPage';
import { StudentsPage } from './pages/StudentsPage';
import { TemplatesPage } from './pages/TemplatesPage';

const queryClient = new QueryClient();

function App() {
  return (
    <BrowserRouter>
      <QueryClientProvider client={queryClient}>
        <AuthProvider>
          <Routes>
          <Route path="/login" element={<LoginPage />} />
          <Route path="/register" element={<RegisterPage />} />
          <Route path="/verify" element={<VerifyPage />} />
          <Route
            path="/settings"
            element={
              <AuthGate>
                <SettingsPage />
              </AuthGate>
            }
          />
          <Route
            path="/calendar"
            element={
              <AuthGate>
                <TeacherCalendarPage />
              </AuthGate>
            }
          />
          <Route
            path="/manager"
            element={
              <AuthGate>
                <ManagerCalendarPage />
              </AuthGate>
            }
          />
          <Route
            path="/students"
            element={
              <AuthGate>
                <StudentsPage />
              </AuthGate>
            }
          />
          <Route
            path="/templates"
            element={
              <AuthGate>
                <TemplatesPage />
              </AuthGate>
            }
          />
          <Route
            path="/"
            element={
              <AuthGate>
                <DashboardPage />
              </AuthGate>
            }
          />
        </Routes>
        </AuthProvider>
      </QueryClientProvider>
    </BrowserRouter>
  );
}

export default App;
