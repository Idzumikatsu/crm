import { BrowserRouter, Route, Routes } from 'react-router-dom';
import { AuthProvider } from './auth';
import { AuthGate } from './components/AuthGate';
import { LoginPage } from './pages/LoginPage';
import { DashboardPage } from './pages/DashboardPage';
import { SettingsPage } from './pages/SettingsPage';
import { TeacherCalendarPage } from './pages/TeacherCalendarPage';

function App() {
  return (
    <BrowserRouter>
      <AuthProvider>
        <Routes>
          <Route path="/login" element={<LoginPage />} />
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
            path="/"
            element={
              <AuthGate>
                <DashboardPage />
              </AuthGate>
            }
          />
        </Routes>
      </AuthProvider>
    </BrowserRouter>
  );
}

export default App;
