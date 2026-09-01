import { Link } from 'react-router-dom';
import Container from '../components/ui/Container';
import Button from '../components/ui/Button';

export default function NotFoundPage() {
  return (
    <Container className="min-h-[70vh] flex items-center justify-center py-20">
      <div className="text-center max-w-md">
        <p className="text-8xl font-light text-[var(--color-gold-dim)]/30 select-none">404</p>
        <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight mt-4">
          Page Not Found
        </h1>
        <p className="mt-3 text-[var(--color-text-muted)] font-light">
          The page you're looking for doesn't exist or has been moved.
        </p>
        <div className="mt-8 flex items-center justify-center gap-4">
          <Link to="/">
            <Button variant="primary">Go Home</Button>
          </Link>
          <Link to="/products">
            <Button variant="outline">Browse Products</Button>
          </Link>
        </div>
      </div>
    </Container>
  );
}