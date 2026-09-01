import { Link } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import StatsPlaceholder from '../../components/admin/StatsPlaceholder';
import Button from '../../components/ui/Button';

const QUICK_ACTIONS = [
  { label: 'Manage Products', to: '/admin/products', description: 'Create, edit, and remove catalog items' },
  { label: 'Manage Orders', to: '/admin/orders', description: 'View and update order status' },
  { label: 'Manage Users', to: '/admin/users', description: 'View customers and account details' },
];

export default function AdminDashboardPage() {
  const { user } = useAuth();

  return (
    <div className="space-y-10">
      <div>
        <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight">
          Welcome back, {user?.firstName}
        </h1>
        <p className="mt-2 text-sm font-light text-[var(--color-text-muted)]">
          Maison Noir administration — manage your store from here.
        </p>
      </div>

      <section>
        <h2 className="text-sm uppercase tracking-wider text-[var(--color-text-muted)] mb-4">
          Overview
        </h2>
        <StatsPlaceholder />
      </section>

      <section>
        <h2 className="text-sm uppercase tracking-wider text-[var(--color-text-muted)] mb-4">
          Quick actions
        </h2>
        <div className="grid grid-cols-1 md:grid-cols-3 gap-4">
          {QUICK_ACTIONS.map((action) => (
            <Link
              key={action.to}
              to={action.to}
              className="block p-5 bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg hover:border-[var(--color-gold-dim)] transition-colors"
            >
              <h3 className="text-base font-medium text-[var(--color-text-heading)] mb-2">
                {action.label}
              </h3>
              <p className="text-sm font-light text-[var(--color-text-muted)] mb-4">
                {action.description}
              </p>
              <span className="text-sm text-[var(--color-gold)]">Open →</span>
            </Link>
          ))}
        </div>
      </section>

      <div>
        <Link to="/">
          <Button variant="outline">View customer storefront</Button>
        </Link>
      </div>
    </div>
  );
}
