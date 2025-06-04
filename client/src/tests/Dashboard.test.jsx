import { render, screen, waitFor } from '@testing-library/react';
import Dashboard from '../pages/Dashboard';

it('fetches protected endpoint and displays response', async () => {
  global.fetch = jest.fn(() => Promise.resolve({
    json: () => Promise.resolve({ message: 'You are authenticated', user: { userId: '1', role: 'ADMIN' } })
  }));
  localStorage.setItem('token', 'abc');
  render(<Dashboard />);
  await waitFor(() => screen.getByText(/You are authenticated as 1/));
});
