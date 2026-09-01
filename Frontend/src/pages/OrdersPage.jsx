import { useState, useEffect } from 'react';
import { ordersService } from '../services/orders';
import Container from '../components/ui/Container';
import Spinner from '../components/ui/Spinner';
import EmptyState from '../components/ui/EmptyState';
import OrderCard from '../components/ui/OrderCard';

export default function OrdersPage() {
  const [orders, setOrders] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    ordersService
      .getMyOrders()
      .then((data) => setOrders(Array.isArray(data) ? data : []))
      .catch(() => setOrders([]))
      .finally(() => setLoading(false));
  }, []);

  return (
    <Container className="py-8 lg:py-12">
      <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight mb-8">
        My Orders
      </h1>

      {loading ? (
        <div className="flex justify-center py-12">
          <Spinner size="lg" />
        </div>
      ) : orders.length === 0 ? (
        <EmptyState
          title="No orders yet"
          description="You haven't placed any orders yet. Start shopping and come back here to track them."
          icon="📦"
          actionLabel="Browse Products"
          onAction={() => window.location.href = '/products'}
        />
      ) : (
        <div className="grid grid-cols-1 md:grid-cols-2 gap-4">
          {orders.map((order) => (
            <OrderCard key={order.id} order={order} />
          ))}
        </div>
      )}
    </Container>
  );
}