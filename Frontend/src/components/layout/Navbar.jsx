import { useState, useEffect } from 'react';
import { Link, NavLink } from 'react-router-dom';
import { SITE } from '../../config/site';
import { useAuth } from '../../hooks/useAuth';
import { useCart } from '../../hooks/useCart';
import Button from '../ui/Button';
import AdminStoreBanner from '../admin/AdminStoreBanner';

export default function Navbar() {
  const { isAuthenticated, isAdmin, user, logout } = useAuth();
  const { itemCount } = useCart();
  const [mobileOpen, setMobileOpen] = useState(false);
  const [userMenuOpen, setUserMenuOpen] = useState(false);

  useEffect(() => {
    document.documentElement.classList.toggle('admin-browse', isAdmin);
    return () => document.documentElement.classList.remove('admin-browse');
  }, [isAdmin]);

  return (
    <header className="fixed top-0 left-0 right-0 z-40 bg-[var(--color-noir-surface)]/95 backdrop-blur-md border-b border-[var(--color-noir-border)]">
      {isAdmin && <AdminStoreBanner />}
      <div className="max-w-[var(--container-max)] mx-auto px-4 sm:px-6 lg:px-8">
        <div className="flex items-center justify-between h-[var(--header-height-mobile)] lg:h-[var(--header-height)]">
          {/* Logo */}
          <div className="">
            <Link to="/" className="">
              <span className="block text-xs sm:text-xl lg:text-2xl font-light tracking-[0.2em] uppercase text-[var(--color-gold)]">
                {SITE.name}
              </span>
              <span className="hidden sm:block sm:text-xs font-light tracking-[0.167em] lg:tracking-[0.33em] uppercase text-[var(--color-text-muted)]">
                {SITE.tagline}
              </span>
            </Link>
          </div>

          {/* Desktop nav */}
          <nav className="hidden lg:flex items-center gap-8">
            {SITE.nav.map((link) => (
              <NavLink
                key={link.href + link.label}
                to={link.href}
                className={({ isActive }) =>
                  `text-sm font-light tracking-wide transition-colors duration-200 ${
                    isActive
                      ? 'text-[var(--color-gold)]'
                      : 'text-[var(--color-text-muted)] hover:text-[var(--color-text-primary)]'
                  }`
                }
              >
                {link.label}
              </NavLink>
            ))}
          </nav>

          {/* Right side */}
          <div className="flex items-center gap-4">
            {/* Cart icon (hidden for admin browse-only preview) */}
            {!isAdmin && (
              <Link
                to="/cart"
                className="relative p-2 text-[var(--color-text-muted)] hover:text-[var(--color-gold)] transition-colors"
              >
                <svg width="22" height="22" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
                  <path d="M6 2L3 6v14a2 2 0 002 2h14a2 2 0 002-2V6l-3-4zM3 6h18" />
                  <path d="M16 10a4 4 0 01-8 0" />
                </svg>
                {itemCount > 0 && (
                  <span className="absolute -top-0.5 -right-0.5 w-5 h-5 flex items-center justify-center rounded-full bg-[var(--color-gold)] text-[var(--color-noir-page)] text-[11px] font-medium">
                    {itemCount > 99 ? '99+' : itemCount}
                  </span>
                )}
              </Link>
            )}

            {/* User menu */}
            {isAuthenticated ? (
              <div className="relative">
                <button
                  onClick={() => setUserMenuOpen(!userMenuOpen)}
                  className="flex items-center gap-2 p-2 text-[var(--color-text-muted)] hover:text-[var(--color-gold)] transition-colors cursor-pointer"
                >
                  <svg width="20" height="20" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
                    <path d="M20 21v-2a4 4 0 00-4-4H8a4 4 0 00-4 4v2" />
                    <circle cx="12" cy="7" r="4" />
                  </svg>
                  <span className="hidden lg:inline text-sm font-light">
                    {user?.firstName}
                  </span>
                </button>
                {userMenuOpen && (
                  <>
                    <div className="fixed inset-0 z-10" onClick={() => setUserMenuOpen(false)} />
                    <div className="absolute right-0 mt-2 w-48 bg-[var(--color-noir-elevated)] border border-[var(--color-noir-border)] rounded-lg shadow-xl z-20 py-1">
                      {isAdmin && (
                        <Link
                          to="/admin"
                          className="block px-4 py-2.5 text-sm font-light text-[var(--color-gold)] hover:bg-[var(--color-noir-border)] transition-colors"
                          onClick={() => setUserMenuOpen(false)}
                        >
                          Admin Portal
                        </Link>
                      )}
                      <Link
                        to="/account"
                        className="block px-4 py-2.5 text-sm font-light text-[var(--color-text-primary)] hover:bg-[var(--color-noir-border)] transition-colors"
                        onClick={() => setUserMenuOpen(false)}
                      >
                        My Account
                      </Link>
                      <Link
                        to="/account/orders"
                        className="block px-4 py-2.5 text-sm font-light text-[var(--color-text-primary)] hover:bg-[var(--color-noir-border)] transition-colors"
                        onClick={() => setUserMenuOpen(false)}
                      >
                        Orders
                      </Link>
                      <Link
                        to="/account/address"
                        className="block px-4 py-2.5 text-sm font-light text-[var(--color-text-primary)] hover:bg-[var(--color-noir-border)] transition-colors"
                        onClick={() => setUserMenuOpen(false)}
                      >
                        Address
                      </Link>
                      <hr className="my-1 border-[var(--color-noir-border)]" />
                      <button
                        onClick={() => {
                          logout();
                          setUserMenuOpen(false);
                        }}
                        className="w-full text-left px-4 py-2.5 text-sm font-light text-[var(--color-error)] hover:bg-[var(--color-noir-border)] transition-colors cursor-pointer"
                      >
                        Sign Out
                      </button>
                    </div>
                  </>
                )}
              </div>
            ) : (
              <Link to="/login">
                <Button variant="outline" size="sm">Sign In</Button>
              </Link>
            )}

            {/* Mobile hamburger */}
            <button
              onClick={() => setMobileOpen(!mobileOpen)}
              className="lg:hidden p-2 text-[var(--color-text-muted)] hover:text-[var(--color-gold)] transition-colors cursor-pointer"
            >
              {mobileOpen ? (
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
                  <path d="M18 6L6 18M6 6l12 12" />
                </svg>
              ) : (
                <svg width="24" height="24" viewBox="0 0 24 24" fill="none" stroke="currentColor" strokeWidth="1.5">
                  <path d="M3 12h18M3 6h18M3 18h18" />
                </svg>
              )}
            </button>
          </div>
        </div>
      </div>

      {/* Mobile menu */}
      {mobileOpen && (
        <div className="lg:hidden bg-[var(--color-noir-surface)] border-b border-[var(--color-noir-border)]">
          <div className="px-4 py-4 flex flex-col gap-2">
            {SITE.nav.map((link) => (
              <NavLink
                key={link.href + link.label}
                to={link.href}
                onClick={() => setMobileOpen(false)}
                className={({ isActive }) =>
                  `py-2 text-base font-light transition-colors ${
                    isActive
                      ? 'text-[var(--color-gold)]'
                      : 'text-[var(--color-text-muted)]'
                  }`
                }
              >
                {link.label}
              </NavLink>
            ))}
            {!isAuthenticated && (
              <NavLink
                to="/login"
                onClick={() => setMobileOpen(false)}
                className="py-2 text-base font-light text-[var(--color-gold)]"
              >
                Sign In
              </NavLink>
            )}
          </div>
        </div>
      )}
    </header>
  );
}