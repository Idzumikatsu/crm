import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import LoginForm from '../components/LoginForm';

test('login form calls API', async () => {
  const onSuccess = jest.fn();
  global.fetch = jest.fn(() => Promise.resolve({ ok: true, json: () => Promise.resolve({ token: 'abc' }) }));
  render(<LoginForm onSuccess={onSuccess} />);
  await userEvent.type(screen.getByPlaceholderText(/email/i), 'a@a.com');
  await userEvent.type(screen.getByPlaceholderText(/password/i), 'pass');
  await userEvent.click(screen.getByRole('button', { name: /log in/i }));
  expect(fetch).toHaveBeenCalledWith('/api/auth/login', expect.any(Object));
});
