import { api } from './api';

export const cartService = {
  get: () => api.get('/api/cart'),
  addItem: (variantId, quantity) => api.post('/api/cart/add', { variantId, quantity }),
  updateItem: (cartItemId, quantity) => api.put(`/api/cart/items/${cartItemId}`, { quantity }),
  removeItem: (cartItemId) => api.delete(`/api/cart/remove/${cartItemId}`),
  clear: () => api.delete('/api/cart/clear'),
};