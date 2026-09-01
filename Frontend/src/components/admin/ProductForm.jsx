import { useState } from 'react';
import Input from '../ui/Input';
import Button from '../ui/Button';

const emptyVariantOption = () => ({ label: '', values: [''] });
const emptyVariantItem = () => ({
  id: '',
  variantLabel: 'Size',
  name: '',
  image: '',
  price: '',
  category: '',
  stockCount: '0',
  isAvailable: true,
});
const emptyAttribute = () => ({ key: '', value: '' });

export function productToFormState(product) {
  if (!product) {
    return {
      name: '',
      description: '',
      category: '',
      images: [''],
      variants: [emptyVariantOption()],
      attributes: [emptyAttribute()],
      variantItems: [emptyVariantItem()],
    };
  }

  const variants =
    product.variants?.length > 0
      ? product.variants.map((v) => ({
          label: v.label || '',
          values: Array.isArray(v.values) && v.values.length ? [...v.values] : [''],
        }))
      : [emptyVariantOption()];

  const attributes =
    product.attributes && Object.keys(product.attributes).length > 0
      ? Object.entries(product.attributes).map(([key, value]) => ({
          key,
          value: typeof value === 'string' ? value : JSON.stringify(value),
        }))
      : [emptyAttribute()];

  return {
    name: product.name || '',
    description: product.description || '',
    category: product.category || '',
    images: product.images?.length ? [...product.images] : [''],
    variants,
    attributes,
    variantItems:
      product.variantItems?.length > 0
        ? product.variantItems.map((item) => ({
            id: item.id || '',
            variantLabel: item.variantLabel || 'Size',
            name: item.name || '',
            image: item.image || '',
            price: item.price != null ? String(item.price) : '',
            category: item.category || product.category || '',
            stockCount: item.stockCount != null ? String(item.stockCount) : '0',
            isAvailable: item.isAvailable !== false,
          }))
        : [emptyVariantItem()],
  };
}

export function formStateToPayload(form) {
  const images = form.images.map((s) => s.trim()).filter(Boolean);
  const variants = form.variants
    .filter((v) => v.label.trim())
    .map((v) => ({
      label: v.label.trim(),
      values: v.values.map((s) => s.trim()).filter(Boolean),
    }));
  const attributes = {};
  form.attributes.forEach(({ key, value }) => {
    const k = key.trim();
    if (k) attributes[k] = value.trim();
  });
  const variantItems = form.variantItems
    .filter((item) => item.name.trim() && item.price !== '')
    .map((item) => {
      return {
        variantLabel: item.variantLabel.trim() || 'Size',
        name: item.name.trim(),
        image: item.image.trim() || images[0] || '',
        price: Number(item.price),
        category: item.category.trim() || form.category.trim(),
        stockCount: Number(item.stockCount) || 0,
        isAvailable: item.isAvailable,
      };
    });

  return {
    name: form.name.trim(),
    description: form.description.trim(),
    category: form.category.trim(),
    images,
    variants,
    attributes,
    variantItems,
  };
}

