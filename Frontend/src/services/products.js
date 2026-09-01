import { api } from './api';

export const productsService = {
  getAll: () => api.get('/api/products'),
  getById: (id) => api.get(`/api/products/${id}`),
  getByCategory: (category) => api.get(`/api/products/category/${category}`),
  search: (name) => api.get(`/api/products/search?name=${encodeURIComponent(name)}`),
  create: (body) => api.post('/api/products', body),
  update: (id, body) => api.put(`/api/products/${id}`, body),
  remove: (id) => api.delete(`/api/products/${id}`),
};