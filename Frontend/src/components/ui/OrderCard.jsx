import { Link } from 'react-router-dom';
import Badge from './Badge';
import { SITE } from '../../config/site';

const statusBadgeVariant = {
  PENDING: 'warning',
  CONFIRMED: 'gold',
  SHIPPED: 'gold',
  DELIVERED: 'success',
  CANCELLED: 'error',
  RETURNED: 'default',
};

export default function OrderCard({ order }) {
  const statusLabel = SITE.orderStatusLabels[order.orderStatus] || order.orderStatus;
  const badgeVariant = statusBadgeVariant[order.orderStatus] || 'default';

  return (
    <Link
      to={`/account/orders/${order.id}`}
      className="block bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-5 hover:border-[var(--color-gold-dim)] transition-all duration-300"
    >
      <div className="flex items-start justify-between mb-3">
        <div>
          <p className="text-sm font-medium text-[var(--color-text-heading)]">
            Order #{order.id}
          </p>
          <p className="text-xs text-[var(--color-text-muted)] mt-0.5">
            {new Date(order.placedAt).toLocaleDateString('en-IN', {
              day: 'numeric',
              month: 'short',
              year: 'numeric',
            })}
          </p>
        </div>
        <Badge variant={badgeVariant}>{statusLabel}</Badge>
      </div>

      <div className="flex items-center gap-2">
        {order.orderItems?.slice(0, 3).map((item) => (
          <div
            key={item.id}
            className="w-10 h-12 rounded bg-[var(--color-noir-elevated)] overflow-hidden flex-shrink-0"
          >
            <img
              src={item.snapshotImage || '/vite.svg'}
              alt=""
              className="w-full h-full object-cover"
            />
          </div>
        ))}
        {order.orderItems?.length > 3 && (
          <span className="text-xs text-[var(--color-text-muted)] font-light">
            +{order.orderItems.length - 3} more
          </span>
        )}
      </div>

      <div className="flex items-center justify-between mt-3 pt-3 border-t border-[var(--color-noir-border)]">
        <span className="text-xs text-[var(--color-text-muted)] font-light">
          {order.orderItems?.length || 0} {order.orderItems?.length === 1 ? 'item' : 'items'}
        </span>
        <span className="text-sm font-medium text-[var(--color-text-heading)]">
          ₹{Number(order.total).toLocaleString('en-IN')}
        </span>
      </div>
    </Link>
  );
}