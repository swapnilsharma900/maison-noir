const STAT_CARDS = [
  { title: 'Total Orders', icon: '📦' },
  { title: 'Pending Orders', icon: '⏳' },
  { title: 'Products', icon: '👔' },
  { title: 'Users', icon: '👤' },
];

export default function StatsPlaceholder() {
  return (
    <div className="grid grid-cols-1 sm:grid-cols-2 lg:grid-cols-4 gap-4">
      {STAT_CARDS.map((card) => (
        <div
          key={card.title}
          className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-5 opacity-80"
        >
          <div className="flex items-center justify-between mb-3">
            <span className="text-2xl" aria-hidden>
              {card.icon}
            </span>
            <span className="text-xs uppercase tracking-wider text-[var(--color-text-muted)]">
              Coming soon
            </span>
          </div>
          <h3 className="text-sm font-medium text-[var(--color-text-heading)] mb-2">
            {card.title}
          </h3>
          <p className="text-xs font-light text-[var(--color-text-muted)] leading-relaxed">
            Statistics are not available yet. This section will be enabled in a future update.
          </p>
        </div>
      ))}
    </div>
  );
}
