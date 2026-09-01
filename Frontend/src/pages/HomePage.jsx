import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { SITE } from '../config/site';
import { productsService } from '../services/products';
import Container from '../components/ui/Container';
import Section from '../components/ui/Section';
import Button from '../components/ui/Button';
import Spinner from '../components/ui/Spinner';
import ProductCard from '../components/ui/ProductCard';
import { toast } from '../components/ui/Toast';

export default function HomePage() {
  const [featured, setFeatured] = useState([]);
  const [loading, setLoading] = useState(true);

  useEffect(() => {
    productsService
      .getAll()
      .then((data) => setFeatured(data?.slice(0, 8) || []))
      .catch((err) => {
        setFeatured([]);
        toast(err.message || 'Failed to load products', 'error');
      })
      .finally(() => setLoading(false));
  }, []);

  return (
    <>
      {/* Hero */}
      <section className="relative h-[80vh] min-h-[500px] flex items-center bg-[var(--color-noir-surface)] overflow-hidden">
        <div className="absolute inset-0 bg-gradient-to-r from-[var(--color-noir-page)] via-[var(--color-noir-page)]/80 to-transparent z-10" />
        <div className="absolute inset-0 bg-[radial-gradient(ellipse_at_top_right,var(--color-gold-dim),transparent_50%)] opacity-20" />
        <Container className="relative z-20">
          <div className="max-w-2xl">
            <p className="text-2xl sm:text-4xl tracking-[0.15em] sm:tracking-[0.3em] uppercase text-[var(--color-gold)] mb-4 font-light">
              {SITE.hero.headline}
            </p>
            <h1 className="text-xl sm:text-2xl font-light tracking-tight text-[var(--color-text-heading)] leading-tight">
              {SITE.hero.subtext}
            </h1>
            <p className="mt-6 text-lg font-light text-[var(--color-text-muted)] max-w-lg">
              {SITE.hero.subtitle}
            </p>
            <div className="mt-8">
              <Link to="/products">
                <Button size="lg">{SITE.hero.cta}</Button>
              </Link>
            </div>
          </div>
        </Container>
      </section>

      {/* Categories */}
      <Section title={SITE.categories[0].headline}>
        <Container>
          <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-6 gap-4">
            {SITE.categories.slice(1).map((cat) => (
              <Link
                key={cat.key}
                to={`/products/category/${encodeURIComponent(cat.key)}`}
                className="group flex flex-col items-center p-6 bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg hover:border-[var(--color-gold)] transition-all duration-300"
              >
                <span className="text-sm font-light text-[var(--color-text-primary)] group-hover:text-[var(--color-gold)] transition-colors">
                  {cat.label}
                </span>
              </Link>
            ))}
          </div>
        </Container>
      </Section>

      {/* Featured Products */}
      <Section
        title={SITE.collection.headline}
        subtitle={SITE.collection.subtext}
      >
        <Container>
          {loading ? (
            <div className="flex justify-center py-12">
              <Spinner size="lg" />
            </div>
          ) : featured.length === 0 ? (
            <p className="text-center text-[var(--color-text-muted)] py-12 font-light max-w-md mx-auto">
              No products in the catalog. Seed MongoDB with{' '}
              <code className="text-[var(--color-gold)] text-xs">
                mongosh --file Backend/src/main/resources/db/mongodb/data.js
              </code>{' '}
              and restart the backend.
            </p>
          ) : (
            <div className="grid grid-cols-2 md:grid-cols-3 lg:grid-cols-4 gap-4 lg:gap-6">
              {featured.map((product) => (
                <ProductCard key={product.id} product={product} />
              ))}
            </div>
          )}
          <div className="text-center mt-10">
            <Link to="/products">
              <Button variant="outline">View All Products</Button>
            </Link>
          </div>
        </Container>
      </Section>

      {/* Brand Story */}
      <section className="py-16 lg:py-24 bg-[var(--color-noir-surface)] border-y border-[var(--color-noir-border)]">
        <Container>
          <div className="max-w-3xl mx-auto text-center">
            <p className="text-sm tracking-[0.3em] uppercase text-[var(--color-gold)] mb-4 font-light">
              {SITE.name}
            </p>
            <h2 className="text-3xl lg:text-4xl font-light text-[var(--color-text-heading)] tracking-tight">
              Crafted for Presence
            </h2>
            <p className="mt-6 text-[var(--color-text-muted)] font-light leading-relaxed">
              {SITE.footer.about}
            </p>
          </div>
        </Container>
      </section>
    </>
  );
}