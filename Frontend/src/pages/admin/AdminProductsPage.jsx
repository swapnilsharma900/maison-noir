import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { productsService } from '../../services/products';
import Button from '../../components/ui/Button';
import Spinner from '../../components/ui/Spinner';
import Badge from '../../components/ui/Badge';
import ConfirmDialog from '../../components/admin/ConfirmDialog';
import { toast } from '../../components/ui/Toast';
import { resolveProductImage } from '../../utils/images';

export default function AdminProductsPage() {
  const [products, setProducts] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [deleting, setDeleting] = useState(false);

  const loadProducts = () => {
    setLoading(true);
    productsService
      .getAll()
      .then((data) => setProducts(Array.isArray(data) ? data : []))
      .catch((err) => {
        toast(err.message || 'Failed to load products', 'error');
        setProducts([]);
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadProducts();
  }, []);

  const handleDelete = async () => {
    if (!deleteTarget) return;
    setDeleting(true);
    try {
      await productsService.remove(deleteTarget.id);
      toast('Product deleted', 'success');
      setDeleteTarget(null);
      loadProducts();
    } catch (err) {
      toast(err.message || 'Failed to delete product', 'error');
    } finally {
      setDeleting(false);
    }
  };

  return (
    <div>
      <div className="flex flex-wrap items-center justify-between gap-4 mb-8">
        <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight">
          Products
        </h1>
        <Link to="/admin/products/new">
          <Button>New Product</Button>
        </Link>
      </div>

      {loading ? (
        <div className="flex justify-center py-16">
          <Spinner size="lg" />
        </div>
      ) : products.length === 0 ? (
        <p className="text-sm text-[var(--color-text-muted)] font-light">No products in catalog.</p>
      ) : (
        <div className="overflow-x-auto border border-[var(--color-noir-border)] rounded-lg">
          <table className="w-full text-sm font-light">
            <thead>
              <tr className="border-b border-[var(--color-noir-border)] bg-[var(--color-noir-surface)]">
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Product</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Category</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">SKUs</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Status</th>
                <th className="text-right p-3 text-[var(--color-text-muted)] font-medium">Actions</th>
              </tr>
            </thead>
            <tbody>
              {products.map((product) => (
                <tr
                  key={product.id}
                  className="border-b border-[var(--color-noir-border)] last:border-0 hover:bg-[var(--color-noir-elevated)]/50"
                >
                  <td className="p-3">
                    <div className="flex items-center gap-3">
                      <img
                        src={resolveProductImage(product.images?.[0])}
                        alt=""
                        className="w-10 h-12 object-cover rounded bg-[var(--color-noir-elevated)]"
                      />
                      <span className="text-[var(--color-text-heading)]">{product.name}</span>
                    </div>
                  </td>
                  <td className="p-3 text-[var(--color-text-muted)]">{product.category}</td>
                  <td className="p-3 text-[var(--color-text-muted)]">
                    {product.variantItems?.length ?? 0}
                  </td>
                  <td className="p-3">
                    <Badge variant={product.isActive !== false ? 'success' : 'default'}>
                      {product.isActive !== false ? 'Active' : 'Inactive'}
                    </Badge>
                  </td>
                  <td className="p-3 text-right space-x-2">
                    <Link to={`/admin/products/${product.id}/edit`}>
                      <Button variant="outline" size="sm">
                        Edit
                      </Button>
                    </Link>
                    <Button
                      variant="ghost"
                      size="sm"
                      className="!text-[var(--color-error)]"
                      onClick={() => setDeleteTarget(product)}
                    >
                      Delete
                    </Button>
                  </td>
                </tr>
              ))}
            </tbody>
          </table>
        </div>
      )}

      <ConfirmDialog
        open={!!deleteTarget}
        onClose={() => setDeleteTarget(null)}
        onConfirm={handleDelete}
        title="Delete product"
        message={`Permanently delete "${deleteTarget?.name}"? This cannot be undone.`}
        confirmLabel="Delete"
        loading={deleting}
        danger
      />
    </div>
  );
}
