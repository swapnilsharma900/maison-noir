import { Link } from 'react-router-dom';

export default function Breadcrumb({ items = [], className = '' }) {
  return (
    <nav className={`flex items-center gap-2 text-sm font-light ${className}`} aria-label="Breadcrumb">
      {items.map((item, index) => {
        const isLast = index === items.length - 1;
        return (
          <span key={item.href || index} className="flex items-center gap-2">
            {index > 0 && (
              <span className="text-[var(--color-text-muted)]">/</span>
            )}
            {isLast ? (
              <span className="text-[var(--color-gold)]">{item.label}</span>
            ) : (
              <Link
                to={item.href}
                className="text-[var(--color-text-muted)] hover:text-[var(--color-gold)] transition-colors duration-200"
              >
                {item.label}
              </Link>
            )}
          </span>
        );
      })}
    </nav>
  );
}