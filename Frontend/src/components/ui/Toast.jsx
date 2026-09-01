import { useState, useEffect, useCallback } from 'react';

let toastId = 0;

const listeners = new Set();

function notify(toast) {
  listeners.forEach((fn) => fn(toast));
}

export function toast(message, variant = 'info') {
  notify({ id: ++toastId, message, variant });
}

const variantStyles = {
  success: 'border-emerald-500/50 bg-emerald-500/10 text-emerald-400',
  error: 'border-red-500/50 bg-red-500/10 text-red-400',
  info: 'border-[var(--color-gold)]/50 bg-[var(--color-gold)]/10 text-[var(--color-gold)]',
};

export default function ToastContainer() {
  const [toasts, setToasts] = useState([]);

  const addToast = useCallback((t) => {
    setToasts((prev) => [...prev, t]);
    setTimeout(() => {
      setToasts((prev) => prev.filter((item) => item.id !== t.id));
    }, 4000);
  }, []);

  useEffect(() => {
    listeners.add(addToast);
    return () => listeners.delete(addToast);
  }, [addToast]);

  if (toasts.length === 0) return null;

  return (
    <div className="fixed bottom-6 right-6 z-50 flex flex-col gap-2">
      {toasts.map((t) => (
        <div
          key={t.id}
          className={`px-5 py-3 rounded-lg border text-sm font-light backdrop-blur-sm animate-[slideUp_0.3s_ease-out]
            ${variantStyles[t.variant]}`}
        >
          {t.message}
        </div>
      ))}
    </div>
  );
}