export default function Section({ title, subtitle, children, className = '' }) {
  return (
    <section className={`py-16 lg:py-24 ${className}`}>
      {(title || subtitle) && (
        <div className="max-w-[var(--container-max)] mx-auto px-4 sm:px-6 lg:px-8 mb-12">
          {title && (
            <h2 className="text-3xl lg:text-4xl font-light text-[var(--color-text-heading)] tracking-tight">
              {title}
            </h2>
          )}
          {subtitle && (
            <p className="mt-3 text-[var(--color-text-muted)] font-light max-w-2xl">{subtitle}</p>
          )}
          <div className="mt-4 h-px w-16 bg-[var(--color-gold)]" />
        </div>
      )}
      {children}
    </section>
  );
}