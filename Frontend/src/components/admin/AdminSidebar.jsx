import { NavLink } from 'react-router-dom';
import { useAuth } from '../../hooks/useAuth';
import { SITE } from '../../config/site';

const NAV_ITEMS = [
  { label: 'Dashboard', to: '/admin', end: true },
  { label: 'Products', to: '/admin/products' },
  { label: 'Orders', to: '/admin/orders' },
  { label: 'Users', to: '/admin/users' },
];

export default function AdminSidebar({ onNavigate }) {
  const { user, logout } = useAuth();

  const linkClass = ({ isActive }) =>
    `block px-4 py-2.5 rounded-lg text-sm font-light transition-colors ${
      isActive
        ? 'bg-[var(--color-gold)]/10 text-[var(--color-gold)]'
        : 'text-[var(--color-text-muted)] hover:text-[var(--color-text-primary)] hover:bg-[var(--color-noir-elevated)]'
    }`;

  return (
    <aside className="w-full lg:w-56 flex-shrink-0">
      <div className="mb-6">
        <p className="text-xs uppercase tracking-[0.2em] text-[var(--color-gold)]">{SITE.name}</p>
        <p className="text-sm font-light text-[var(--color-text-muted)] mt-1">Admin Portal</p>
      </div>

      <nav className="flex flex-col gap-1">
        {NAV_ITEMS.map((item) => (
          <NavLink
            key={item.to}
            to={item.to}
            end={item.end}
            className={linkClass}
            onClick={onNavigate}
          >
            {item.label}
          </NavLink>
        ))}
      </nav>

      <hr className="my-6 border-[var(--color-noir-border)]" />

      <p className="px-4 text-xs text-[var(--color-text-muted)] font-light truncate">
        {user?.firstName} {user?.lastName}
      </p>
      <button
        type="button"
        onClick={() => {
          logout();
          onNavigate?.();
        }}
        className="mt-2 w-full text-left px-4 py-2.5 text-sm font-light text-[var(--color-error)] hover:bg-[var(--color-noir-elevated)] rounded-lg transition-colors cursor-pointer"
      >
        Sign Out
      </button>
    </aside>
  );
}
