import { render, screen } from '@testing-library/react';
import userEvent from '@testing-library/user-event';
import LoginPage from '../pages/LoginPage';

it('renders login form', () => {
  render(<LoginPage />);
  expect(screen.getByPlaceholderText('username')).toBeInTheDocument();
});
