const variantStyles = {
  text: 'h-4 bg-[var(--color-noir-border)] rounded',
  card: 'h-64 bg-[var(--color-noir-surface)] rounded-lg border border-[var(--color-noir-border)]',
  image: 'aspect-[3/4] bg-[var(--color-noir-surface)] rounded-lg border border-[var(--color-noir-border)]',
  circle: 'w-12 h-12 bg-[var(--color-noir-border)] rounded-full',
};

export default function Skeleton({ variant = 'text', className = '', count = 1 }) {
  if (count > 1) {
    return (
      <div className={`flex flex-col gap-3 ${className}`}>
        {Array.from({ length: count }).map((_, i) => (
          <div key={i} className={`animate-pulse ${variantStyles[variant]}`} />
        ))}
      </div>
    );
  }

  return <div className={`animate-pulse ${variantStyles[variant]} ${className}`} />;
}