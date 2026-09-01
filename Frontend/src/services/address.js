import { api } from './api';

export const addressService = {
  get: () => api.get('/api/user/address'),
  create: (data) => api.post('/api/user/address', data),
  update: (data) => api.put('/api/user/address', data),
};