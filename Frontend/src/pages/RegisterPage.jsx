import { useNavigate } from 'react-router-dom';
import { useAuth } from '../hooks/useAuth';
import AuthForm from './AuthForm';

export default function RegisterPage() {
  const { register } = useAuth();
  const navigate = useNavigate();

  const handleRegister = async (data) => {
    await register(data);
    navigate('/');
  };

  return <AuthForm mode="register" onSubmit={handleRegister} />;
}