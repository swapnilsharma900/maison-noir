import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import AuthForm from './AuthForm';

export default function LoginPage() {
  const { login, isAuthenticated, isAdmin, loading } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (!loading && isAuthenticated) {
      navigate(isAdmin ? '/admin' : '/', { replace: true });
    }
  }, [loading, isAuthenticated, isAdmin, navigate]);

  const handleLogin = async (data) => {
    const auth = await login(data.email, data.password);
    navigate(auth.role === 'ADMIN' ? '/admin' : '/', { replace: true });
  };

  return <AuthForm mode="login" onSubmit={handleLogin} />;
}