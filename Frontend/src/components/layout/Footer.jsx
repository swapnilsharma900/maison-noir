import { Link } from 'react-router-dom';
import { SITE } from '../../config/site';

export default function Footer() {
  return (
    <footer className="bg-[var(--color-noir-surface)] border-t border-[var(--color-noir-border)]">
      <div className="max-w-[var(--container-max)] mx-auto px-4 sm:px-6 lg:px-8 py-16">
        <div className="grid grid-cols-1 md:grid-cols-2 lg:grid-cols-5 gap-10">
          {/* Brand */}
          <div className="lg:col-span-2">
            <Link to="/" className="text-xl font-light tracking-[0.2em] uppercase text-[var(--color-gold)]">
              {SITE.name}
            </Link>
            <p className="mt-4 text-sm font-light text-[var(--color-text-muted)] leading-relaxed">
              {SITE.footer.about}
            </p>
          </div>

          {/* Link columns */}
          {SITE.footer.columns.map((col) => (
            <div key={col.title}>
              <h4 className="text-sm font-medium text-[var(--color-text-heading)] mb-4 tracking-wide uppercase">
                {col.title}
              </h4>
              <ul className="flex flex-col gap-2">
                {col.links.map((link) => (
                  <li key={link.label}>
                    <Link
                      to={link.href}
                      className="text-sm font-light text-[var(--color-text-muted)] hover:text-[var(--color-gold)] transition-colors duration-200"
                    >
                      {link.label}
                    </Link>
                  </li>
                ))}
              </ul>
            </div>
          ))}
        </div>

        <div className="mt-12 pt-8 border-t border-[var(--color-noir-border)]">
          <p className="text-sm font-light text-[var(--color-text-muted)] text-center">
            {SITE.footer.copyright}
          </p>
        </div>
      </div>
    </footer>
  );
}