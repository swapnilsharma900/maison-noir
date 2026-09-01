import { useEffect } from 'react';
import { Link } from 'react-router-dom';
import { useCart } from '../hooks/useCart';
import { useAuth } from '../hooks/useAuth';
import { useAdminBrowseGuard } from '../hooks/useAdminBrowseGuard';
import Container from '../components/ui/Container';
import Button from '../components/ui/Button';
import Spinner from '../components/ui/Spinner';
import EmptyState from '../components/ui/EmptyState';
import { toast } from '../components/ui/Toast';
import { SITE } from '../config/site';
import { resolveProductImage } from '../utils/images';

export default function CartPage() {
  useAdminBrowseGuard();
  const { cart, loading, fetchCart, updateItem, removeItem, clearCart } = useCart();
  const { isAuthenticated } = useAuth();

  useEffect(() => {
    if (isAuthenticated) fetchCart();
  }, [isAuthenticated, fetchCart]);

  const handleUpdateQty = async (itemId, newQty) => {
    if (newQty < 1) return;
    try {
      await updateItem(itemId, newQty);
    } catch {
      toast('Failed to update item', 'error');
    }
  };

  const handleRemove = async (itemId) => {
    try {
      await removeItem(itemId);
      toast('Item removed', 'info');
    } catch {
      toast('Failed to remove item', 'error');
    }
  };

  const handleClear = async () => {
    try {
      await clearCart();
      toast('Cart cleared', 'info');
    } catch {
      toast('Failed to clear cart', 'error');
    }
  };

  if (!isAuthenticated) {
    return (
      <Container className="py-20">
        <EmptyState
          title="Sign in to view your cart"
          description="You need to be signed in to access your shopping cart."
          icon="🛒"
          actionLabel="Sign In"
          onAction={() => window.location.href = '/login'}
        />
      </Container>
    );
  }

  if (loading) {
    return (
      <Container className="py-20 flex justify-center">
        <Spinner size="lg" />
      </Container>
    );
  }

  if (!cart || !cart.items?.length) {
    return (
      <Container className="py-20">
        <EmptyState
          title="Your cart is empty"
          description="Looks like you haven't added anything yet. Explore our collection and find something you love."
          icon="🛍️"
          actionLabel="Browse Products"
          onAction={() => window.location.href = '/products'}
        />
      </Container>
    );
  }

  return (
    <Container className="py-8 lg:py-12">
      <div className="flex items-center justify-between mb-8">
        <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight">
          Shopping Cart
        </h1>
        <button
          onClick={handleClear}
          className="text-sm font-light text-[var(--color-text-muted)] hover:text-[var(--color-error)] transition-colors cursor-pointer"
        >
          Clear All
        </button>
      </div>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        {/* Cart Items */}
        <div className="lg:col-span-2 flex flex-col gap-4">
          {cart.items.map((item) => (
            <div
              key={item.id}
              className="flex gap-4 p-4 bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg"
            >
              <div className="w-20 h-24 flex-shrink-0 bg-[var(--color-noir-elevated)] rounded-md overflow-hidden">
                <img
                  src={resolveProductImage(item.snapshotImage)}
                  alt={item.snapshotName}
                  className="w-full h-full object-cover"
                />
              </div>
              <div className="flex-1 min-w-0">
                <h3 className="font-medium text-sm text-[var(--color-text-heading)] truncate">
                  {item.snapshotName}
                </h3>
                <p className="text-xs text-[var(--color-text-muted)] mt-0.5">
                  {item.variantLabel}: {item.variantLabel}
                </p>
                <p className="text-sm font-medium text-[var(--color-gold)] mt-1">
                  ₹{Number(item.snapshotPrice).toLocaleString('en-IN')}
                </p>
                <div className="flex items-center justify-between mt-3">
                  <div className="flex items-center gap-2">
                    <button
                      onClick={() => handleUpdateQty(item.id, item.quantity - 1)}
                      className="w-7 h-7 flex items-center justify-center rounded border border-[var(--color-noir-border)] text-sm text-[var(--color-text-muted)] hover:text-[var(--color-text-primary)] transition-colors cursor-pointer"
                    >
                      -
                    </button>
                    <span className="text-sm font-light w-6 text-center">{item.quantity}</span>
                    <button
                      onClick={() => handleUpdateQty(item.id, item.quantity + 1)}
                      className="w-7 h-7 flex items-center justify-center rounded border border-[var(--color-noir-border)] text-sm text-[var(--color-text-muted)] hover:text-[var(--color-text-primary)] transition-colors cursor-pointer"
                    >
                      +
                    </button>
                  </div>
                  <button
                    onClick={() => handleRemove(item.id)}
                    className="text-xs font-light text-[var(--color-text-muted)] hover:text-[var(--color-error)] transition-colors cursor-pointer"
                  >
                    Remove
                  </button>
                </div>
              </div>
              <div className="text-right flex-shrink-0">
                <p className="text-sm font-medium text-[var(--color-text-heading)]">
                  ₹{Number(item.totalPrice).toLocaleString('en-IN')}
                </p>
              </div>
            </div>
          ))}
        </div>

        {/* Order Summary */}
        <div className="lg:col-span-1">
          <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-6 sticky top-[calc(var(--header-height)+2rem)]">
            <h2 className="text-lg font-light text-[var(--color-text-heading)] mb-4">
              Order Summary
            </h2>
            <div className="flex justify-between text-sm font-light mb-2">
              <span className="text-[var(--color-text-muted)]">Subtotal ({cart.totalItems} items)</span>
              <span className="text-[var(--color-text-primary)]">
                ₹{Number(cart.totalAmount).toLocaleString('en-IN')}
              </span>
            </div>
            <div className="flex justify-between text-sm font-light mb-2">
              <span className="text-[var(--color-text-muted)]">Shipping</span>
              <span className="text-[var(--color-text-primary)]">Calculated at checkout</span>
            </div>
            <hr className="my-4 border-[var(--color-noir-border)]" />
            <div className="flex justify-between font-medium mb-6">
              <span className="text-[var(--color-text-heading)]">Total</span>
              <span className="text-[var(--color-gold)] text-lg">
                ₹{Number(cart.totalAmount).toLocaleString('en-IN')}
              </span>
            </div>
            <Link to="/checkout">
              <Button className="w-full" size="lg">
                Proceed to Checkout
              </Button>
            </Link>
          </div>
        </div>
      </div>
    </Container>
  );
}