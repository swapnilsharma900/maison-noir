import { useState, useEffect } from 'react';
import { useParams, useNavigate } from 'react-router-dom';
import { productsService } from '../services/products';
import { useCart } from '../hooks/useCart';
import { useAuth } from '../hooks/useAuth';
import Container from '../components/ui/Container';
import Button from '../components/ui/Button';
import Badge from '../components/ui/Badge';
import Spinner from '../components/ui/Spinner';
import Breadcrumb from '../components/ui/Breadcrumb';
import { toast } from '../components/ui/Toast';
import { SITE } from '../config/site';
import { resolveProductImage } from '../utils/images';

export default function ProductDetailPage() {
  const { productId } = useParams();
  const navigate = useNavigate();
  const { isAuthenticated, isAdmin } = useAuth();
  const { addItem } = useCart();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(true);
  const [selectedImage, setSelectedImage] = useState(0);
  const [selectedVariant, setSelectedVariant] = useState(null);
  const [adding, setAdding] = useState(false);

  useEffect(() => {
    setLoading(true);
    productsService
      .getById(productId)
      .then((data) => {
        setProduct(data);
        if (data.variantItems?.length > 0) {
          setSelectedVariant(data.variantItems[0]);
        }
        setSelectedImage(0);
      })
      .catch(() => setProduct(null))
      .finally(() => setLoading(false));
  }, [productId]);

  const handleAddToCart = async () => {
    if (!isAuthenticated) {
      navigate('/login');
      return;
    }
    if (!selectedVariant) return;
    setAdding(true);
    try {
      await addItem(selectedVariant.id, 1);
      toast('Added to cart', 'success');
    } catch {
      toast('Failed to add to cart', 'error');
    } finally {
      setAdding(false);
    }
  };

  if (loading) {
    return (
      <Container className="py-20 flex justify-center">
        <Spinner size="lg" />
      </Container>
    );
  }

  if (!product) {
    return (
      <Container className="py-20 text-center">
        <h2 className="text-2xl font-light text-[var(--color-text-heading)]">Product not found</h2>
      </Container>
    );
  }

  const images = product.images?.length
    ? product.images.map(resolveProductImage)
    : [resolveProductImage()];
  const selectedPrice = selectedVariant?.price ?? product.variantItems?.[0]?.price;

  return (
    <Container className="py-8 lg:py-12">
      <Breadcrumb
        items={[
          { label: 'Home', href: '/' },
          { label: 'Products', href: '/products' },
          { label: product.name, href: '#' },
        ]}
        className="mb-8"
      />

      <div className="grid grid-cols-1 lg:grid-cols-2 gap-8 lg:gap-12">
        {/* Image Gallery */}
        <div className="flex flex-col-reverse sm:flex-row gap-4">
          {/* Thumbnails */}
          <div className="flex sm:flex-col gap-2 overflow-x-auto sm:overflow-visible">
            {images.map((img, i) => (
              <button
                key={i}
                onClick={() => setSelectedImage(i)}
                className={`w-16 h-20 flex-shrink-0 rounded-md overflow-hidden border-2 transition-colors cursor-pointer ${
                  i === selectedImage
                    ? 'border-[var(--color-gold)]'
                    : 'border-transparent hover:border-[var(--color-gold-dim)]'
                }`}
              >
                <img src={img} alt="" className="w-full h-full object-cover" />
              </button>
            ))}
          </div>
          {/* Main image */}
          <div className="flex-1 aspect-[3/4] bg-[var(--color-noir-elevated)] rounded-lg overflow-hidden border border-[var(--color-noir-border)]">
            <img
              src={images[selectedImage]}
              alt={product.name}
              className="w-full h-full object-cover"
            />
          </div>
        </div>

        {/* Product Info */}
        <div>
          <Badge variant="gold" className="mb-3">{product.category}</Badge>
          <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight">
            {product.name}
          </h1>

          {selectedPrice !== undefined && (
            <p className="mt-4 text-2xl font-light text-[var(--color-gold)]">
              ₹{Number(selectedPrice).toLocaleString('en-IN')}
            </p>
          )}

          <p className="mt-6 text-[var(--color-text-muted)] font-light leading-relaxed text-sm">
            {product.description}
          </p>

          {/* Variants */}
          {product.variantItems?.length > 0 && (
            <div className="mt-8">
              <h3 className="text-sm font-medium text-[var(--color-text-heading)] mb-3">
                {product.variantItems[0]?.variantLabel || 'Variant'}
              </h3>
              <div className="flex flex-wrap gap-2">
                {product.variantItems.map((v) => (
                  <button
                    key={v.id}
                    onClick={() => setSelectedVariant(v)}
                    className={`px-4 py-2 rounded-lg border text-sm font-light transition-colors cursor-pointer ${
                      selectedVariant?.id === v.id
                        ? 'border-[var(--color-gold)] bg-[var(--color-gold)]/10 text-[var(--color-gold)]'
                        : 'border-[var(--color-noir-border)] text-[var(--color-text-muted)] hover:border-[var(--color-gold-dim)]'
                    }`}
                  >
                    {v.name}
                    {v.stockCount !== undefined && v.stockCount <= 5 && v.stockCount > 0 && (
                      <span className="ml-2 text-xs text-[var(--color-warning)]">
                        Only {v.stockCount} left
                      </span>
                    )}
                  </button>
                ))}
              </div>
            </div>
          )}

          {/* Add to cart (disabled for admin browse-only preview) */}
          {!isAdmin && (
            <div className="mt-8 flex gap-4">
              <Button
                size="lg"
                className="flex-1"
                disabled={adding || !selectedVariant}
                onClick={handleAddToCart}
              >
                {adding ? 'Adding...' : 'Add to Cart'}
              </Button>
            </div>
          )}
          {isAdmin && (
            <p className="mt-8 text-sm font-light text-[var(--color-text-muted)]">
              Cart is disabled in admin preview mode.
            </p>
          )}

          {/* Attributes */}
          {product.attributes && Object.keys(product.attributes).length > 0 && (
            <div className="mt-8 pt-8 border-t border-[var(--color-noir-border)]">
              <h3 className="text-sm font-medium text-[var(--color-text-heading)] mb-3">
                Details
              </h3>
              <dl className="grid grid-cols-2 gap-2">
                {Object.entries(product.attributes).map(([key, value]) => (
                  <div key={key} className="flex flex-col">
                    <dt className="text-xs text-[var(--color-text-muted)] capitalize">{key}</dt>
                    <dd className="text-sm font-light text-[var(--color-text-primary)]">
                      {typeof value === 'string' ? value : JSON.stringify(value)}
                    </dd>
                  </div>
                ))}
              </dl>
            </div>
          )}
        </div>
      </div>
    </Container>
  );
}