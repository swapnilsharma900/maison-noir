import { Link } from 'react-router-dom';
import Button from '../ui/Button';

export default function AdminStoreBanner() {
  return (
    <div className="bg-[var(--color-noir-elevated)] border-b border-[var(--color-noir-border)]">
      <div className="max-w-[var(--container-max)] mx-auto px-4 sm:px-6 lg:px-8 py-1.5 flex flex-wrap items-center justify-between gap-2">
        <p className="text-xs sm:text-sm font-light text-[var(--color-text-muted)]">
          Admin preview — browse only. Cart and checkout are disabled.
        </p>
        <Link to="/admin">
          <Button variant="ghost" size="sm">
            Back to Admin Portal
          </Button>
        </Link>
      </div>
    </div>
  );
}
