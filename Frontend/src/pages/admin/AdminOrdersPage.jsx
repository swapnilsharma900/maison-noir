import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { ordersService } from '../../services/orders';
import Spinner from '../../components/ui/Spinner';
import Badge from '../../components/ui/Badge';
import { SITE } from '../../config/site';
import { toast } from '../../components/ui/Toast';

const STATUS_FILTERS = [
  { label: 'All', value: '' },
  { label: 'Pending', value: 'PENDING' },
  { label: 'Confirmed', value: 'CONFIRMED' },
  { label: 'Shipped', value: 'SHIPPED' },
  { label: 'Delivered', value: 'DELIVERED' },
  { label: 'Cancelled', value: 'CANCELLED' },
  { label: 'Returned', value: 'RETURNED' },
];

const statusBadgeVariant = {
  PENDING: 'warning',
  CONFIRMED: 'gold',
  SHIPPED: 'gold',
  DELIVERED: 'success',
  CANCELLED: 'error',
  RETURNED: 'default',
};

export default function AdminOrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [statusFilter, setStatusFilter] = useState('');

  useEffect(() => {
    setLoading(true);
    const fetcher = statusFilter
      ? ordersService.getByStatus(statusFilter)
      : ordersService.getAll();

    fetcher
      .then((data) => setOrders(Array.isArray(data) ? data : []))
      .catch((err) => {
        toast(err.message || 'Failed to load orders', 'error');
        setOrders([]);
      })
      .finally(() => setLoading(false));
  }, [statusFilter]);

  return (
    <div>
      <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight mb-6">
        Orders
      </h1>

      <div className="flex flex-wrap gap-2 mb-6">
        {STATUS_FILTERS.map((f) => (
          <button
            key={f.value || 'all'}
            type="button"
            onClick={() => setStatusFilter(f.value)}
            className={`px-3 py-1.5 rounded-full text-xs font-medium transition-colors cursor-pointer ${
              statusFilter === f.value
                ? 'bg-[var(--color-gold)]/20 text-[var(--color-gold)] border border-[var(--color-gold-dim)]'
                : 'bg-[var(--color-noir-elevated)] text-[var(--color-text-muted)] border border-[var(--color-noir-border)] hover:text-[var(--color-text-primary)]'
            }`}
          >
            {f.label}
          </button>
        ))}
      </div>

      {loading ? (
        <div className="flex justify-center py-16">
          <Spinner size="lg" />
        </div>
      ) : orders.length === 0 ? (
        <p className="text-sm text-[var(--color-text-muted)] font-light">No orders found.</p>
      ) : (
        <div className="overflow-x-auto border border-[var(--color-noir-border)] rounded-lg">
          <table className="w-full text-sm font-light">
            <thead>
              <tr className="border-b border-[var(--color-noir-border)] bg-[var(--color-noir-surface)]">
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Order</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Customer</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Total</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Status</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Placed</th>
                <th className="text-right p-3 text-[var(--color-text-muted)] font-medium" />
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr
                  key={order.id}
                  className="border-b border-[var(--color-noir-border)] last:border-0 hover:bg-[var(--color-noir-elevated)]/50"
                >
                  <td className="p-3 text-[var(--color-text-heading)]">#{order.id}</td>
                  <td className="p-3 text-[var(--color-text-muted)]">User #{order.userId}</td>
                  <td className="p-3 text-[var(--color-gold)]">
                    ₹{Number(order.total).toLocaleString('en-IN')}
                  </td>
                  <td className="p-3">
                    <Badge variant={statusBadgeVariant[order.orderStatus] || 'default'}>
                      {SITE.orderStatusLabels[order.orderStatus] || order.orderStatus}
                    </Badge>
                  </td>
                  <td className="p-3 text-[var(--color-text-muted)]">
                    {order.placedAt
                      ? new Date(order.placedAt).toLocaleDateString('en-IN')
                      : '—'}
                  </td>
                  <td className="p-3 text-right">
                    <Link
                      to={`/admin/orders/${order.id}`}
                      className="text-[var(--color-gold)] hover:underline text-sm"
                    >
                      View
                    </Link>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}
    </div>
  );
}
