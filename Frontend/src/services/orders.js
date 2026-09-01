import { api } from './api';

export const ordersService = {
  place: (paymentMethod) => api.post('/api/orders', { paymentMethod }),
  getMyOrders: () => api.get('/api/orders/my-orders'),
  getById: (id) => api.get(`/api/orders/${id}`),
  cancel: (id) => api.delete(`/api/orders/${id}/cancel`),
  getAll: () => api.get('/api/orders'),
  getByStatus: (status) => api.get(`/api/orders/status/${status}`),
  updateStatus: (id, orderStatus) => api.patch(`/api/orders/${id}/status`, { orderStatus }),
};