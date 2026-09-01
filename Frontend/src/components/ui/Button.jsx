const variants = {
  primary:
    'bg-[var(--color-gold)] text-[var(--color-noir-page)] hover:bg-[var(--color-gold-hover)]',
  outline:
    'border border-[var(--color-gold)] text-[var(--color-gold)] hover:bg-[var(--color-gold)]/10',
  ghost: 'text-[var(--color-gold)] hover:bg-[var(--color-noir-elevated)]',
};

const sizes = {
  sm: 'py-1.5 px-3 text-sm',
  md: 'py-2.5 px-5 text-sm',
  lg: 'py-3 px-8 text-base',
};

export default function Button({
  children,
  variant = 'primary',
  size = 'md',
  className = '',
  disabled = false,
  ...props
}) {
  return (
    <button
      className={`inline-flex items-center justify-center rounded-lg font-medium transition-colors duration-200
        ${variants[variant]} ${sizes[size]}
        ${disabled ? 'opacity-50 cursor-not-allowed' : 'cursor-pointer'}
        ${className}`}
      disabled={disabled}
      {...props}
    >
      {children}
    </button>
  );
}