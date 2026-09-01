const variantStyles = {
  default: 'bg-[var(--color-noir-border)] text-[var(--color-text-muted)]',
  success: 'bg-emerald-500/10 text-emerald-400',
  error: 'bg-red-500/10 text-red-400',
  warning: 'bg-amber-500/10 text-amber-400',
  gold: 'bg-[var(--color-gold)]/10 text-[var(--color-gold)]',
};

export default function Badge({ children, variant = 'default', className = '' }) {
  return (
    <span
      className={`inline-flex items-center px-2.5 py-0.5 rounded-full text-xs font-medium tracking-wide uppercase
        ${variantStyles[variant]} ${className}`}
    >
      {children}
    </span>
  );
}