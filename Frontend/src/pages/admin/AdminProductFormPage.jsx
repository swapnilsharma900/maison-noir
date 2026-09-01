import { useState, useEffect } from 'react';
import { useNavigate, useParams, Link } from 'react-router-dom';
import { productsService } from '../../services/products';
import ProductForm from '../../components/admin/ProductForm';
import Spinner from '../../components/ui/Spinner';
import Breadcrumb from '../../components/ui/Breadcrumb';
import { toast } from '../../components/ui/Toast';

export default function AdminProductFormPage() {
  const { productId } = useParams();
  const isEdit = Boolean(productId);
  const navigate = useNavigate();
  const [product, setProduct] = useState(null);
  const [loading, setLoading] = useState(isEdit);
  const [submitting, setSubmitting] = useState(false);

  useEffect(() => {
    if (!isEdit) return;
    productsService
      .getById(productId)
      .then(setProduct)
      .catch((err) => {
        toast(err.message || 'Product not found', 'error');
        setProduct(null);
      })
      .finally(() => setLoading(false));
  }, [isEdit, productId]);

  const handleSubmit = async (payload) => {
    setSubmitting(true);
    try {
      if (isEdit) {
        await productsService.update(productId, payload);
        toast('Product updated', 'success');
      } else {
        await productsService.create(payload);
        toast('Product created', 'success');
      }
      navigate('/admin/products');
    } catch (err) {
      toast(err.message || 'Failed to save product', 'error');
    } finally {
      setSubmitting(false);
    }
  };

  if (loading) {
    return (
      <div className="flex justify-center py-20">
        <Spinner size="lg" />
      </div>
    );
  }

  if (isEdit && !product) {
    return (
      <div className="text-center py-20">
        <p className="text-[var(--color-text-muted)] mb-4">Product not found.</p>
        <Link to="/admin/products" className="text-[var(--color-gold)] text-sm">
          Back to products
        </Link>
      </div>
    );
  }

  return (
    <div>
      <Breadcrumb
        items={[
          { label: 'Admin', href: '/admin' },
          { label: 'Products', href: '/admin/products' },
          { label: isEdit ? 'Edit' : 'New', href: '#' },
        ]}
        className="mb-6"
      />
      <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight mb-8">
        {isEdit ? `Edit: ${product.name}` : 'New Product'}
      </h1>
      <ProductForm
        key={product?.id || 'new'}
        initial={product}
        onSubmit={handleSubmit}
        submitting={submitting}
        submitLabel={isEdit ? 'Update Product' : 'Create Product'}
      />
    </div>
  );
}
