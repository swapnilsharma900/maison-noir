import Modal from '../ui/Modal';
import Button from '../ui/Button';

export default function ConfirmDialog({
  open,
  onClose,
  onConfirm,
  title,
  message,
  confirmLabel = 'Confirm',
  loading = false,
  danger = false,
}) {
  return (
    <Modal open={open} onClose={onClose} title={title}>
      <p className="text-sm font-light text-[var(--color-text-muted)] mb-6">{message}</p>
      <div className="flex justify-end gap-3">
        <Button variant="outline" size="sm" onClick={onClose} disabled={loading}>
          Cancel
        </Button>
        <Button
          variant={danger ? 'primary' : 'primary'}
          size="sm"
          onClick={onConfirm}
          disabled={loading}
          className={danger ? '!bg-[var(--color-error)] hover:!bg-red-500' : ''}
        >
          {loading ? 'Please wait...' : confirmLabel}
        </Button>
      </div>
    </Modal>
  );
}
