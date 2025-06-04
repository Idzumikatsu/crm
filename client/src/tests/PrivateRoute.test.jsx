import { render, screen } from '@testing-library/react';
import { MemoryRouter, Routes, Route } from 'react-router-dom';
import PrivateRoute from '../components/PrivateRoute';

function renderWithRoutes() {
  return render(
    <MemoryRouter initialEntries={[ '/dashboard' ]}>
      <Routes>
        <Route path="/login" element={<div>login</div>} />
        <Route path="/dashboard" element={<PrivateRoute><div>dash</div></PrivateRoute>} />
      </Routes>
    </MemoryRouter>
  );
}

test('redirects when no token', () => {
  localStorage.removeItem('token');
  renderWithRoutes();
  expect(screen.getByText('login')).toBeInTheDocument();
});
