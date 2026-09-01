import { useState, useEffect } from 'react';
import { useNavigate, Link } from 'react-router-dom';
import { useCart } from '../hooks/useCart';
import { useAuth } from '../hooks/useAuth';
import { useAdminBrowseGuard } from '../hooks/useAdminBrowseGuard';
import { ordersService } from '../services/orders';
import { addressService } from '../services/address';
import Container from '../components/ui/Container';
import Button from '../components/ui/Button';
import Spinner from '../components/ui/Spinner';
import EmptyState from '../components/ui/EmptyState';
import { toast } from '../components/ui/Toast';
import { SITE } from '../config/site';
import { resolveProductImage } from '../utils/images';

export default function CheckoutPage() {
  useAdminBrowseGuard();
  const { isAuthenticated } = useAuth();
  const { cart, fetchCart } = useCart();
  const navigate = useNavigate();
  const [address, setAddress] = useState(null);
  const [paymentMethod, setPaymentMethod] = useState('COD');
  const [placing, setPlacing] = useState(false);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    fetchCart();
    addressService
      .get()
      .then((data) => setAddress(data))
      .catch(() => setAddress(null))
      .finally(() => setLoading(false));
  }, [isAuthenticated, fetchCart, navigate]);

  const handlePlaceOrder = async () => {
    if (!address) {
      toast('Please add a shipping address first', 'error');
      return;
    }
    setPlacing(true);
    try {
      await ordersService.place(paymentMethod);
      toast('Order placed successfully!', 'success');
      navigate('/account/orders');
    } catch (err) {
      toast(err.message || 'Failed to place order', 'error');
    } finally {
      setPlacing(false);
      fetchCart();
    }
  };

  if (!isAuthenticated) return null;

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
          description="Add some items before checking out."
          icon="🛒"
          actionLabel="Browse Products"
          onAction={() => navigate('/products')}
        />
      </Container>
    );
  }

  return (
    <Container className="py-8 lg:py-12">
      <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight mb-8">
        Checkout
      </h1>

      <div className="grid grid-cols-1 lg:grid-cols-3 gap-8">
        <div className="lg:col-span-2 space-y-8">
          {/* Address */}
          <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-6">
            <div className="flex items-center justify-between mb-4">
              <h2 className="text-lg font-light text-[var(--color-text-heading)]">
                Shipping Address
              </h2>
              <Link
                to="/account/address"
                className="text-sm font-light text-[var(--color-gold)] hover:text-[var(--color-gold-hover)] transition-colors"
              >
                {address ? 'Edit' : 'Add'}
              </Link>
            </div>
            {address ? (
              <div className="text-sm font-light text-[var(--color-text-muted)] space-y-0.5">
                <p className="text-[var(--color-text-primary)]">{address.lineOne}, {address.lineTwo}</p>
                {address.landmark && <p>{address.landmark}</p>}
                <p>{address.city}, {address.state} - {address.pincode}</p>
                <p>{address.country}</p>
              </div>
            ) : (
              <p className="text-sm font-light text-[var(--color-warning)]">
                Please add a shipping address to continue.
              </p>
            )}
          </div>

          {/* Order Items */}
          <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-6">
            <h2 className="text-lg font-light text-[var(--color-text-heading)] mb-4">
              Order Items ({cart.totalItems})
            </h2>
            <div className="flex flex-col gap-3">
              {cart.items.map((item) => (
                <div key={item.id} className="flex items-center gap-3 text-sm">
                  <img
                    src={resolveProductImage(item.snapshotImage)}
                    alt=""
                    className="w-10 h-12 rounded object-cover flex-shrink-0"
                  />
                  <div className="flex-1 min-w-0">
                    <p className="font-light text-[var(--color-text-primary)] truncate">
                      {item.snapshotName}
                    </p>
                    <p className="text-xs text-[var(--color-text-muted)]">Qty: {item.quantity}</p>
                  </div>
                  <p className="font-light text-[var(--color-text-primary)]">
                    ₹{Number(item.totalPrice).toLocaleString('en-IN')}
                  </p>
                </div>
              ))}
            </div>
          </div>

          {/* Payment */}
          <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-6">
            <h2 className="text-lg font-light text-[var(--color-text-heading)] mb-4">
              Payment Method
            </h2>
            <div className="flex flex-wrap gap-3">
              {SITE.paymentMethods.map((method) => (
                <button
                  key={method}
                  onClick={() => setPaymentMethod(method)}
                  className={`px-4 py-2 rounded-lg border text-sm font-light transition-colors cursor-pointer ${
                    paymentMethod === method
                      ? 'border-[var(--color-gold)] bg-[var(--color-gold)]/10 text-[var(--color-gold)]'
                      : 'border-[var(--color-noir-border)] text-[var(--color-text-muted)] hover:border-[var(--color-gold-dim)]'
                  }`}
                >
                  {method}
                </button>
              ))}
            </div>
          </div>
        </div>

        {/* Summary sidebar */}
        <div className="lg:col-span-1">
          <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-6 sticky top-[calc(var(--header-height)+2rem)]">
            <h2 className="text-lg font-light text-[var(--color-text-heading)] mb-4">
              Summary
            </h2>
            <div className="flex justify-between text-sm font-light mb-2">
              <span className="text-[var(--color-text-muted)]">Subtotal</span>
              <span className="text-[var(--color-text-primary)]">
                ₹{Number(cart.totalAmount).toLocaleString('en-IN')}
              </span>
            </div>
            <div className="flex justify-between text-sm font-light mb-2">
              <span className="text-[var(--color-text-muted)]">Shipping</span>
              <span className="text-emerald-400">Free</span>
            </div>
            <hr className="my-4 border-[var(--color-noir-border)]" />
            <div className="flex justify-between font-medium mb-6">
              <span className="text-[var(--color-text-heading)]">Total</span>
              <span className="text-[var(--color-gold)] text-lg">
                ₹{Number(cart.totalAmount).toLocaleString('en-IN')}
              </span>
            </div>
            <Button
              className="w-full"
              size="lg"
              disabled={placing || !address}
              onClick={handlePlaceOrder}
            >
              {placing ? 'Placing Order...' : 'Place Order'}
            </Button>
          </div>
        </div>
      </div>
    </Container>
  );
}