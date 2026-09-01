import { useState } from 'react';
import { Link, Outlet } from 'react-router-dom';
import { SITE } from '../../config/site';
import Button from '../ui/Button';
import AdminSidebar from './AdminSidebar';

export default function AdminLayout() {
  const [sidebarOpen, setSidebarOpen] = useState(false);

  return (
    <div className="min-h-screen bg-[var(--color-noir-page)] flex flex-col">
      <header className="sticky top-0 z-40 bg-[var(--color-noir-surface)]/95 backdrop-blur-md border-b border-[var(--color-noir-border)]">
        <div className="max-w-[var(--container-max)] mx-auto px-4 sm:px-6 lg:px-8 h-14 flex items-center justify-between">
          <div className="flex items-center gap-4">
            <button
              type="button"
              className="lg:hidden p-2 text-[var(--color-text-muted)] hover:text-[var(--color-gold)] cursor-pointer"
              onClick={() => setSidebarOpen(!sidebarOpen)}
              aria-label="Toggle menu"
            >
              <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
                <path d="M3 12h18M3 6h18M3 18h18" />
              </svg>
            </button>
            <Link to="/admin" className="text-sm font-light tracking-[0.15em] uppercase text-[var(--color-gold)]">
              {SITE.name} · Admin
            </Link>
          </div>
          <Link to="/">
            <Button variant="outline" size="sm">
              View Store
            </Button>
          </Link>
        </div>
      </header>

      <div className="flex-1 max-w-[var(--container-max)] mx-auto w-full px-4 sm:px-6 lg:px-8 py-8">
        <div className="flex flex-col lg:flex-row gap-8">
          <div className={`${sidebarOpen ? 'block' : 'hidden'} lg:block`}>
            <AdminSidebar onNavigate={() => setSidebarOpen(false)} />
          </div>
          <main className="flex-1 min-w-0">
            <Outlet />
          </main>
        </div>
      </div>
    </div>
  );
}