export default function ProductForm({ initial, onSubmit, submitting, submitLabel }) {
  const [form, setForm] = useState(() => productToFormState(initial));

  const update = (patch) => setForm((prev) => ({ ...prev, ...patch }));

  const handleSubmit = (e) => {
    e.preventDefault();
    onSubmit(formStateToPayload(form));
  };

  const listField = (key, emptyFn, renderRow) => (
    <div className="space-y-3">
      {form[key].map((row, index) => (
        <div key={index} className="flex flex-wrap gap-2 items-start">
          {renderRow(row, index)}
          <Button
            type="button"
            variant="ghost"
            size="sm"
            onClick={() => {
              const next = form[key].filter((_, i) => i !== index);
              update({ [key]: next.length ? next : [emptyFn()] });
            }}
          >
            Remove
          </Button>
        </div>
      ))}
      <Button
        type="button"
        variant="outline"
        size="sm"
        onClick={() => update({ [key]: [...form[key], emptyFn()] })}
      >
        Add {key === 'images' ? 'Image' : key === 'variantItems' ? 'SKU' : 'Row'}
      </Button>
    </div>
  );

  return (
    <form onSubmit={handleSubmit} className="space-y-10">
      <section className="space-y-4">
        <h2 className="text-lg font-light text-[var(--color-text-heading)]">Basics</h2>
        <Input
          label="Name"
          value={form.name}
          onChange={(e) => update({ name: e.target.value })}
          required
          minLength={3}
        />
        <Input
          label="Description"
          type="textarea"
          rows={4}
          value={form.description}
          onChange={(e) => update({ description: e.target.value })}
          required
          minLength={10}
        />
        <Input
          label="Category"
          value={form.category}
          onChange={(e) => update({ category: e.target.value })}
          required
        />
      </section>

      <section className="space-y-4">
        <h2 className="text-lg font-light text-[var(--color-text-heading)]">Images</h2>
        <p className="text-xs text-[var(--color-text-muted)] font-light">
          Paths like /Data/Images/product_images/example.png
        </p>
        {listField('images', () => '', (url, index) => (
          <Input
            className="flex-1 min-w-[200px]"
            value={url}
            onChange={(e) => {
              const images = [...form.images];
              images[index] = e.target.value;
              update({ images });
            }}
            placeholder="/Data/Images/product_images/..."
          />
        ))}
      </section>

      <section className="space-y-4">
        <h2 className="text-lg font-light text-[var(--color-text-heading)]">Variant options</h2>
        <p className="text-xs text-[var(--color-text-muted)] font-light">
          Display metadata (e.g. Size: S, M, L)
        </p>
        {form.variants.map((variant, vIndex) => (
          <div
            key={vIndex}
            className="p-4 border border-[var(--color-noir-border)] rounded-lg space-y-3"
          >
            <div className="flex gap-2 flex-wrap">
              <Input
                label="Label"
                className="flex-1 min-w-[120px]"
                value={variant.label}
                onChange={(e) => {
                  const variants = [...form.variants];
                  variants[vIndex] = { ...variants[vIndex], label: e.target.value };
                  update({ variants });
                }}
                placeholder="Size"
              />
              <Button
                type="button"
                variant="ghost"
                size="sm"
                className="self-end"
                onClick={() => {
                  const variants = form.variants.filter((_, i) => i !== vIndex);
                  update({ variants: variants.length ? variants : [emptyVariantOption()] });
                }}
              >
                Remove group
              </Button>
            </div>
            {variant.values.map((val, valIndex) => (
              <div key={valIndex} className="flex gap-2">
                <Input
                  className="flex-1"
                  value={val}
                  onChange={(e) => {
                    const variants = [...form.variants];
                    const values = [...variants[vIndex].values];
                    values[valIndex] = e.target.value;
                    variants[vIndex] = { ...variants[vIndex], values };
                    update({ variants });
                  }}
                  placeholder="Value"
                />
                <Button
                  type="button"
                  variant="ghost"
                  size="sm"
                  onClick={() => {
                    const variants = [...form.variants];
                    const values = variants[vIndex].values.filter((_, i) => i !== valIndex);
                    variants[vIndex] = {
                      ...variants[vIndex],
                      values: values.length ? values : [''],
                    };
                    update({ variants });
                  }}
                >
                  ×
                </Button>
              </div>
            ))}
            <Button
              type="button"
              variant="outline"
              size="sm"
              onClick={() => {
                const variants = [...form.variants];
                variants[vIndex] = {
                  ...variants[vIndex],
                  values: [...variants[vIndex].values, ''],
                };
                update({ variants });
              }}
            >
              Add value
            </Button>
          </div>
        ))}
        <Button
          type="button"
          variant="outline"
          size="sm"
          onClick={() => update({ variants: [...form.variants, emptyVariantOption()] })}
        >
          Add variant group
        </Button>
      </section>

      <section className="space-y-4">
        <h2 className="text-lg font-light text-[var(--color-text-heading)]">Attributes</h2>
        {listField('attributes', emptyAttribute, (row, index) => (
          <>
            <Input
              className="w-36"
              value={row.key}
              onChange={(e) => {
                const attributes = [...form.attributes];
                attributes[index] = { ...attributes[index], key: e.target.value };
                update({ attributes });
              }}
              placeholder="material"
            />
            <Input
              className="flex-1 min-w-[160px]"
              value={row.value}
              onChange={(e) => {
                const attributes = [...form.attributes];
                attributes[index] = { ...attributes[index], value: e.target.value };
                update({ attributes });
              }}
              placeholder="100% Cotton"
            />
          </>
        ))}
      </section>

      <section className="space-y-4">
        <h2 className="text-lg font-light text-[var(--color-text-heading)]">SKU variants</h2>
        {form.variantItems.map((item, index) => (
          <div
            key={index}
            className="p-4 border border-[var(--color-noir-border)] rounded-lg grid grid-cols-1 sm:grid-cols-2 gap-3"
          >
            <Input
              label="Variant label"
              value={item.variantLabel}
              onChange={(e) => {
                const variantItems = [...form.variantItems];
                variantItems[index] = { ...variantItems[index], variantLabel: e.target.value };
                update({ variantItems });
              }}
            />
            <Input
              label="SKU name"
              value={item.name}
              onChange={(e) => {
                const variantItems = [...form.variantItems];
                variantItems[index] = { ...variantItems[index], name: e.target.value };
                update({ variantItems });
              }}
              required
            />
            <Input
              label="Price (₹)"
              type="number"
              min="0.01"
              step="0.01"
              value={item.price}
              onChange={(e) => {
                const variantItems = [...form.variantItems];
                variantItems[index] = { ...variantItems[index], price: e.target.value };
                update({ variantItems });
              }}
              required
            />
            <Input
              label="Stock"
              type="number"
              min="0"
              value={item.stockCount}
              onChange={(e) => {
                const variantItems = [...form.variantItems];
                variantItems[index] = { ...variantItems[index], stockCount: e.target.value };
                update({ variantItems });
              }}
            />
            <Input
              label="Category (SKU)"
              value={item.category}
              onChange={(e) => {
                const variantItems = [...form.variantItems];
                variantItems[index] = { ...variantItems[index], category: e.target.value };
                update({ variantItems });
              }}
            />
            <Input
              label="Image URL"
              value={item.image}
              onChange={(e) => {
                const variantItems = [...form.variantItems];
                variantItems[index] = { ...variantItems[index], image: e.target.value };
                update({ variantItems });
              }}
            />
            <label className="flex items-center gap-2 text-sm text-[var(--color-text-muted)] sm:col-span-2">
              <input
                type="checkbox"
                checked={item.isAvailable}
                onChange={(e) => {
                  const variantItems = [...form.variantItems];
                  variantItems[index] = { ...variantItems[index], isAvailable: e.target.checked };
                  update({ variantItems });
                }}
                className="accent-[var(--color-gold)]"
              />
              Available for purchase
            </label>
            <div className="sm:col-span-2">
              <Button
                type="button"
                variant="ghost"
                size="sm"
                onClick={() => {
                  const variantItems = form.variantItems.filter((_, i) => i !== index);
                  update({
                    variantItems: variantItems.length ? variantItems : [emptyVariantItem()],
                  });
                }}
              >
                Remove SKU
              </Button>
            </div>
          </div>
        ))}
        <Button
          type="button"
          variant="outline"
          size="sm"
          onClick={() => update({ variantItems: [...form.variantItems, emptyVariantItem()] })}
        >
          Add SKU
        </Button>
      </section>

      <div className="flex gap-3 pt-4 border-t border-[var(--color-noir-border)]">
        <Button type="submit" disabled={submitting}>
          {submitting ? 'Saving...' : submitLabel}
        </Button>
      </div>
    </form>
  );
}
