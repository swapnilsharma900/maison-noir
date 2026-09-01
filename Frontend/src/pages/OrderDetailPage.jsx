import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { ordersService } from '../services/orders';
import Container from '../components/ui/Container';
import Button from '../components/ui/Button';
import Badge from '../components/ui/Badge';
import Spinner from '../components/ui/Spinner';
import Breadcrumb from '../components/ui/Breadcrumb';
import { toast } from '../components/ui/Toast';
import { SITE } from '../config/site';
import { resolveProductImage } from '../utils/images';

const statusBadgeVariant = {
  PENDING: 'warning',
  CONFIRMED: 'gold',
  SHIPPED: 'gold',
  DELIVERED: 'success',
  CANCELLED: 'error',
  RETURNED: 'default',
};

const statusSteps = ['PENDING', 'CONFIRMED', 'SHIPPED', 'DELIVERED'];

export default function OrderDetailPage() {
  const { orderId } = useParams();
  const navigate = useNavigate();
  const [order, setOrder] = useState(null);
  const [loading, setLoading] = useState(true);
  const [cancelling, setCancelling] = useState(false);

  useEffect(() => {
    ordersService
      .getById(orderId)
      .then((data) => setOrder(data))
      .catch(() => setOrder(null))
      .finally(() => setLoading(false));
  }, [orderId]);

  const handleCancel = async () => {
    setCancelling(true);
    try {
      const updated = await ordersService.cancel(orderId);
      setOrder(updated);
      toast('Order cancelled', 'success');
    } catch (err) {
      toast(err.message || 'Failed to cancel order', 'error');
    } finally {
      setCancelling(false);
    }
  };

  if (loading) {
    return (
      <Container className="py-20 flex justify-center">
        <Spinner size="lg" />
      </Container>
    );
  }

  if (!order) {
    return (
      <Container className="py-20 text-center">
        <h2 className="text-2xl font-light text-[var(--color-text-heading)]">Order not found</h2>
      </Container>
    );
  }

  const currentStep = statusSteps.indexOf(order.orderStatus);
  const statusLabel = SITE.orderStatusLabels[order.orderStatus] || order.orderStatus;
  const badgeVariant = statusBadgeVariant[order.orderStatus] || 'default';
  const canCancel = !['SHIPPED', 'DELIVERED', 'CANCELLED', 'RETURNED'].includes(order.orderStatus);

  return (
    <Container className="py-8 lg:py-12">
      <Breadcrumb
        items={[
          { label: 'Home', href: '/' },
          { label: 'My Orders', href: '/account/orders' },
          { label: `Order #${order.id}`, href: '#' },
        ]}
        className="mb-8"
      />

      <div className="flex items-center justify-between mb-8">
        <div>
          <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight">
            Order #{order.id}
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] font-light mt-1">
            Placed on{' '}
            {new Date(order.placedAt).toLocaleDateString('en-IN', {
              day: 'numeric',
              month: 'long',
              year: 'numeric',
            })}
          </p>
        </div>
        <div className="flex items-center gap-3">
          <Badge variant={badgeVariant}>{statusLabel}</Badge>
          {canCancel && (
            <Button
              variant="outline"
              size="sm"
              onClick={handleCancel}
              disabled={cancelling}
            >
              {cancelling ? 'Cancelling...' : 'Cancel Order'}
            </Button>
          )}
        </div>
      </div>

      {/* Status tracker */}
      {order.orderStatus !== 'CANCELLED' && order.orderStatus !== 'RETURNED' && (
        <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-6 mb-8">
          <div className="flex items-center justify-between">
            {statusSteps.map((step, i) => (
              <div key={step} className="flex items-center flex-1 last:flex-none">
                <div className="flex flex-col items-center">
                  <div
                    className={`w-3 h-3 rounded-full ${
                      i <= currentStep
                        ? 'bg-[var(--color-gold)]'
                        : 'bg-[var(--color-noir-border)]'
                    }`}
                  />
                  <span
                    className={`text-xs mt-2 font-light ${
                      i <= currentStep
                        ? 'text-[var(--color-gold)]'
                        : 'text-[var(--color-text-muted)]'
                    }`}
                  >
                    {SITE.orderStatusLabels[step]}
                  </span>
                </div>
                {i < statusSteps.length - 1 && (
                  <div
                    className={`flex-1 h-px mx-2 ${
                      i < currentStep ? 'bg-[var(--color-gold)]' : 'bg-[var(--color-noir-border)]'
                    }`}
                  />
                )}
              </div>
            ))}
          </div>
        </div>
      )}

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Items */}
        <div className="lg:col-span-2">
          <h2 className="text-lg font-light text-[var(--color-text-heading)] mb-4">Items</h2>
          <div className="flex flex-col gap-3">
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
                    {item.variantLabel}: {item.variantLabel} &middot; Qty: {item.quantity}
                  </p>
                </div>
                <p className="text-sm font-medium text-[var(--color-text-heading)]">
                  ₹{Number(item.snapshotPrice * item.quantity).toLocaleString('en-IN')}
                </p>
              </div>
            ))}
          </div>
        </div>

        {/* Shipping & Summary */}
        <div className="space-y-6">
          <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-5">
            <h2 className="text-sm font-medium text-[var(--color-text-heading)] mb-3">
              Shipping Address
            </h2>
            <div className="text-sm font-light text-[var(--color-text-muted)] space-y-0.5">
              <p className="text-[var(--color-text-primary)]">{order.shipName}</p>
              <p>{order.shipFlat}</p>
              <p>{order.shipCity} - {order.shipPincode}</p>
            </div>
          </div>

          <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-5">
            <h2 className="text-sm font-medium text-[var(--color-text-heading)] mb-3">
              Summary
            </h2>
            <div className="flex justify-between text-sm font-light mb-2">
              <span className="text-[var(--color-text-muted)]">Payment Method</span>
              <span className="text-[var(--color-text-primary)]">{order.paymentMethod}</span>
            </div>
            <div className="flex justify-between text-sm font-light mb-2">
              <span className="text-[var(--color-text-muted)]">Payment Status</span>
              <span className="text-[var(--color-text-primary)]">
                {SITE.paymentStatusLabels[order.paymentStatus] || order.paymentStatus}
              </span>
            </div>
            <hr className="my-3 border-[var(--color-noir-border)]" />
            <div className="flex justify-between font-medium">
              <span className="text-[var(--color-text-heading)]">Total</span>
              <span className="text-[var(--color-gold)]">
                ₹{Number(order.total).toLocaleString('en-IN')}
              </span>
            </div>
          </div>
        </div>
      </div>
    </Container>
  );
}