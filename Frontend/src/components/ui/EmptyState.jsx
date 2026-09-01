import Button from './Button';

export default function EmptyState({ icon, title, description, actionLabel, onAction, className = '' }) {
  return (
    <div className={`flex flex-col items-center justify-center py-16 px-4 text-center ${className}`}>
      {icon && <div className="text-5xl mb-4 opacity-40">{icon}</div>}
      {title && (
        <h3 className="text-xl font-light text-[var(--color-text-heading)] mb-2">{title}</h3>
      )}
      {description && (
        <p className="text-[var(--color-text-muted)] font-light max-w-md mb-6">{description}</p>
      )}
      {actionLabel && onAction && (
        <Button variant="outline" onClick={onAction}>
          {actionLabel}
        </Button>
      )}
    </div>
  );
}