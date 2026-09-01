export default function Input({
  label,
  error,
  type = 'text',
  className = '',
  ...props
}) {
  return (
    <div className="w-full">
      {label && (
        <label className="block text-sm text-[var(--color-text-muted)] mb-1.5 font-light">
          {label}
        </label>
      )}
      {type === 'textarea' ? (
        <textarea
          className={`w-full bg-[var(--color-noir-elevated)] border border-[var(--color-noir-border)] rounded-lg px-4 py-2.5
            text-[var(--color-text-primary)] placeholder:text-[var(--color-text-muted)]
            focus:border-[var(--color-gold)] focus:ring-1 focus:ring-[var(--color-gold)]
            transition-colors duration-200 font-light text-sm ${className}`}
          {...props}
        />
      ) : type === 'select' ? (
        <select
          className={`w-full bg-[var(--color-noir-elevated)] border border-[var(--color-noir-border)] rounded-lg px-4 py-2.5
            text-[var(--color-text-primary)]
            focus:border-[var(--color-gold)] focus:ring-1 focus:ring-[var(--color-gold)]
            transition-colors duration-200 font-light text-sm ${className}`}
          {...props}
        />
      ) : (
        <input
          type={type}
          className={`w-full bg-[var(--color-noir-elevated)] border border-[var(--color-noir-border)] rounded-lg px-4 py-2.5
            text-[var(--color-text-primary)] placeholder:text-[var(--color-text-muted)]
            focus:border-[var(--color-gold)] focus:ring-1 focus:ring-[var(--color-gold)]
            transition-colors duration-200 font-light text-sm ${className}`}
          {...props}
        />
      )}
      {error && <p className="mt-1 text-sm text-[var(--color-error)]">{error}</p>}
    </div>
  );
}