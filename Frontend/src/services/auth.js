import { api } from './api';

export const authService = {
  login: (email, password) => {
    console.log('📡 authService.login called with URL:', '/api/auth/login');
    api.post('/api/auth/login', { email, password })
  },
  register: (data) => api.post('/api/auth/register', data),
};