import { useState, useEffect } from 'react';
import { useParams, Link, useNavigate } from 'react-router-dom';
import { userService } from '../../services/user';
import { ordersService } from '../../services/orders';
import { useAuth } from '../../hooks/useAuth';
import Spinner from '../../components/ui/Spinner';
import Badge from '../../components/ui/Badge';
import Button from '../../components/ui/Button';
import Breadcrumb from '../../components/ui/Breadcrumb';
import ConfirmDialog from '../../components/admin/ConfirmDialog';
import { toast } from '../../components/ui/Toast';
import { SITE } from '../../config/site';

const statusBadgeVariant = {
  PENDING: 'warning',
  CONFIRMED: 'gold',
  SHIPPED: 'gold',
  DELIVERED: 'success',
  CANCELLED: 'error',
  RETURNED: 'default',
};

export default function AdminUserDetailPage() {
  const { userId } = useParams();
  const navigate = useNavigate();
  const { user: currentUser } = useAuth();
  const [profile, setProfile] = useState(null);
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);
  const [confirmDelete, setConfirmDelete] = useState(false);
  const [deleting, setDeleting] = useState(false);

  const isSelf = String(userId) === String(currentUser?.id);

  useEffect(() => {
    setLoading(true);
    Promise.all([
      userService.getById(userId),
      ordersService.getAll(),
    ])
      .then(([userData, allOrders]) => {
        setProfile(userData);
        const userOrders = (Array.isArray(allOrders) ? allOrders : []).filter(
          (o) => String(o.userId) === String(userId)
        );
        setOrders(userOrders);
      })
      .catch((err) => {
        toast(err.message || 'Failed to load user', 'error');
        setProfile(null);
        setOrders([]);
      })
      .finally(() => setLoading(false));
  }, [userId]);

  const handleDelete = async () => {
    setDeleting(true);
    try {
      await userService.deleteById(userId);
      toast('User deleted', 'success');
      navigate('/admin/users');
    } catch (err) {
      toast(err.message || 'Failed to delete user', 'error');
    } finally {
      setDeleting(false);
      setConfirmDelete(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center py-20">
        <Spinner size="lg" />
      </div>
    );
  }

  if (!profile) {
    return (
      <div className="text-center py-20">
        <p className="text-[var(--color-text-muted)] mb-4">User not found.</p>
        <Link to="/admin/users" className="text-[var(--color-gold)] text-sm">
          Back to users
        </Link>
      </div>
    );
  }

  return (
    <div>
      <Breadcrumb
        items={[
          { label: 'Admin', href: '/admin' },
          { label: 'Users', href: '/admin/users' },
          { label: profile.email, href: '#' },
        ]}
        className="mb-6"
      />

      <div className="flex flex-wrap items-start justify-between gap-4 mb-8">
        <div>
          <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight">
            {profile.firstName} {profile.lastName}
          </h1>
          <p className="text-sm text-[var(--color-text-muted)] mt-1">{profile.email}</p>
        </div>
        <Badge variant={profile.role === 'ADMIN' ? 'gold' : 'default'}>{profile.role}</Badge>
      </div>

      <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-5 mb-8 max-w-lg">
        <h2 className="text-sm font-medium text-[var(--color-text-heading)] mb-3">Profile</h2>
        <dl className="grid grid-cols-2 gap-3 text-sm font-light">
          <div>
            <dt className="text-[var(--color-text-muted)]">Phone</dt>
            <dd className="text-[var(--color-text-primary)]">{profile.phone || '—'}</dd>
          </div>
          <div>
            <dt className="text-[var(--color-text-muted)]">User ID</dt>
            <dd className="text-[var(--color-text-primary)]">{profile.id}</dd>
          </div>
        </dl>
        {!isSelf && (
          <Button
            variant="outline"
            size="sm"
            className="mt-4 !border-[var(--color-error)] !text-[var(--color-error)]"
            onClick={() => setConfirmDelete(true)}
          >
            Delete user
          </Button>
        )}
        {isSelf && (
          <p className="mt-4 text-xs text-[var(--color-text-muted)]">
            You cannot delete your own account.
          </p>
        )}
      </div>

      <h2 className="text-lg font-light text-[var(--color-text-heading)] mb-4">Orders</h2>
      {orders.length === 0 ? (
        <p className="text-sm text-[var(--color-text-muted)] font-light">No orders for this user.</p>
      ) : (
        <div className="overflow-x-auto border border-[var(--color-noir-border)] rounded-lg">
          <table className="w-full text-sm font-light">
            <thead>
              <tr className="border-b border-[var(--color-noir-border)] bg-[var(--color-noir-surface)]">
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Order</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Total</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Status</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Date</th>
                <th className="text-right p-3" />
              </tr>
            </thead>
            <tbody>
              {orders.map((order) => (
                <tr
                  key={order.id}
                  className="border-b border-[var(--color-noir-border)] last:border-0"
                >
                  <td className="p-3">#{order.id}</td>
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
                      className="text-[var(--color-gold)] hover:underline"
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

      <ConfirmDialog
        open={confirmDelete}
        onClose={() => setConfirmDelete(false)}
        onConfirm={handleDelete}
        title="Delete user"
        message={`Permanently delete ${profile.firstName} ${profile.lastName}?`}
        confirmLabel="Delete"
        loading={deleting}
        danger
      />
    </div>
  );
}
