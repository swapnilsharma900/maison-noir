import { api } from './api';

export const userService = {
  getProfile: () => api.get('/api/user/me'),
  updateProfile: (data) => api.put('/api/user/me', data),
  updatePassword: (data) => api.put('/api/user/me/password', data),
  deleteAccount: () => api.delete('/api/user/me'),
  getAll: () => api.get('/api/user'),
  getById: (userId) => api.get(`/api/user/${userId}`),
  deleteById: (userId) => api.delete(`/api/user/${userId}`),
};