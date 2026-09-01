import { useEffect } from 'react';
import { useNavigate } from 'react-router-dom';
import { useAuth } from './useAuth';
import { toast } from '../components/ui/Toast';

export function useAdminBrowseGuard() {
  const { isAdmin } = useAuth();
  const navigate = useNavigate();

  useEffect(() => {
    if (isAdmin) {
      toast('Checkout is not available in admin preview', 'error');
      navigate('/admin', { replace: true });
    }
  }, [isAdmin, navigate]);
}
