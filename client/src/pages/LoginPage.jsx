import React from 'react';
import { useNavigate } from 'react-router-dom';
import LoginForm from '../components/LoginForm';

export default function LoginPage() {
  const navigate = useNavigate();
  const onSuccess = (token) => {
    localStorage.setItem('token', token);
    navigate('/dashboard');
  };

  return <LoginForm onSuccess={onSuccess} />;
}
