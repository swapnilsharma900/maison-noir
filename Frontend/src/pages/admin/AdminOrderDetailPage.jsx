import { useState, useEffect } from 'react';
import { useParams, Link } from 'react-router-dom';
import { ordersService } from '../../services/orders';
import Button from '../../components/ui/Button';
import Badge from '../../components/ui/Badge';
import Spinner from '../../components/ui/Spinner';
import Breadcrumb from '../../components/ui/Breadcrumb';
import Input from '../../components/ui/Input';
import { toast } from '../../components/ui/Toast';
import { SITE } from '../../config/site';
import { resolveProductImage } from '../../utils/images';

const ORDER_STATUSES = [
  'PENDING',
  'CONFIRMED',
  'SHIPPED',
  'DELIVERED',
  'CANCELLED',
  'RETURNED',
];

const statusBadgeVariant = {
  PENDING: 'warning',
  CONFIRMED: 'gold',
  SHIPPED: 'gold',
  DELIVERED: 'success',
  CANCELLED: 'error',
  RETURNED: 'default',
};

export default function AdminOrderDetailPage() {
  const { orderId } = useParams();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [newStatus, setNewStatus] = useState('');
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    ordersService
      .getById(orderId)
      .then((data) => {
        setOrder(data);
        setNewStatus(data.orderStatus);
      })
      .catch(() => setOrder(null))
      .finally(() => setLoading(false));
  }, [orderId]);

  const handleStatusUpdate = async () => {
    if (!newStatus || newStatus === order.orderStatus) return;
    setSaving(true);
    try {
      const updated = await ordersService.updateStatus(orderId, newStatus);
      setOrder(updated);
      setNewStatus(updated.orderStatus);
      toast('Order status updated', 'success');
    } catch (err) {
      toast(err.message || 'Failed to update status', 'error');
    } finally {
      setSaving(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center py-20">
        <Spinner size="lg" />
      </div>
    );
  }

  if (!order) {
    return (
      <div className="text-center py-20">
        <p className="text-[var(--color-text-muted)] mb-4">Order not found.</p>
        <Link to="/admin/orders" className="text-[var(--color-gold)] text-sm">
          Back to orders
        </Link>
      </div>
    );
  }

  const statusLabel = SITE.orderStatusLabels[order.orderStatus] || order.orderStatus;

  return (
    <div>
      <Breadcrumb
        items={[
          { label: 'Admin', href: '/admin' },
          { label: 'Orders', href: '/admin/orders' },
          { label: `#${order.id}`, href: '#' },
        ]}
        className="mb-6"
      />

      <div className="flex flex-wrap items-start justify-between gap-4 mb-8">
        <div>
          <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight">
            Order #{order.id}
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] mt-1">
            Customer: User #{order.userId} · Placed{' '}
            {new Date(order.placedAt).toLocaleString('en-IN')}
          </p>
        </div>
        <Badge variant={statusBadgeVariant[order.orderStatus] || 'default'}>
          {statusLabel}
        </Badge>
      </div>

      <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-5 mb-8 max-w-md">
        <h2 className="text-sm font-medium text-[var(--color-text-heading)] mb-3">
          Update status
        </h2>
        <div className="flex flex-wrap gap-3 items-end">
          <div className="flex-1 min-w-[180px]">
            <Input
              type="select"
              value={newStatus}
              onChange={(e) => setNewStatus(e.target.value)}
            >
              {ORDER_STATUSES.map((s) => (
                <option key={s} value={s}>
                  {SITE.orderStatusLabels[s]}
                </option>
              ))}
            </Input>
          </div>
          <Button
            size="sm"
            onClick={handleStatusUpdate}
            disabled={saving || newStatus === order.orderStatus}
          >
            {saving ? 'Saving...' : 'Save'}
          </Button>
        </div>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-3">
          <h2 className="text-lg font-light text-[var(--color-text-heading)]">Items</h2>
          {order.orderItems?.map((item) => (
            <div
              key={item.id}
              className="flex items-center gap-4 p-4 bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg"
            >
              <div className="w-14 h-18 flex-shrink-0 bg-[var(--color-noir-elevated)] rounded overflow-hidden">
                <img
                  src={resolveProductImage(item.snapshotImage)}
                  alt=""
                  className="w-full h-full object-cover"
                />
              </div>
              <div className="flex-1">
                <p className="font-medium text-sm text-[var(--color-text-heading)]">
                  {item.snapshotName}
                </p>
                <p className="text-xs text-[var(--color-text-muted)]">
                  Qty: {item.quantity}
                </p>
              </div>
              <p className="text-sm font-medium text-[var(--color-text-heading)]">
                ₹{Number(item.snapshotPrice * item.quantity).toLocaleString('en-IN')}
              </p>
            </div>
          ))}
        </div>

        <div className="space-y-6">
          <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-5">
            <h2 className="text-sm font-medium text-[var(--color-text-heading)] mb-3">
              Shipping
            </h2>
            <div className="text-sm font-light text-[var(--color-text-muted)] space-y-0.5">
              <p className="text-[var(--color-text-primary)]">{order.shipName}</p>
              <p>{order.shipFlat}</p>
              <p>
                {order.shipCity} - {order.shipPincode}
              </p>
            </div>
          </div>
          <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-5">
            <h2 className="text-sm font-medium text-[var(--color-text-heading)] mb-3">
              Summary
            </h2>
            <div className="flex justify-between text-sm font-light mb-2">
              <span className="text-[var(--color-text-muted)]">Payment</span>
              <span>{order.paymentMethod}</span>
            </div>
            <div className="flex justify-between font-medium pt-2 border-t border-[var(--color-noir-border)]">
              <span>Total</span>
              <span className="text-[var(--color-gold)]">
                ₹{Number(order.total).toLocaleString('en-IN')}
              </span>
            </div>
          </div>
        </div>
      </div>
    </div>
  );
}
