import { useState, useEffect } from 'react';
import { useParams, useSearchParams } from 'react-router-dom';
import { productsService } from '../services/products';
import Container from '../components/ui/Container';
import Section from '../components/ui/Section';
import Input from '../components/ui/Input';
import Spinner from '../components/ui/Spinner';
import ProductCard from '../components/ui/ProductCard';
import EmptyState from '../components/ui/EmptyState';
import { SITE } from '../config/site';
import { toast } from '../components/ui/Toast';

const PRODUCT_CATEGORIES = SITE.categories.filter((cat) => cat.key);

export default function ProductListPage() {
  const { category } = useParams();
  const [searchParams] = useSearchParams();
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [search, setSearch] = useState(searchParams.get('q') || '');
  const [selectedCategory, setSelectedCategory] = useState(category || '');

  useEffect(() => {
    setSelectedCategory(category || '');
  }, [category]);

  useEffect(() => {
    setLoading(true);
    const fetchFn = search
      ? productsService.search(search)
      : selectedCategory
        ? productsService.getByCategory(selectedCategory)
        : productsService.getAll();

    fetchFn
      .then((data) => setProducts(Array.isArray(data) ? data : []))
      .catch((err) => {
        setProducts([]);
        toast(err.message || 'Failed to load products', 'error');
      })
      .finally(() => setLoading(false));
  }, [selectedCategory, search]);

  const handleSearch = (e) => {
    e.preventDefault();
    if (search.trim()) {
      setSelectedCategory('');
    }
  };

  return (
    <Section
      title={
        selectedCategory
          ? PRODUCT_CATEGORIES.find((c) => c.key === selectedCategory)?.label || selectedCategory
          : 'All Products'
      }
      subtitle="Discover our curated collection."
    >
      <Container>
        {/* Filters */}
        <div className="flex flex-col sm:flex-row gap-4 mb-10">
          <form onSubmit={handleSearch} className="flex-1">
            <Input
              type="text"
              placeholder="Search products..."
              value={search}
              onChange={(e) => {
                setSearch(e.target.value);
                if (!e.target.value) {
                  setSelectedCategory(selectedCategory || '');
                }
              }}
            />
          </form>
        </div>

        {/* Category pills */}
        <div className="flex flex-wrap gap-2 mb-8">
          <button
            onClick={() => setSelectedCategory('')}
            className={`px-3 py-1.5 rounded-full text-xs font-light transition-colors cursor-pointer ${
              !selectedCategory
                ? 'bg-[var(--color-gold)] text-[var(--color-noir-page)]'
                : 'bg-[var(--color-noir-elevated)] text-[var(--color-text-muted)] hover:text-[var(--color-text-primary)]'
            }`}
          >
            All
          </button>
          {PRODUCT_CATEGORIES.map((cat) => (
            <button
              key={cat.key}
              onClick={() => {
                setSelectedCategory(cat.key);
                setSearch('');
              }}
              className={`px-3 py-1.5 rounded-full text-xs font-light transition-colors cursor-pointer ${
                selectedCategory === cat.key
                  ? 'bg-[var(--color-gold)] text-[var(--color-noir-page)]'
                  : 'bg-[var(--color-noir-elevated)] text-[var(--color-text-muted)] hover:text-[var(--color-text-primary)]'
              }`}
            >
              {cat.label}
            </button>
          ))}
        </div>

        {/* Product grid */}
        {loading ? (
          <div className="flex justify-center py-12">
            <Spinner size="lg" />
          </div>
        ) : products.length === 0 ? (
          <EmptyState
            title="No products found"
            description="Try adjusting your search or filter."
            icon="🔍"
          />
        ) : (
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 lg:gap-6">
            {products.map((product) => (
              <ProductCard key={product.id} product={product} />
            ))}
          </div>
        )}
      </Container>
    </Section>
  );
}