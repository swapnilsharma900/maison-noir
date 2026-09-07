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
    console.log("\n\n\nENTERED ROLE IS: "+data.role);
    const auth = await login(data.email, data.password);
    console.log("\n\n\nauth.ROLE IS: "+auth.role);
    navigate(auth?.role === 'ADMIN' ? '/admin' : '/', { replace: true });
  };

  return <AuthForm mode="login" onSubmit={handleLogin} />;
}