import { Link } from 'react-router-dom';
import Badge from './Badge';
import { resolveProductImage } from '../../utils/images';

export default function ProductCard({ product }) {
  const image = resolveProductImage(product.images?.[0]);
  const price = product.variantItems?.[0]?.price;

  return (
    <Link
      to={`/products/${product.id}`}
      className="group block bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg overflow-hidden hover:border-[var(--color-gold-dim)] transition-all duration-300"
    >
      <div className="aspect-[3/4] overflow-hidden bg-[var(--color-noir-elevated)]">
        <img
          src={image}
          alt={product.name}
          className="w-full h-full object-cover group-hover:scale-105 transition-transform duration-500"
        />
      </div>
      <div className="p-4">
        <Badge variant="gold" className="mb-2">
          {product.category}
        </Badge>
        <h3 className="font-medium text-[var(--color-text-heading)] text-sm truncate group-hover:text-[var(--color-gold)] transition-colors">
          {product.name}
        </h3>
        {price !== undefined && (
          <p className="mt-1 font-medium text-[var(--color-gold)] text-sm">
            ₹{Number(price).toLocaleString('en-IN')}
          </p>
        )}
      </div>
    </Link>
  );
}