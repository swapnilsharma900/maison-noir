const sizes = {
  sm: 'w-4 h-4 border-2',
  md: 'w-8 h-8 border-2',
  lg: 'w-12 h-12 border-[3px]',
};

export default function Spinner({ size = 'md', className = '' }) {
  return (
    <div
      className={`${sizes[size]} rounded-full border-[var(--color-noir-border)] border-t-[var(--color-gold)] animate-spin ${className}`}
      role="status"
      aria-label="Loading"
    />
  );
}