import { useState, useEffect } from 'react';
import { addressService } from '../services/address';
import Container from '../components/ui/Container';
import AddressForm from '../components/ui/AddressForm';
import Spinner from '../components/ui/Spinner';
import { toast } from '../components/ui/Toast';

export default function AddressPage() {
  const [address, setAddress] = useState(null);
  const [loading, setLoading] = useState(true);
  const [saving, setSaving] = useState(false);

  useEffect(() => {
    addressService
      .get()
      .then((data) => setAddress(data))
      .catch(() => setAddress(null))
      .finally(() => setLoading(false));
  }, []);

  const handleSave = async (data) => {
    setSaving(true);
    try {
      const saved = address
        ? await addressService.update(data)
        : await addressService.create(data);
      setAddress(saved);
      toast('Address saved successfully', 'success');
    } catch (err) {
      toast(err.message || 'Failed to save address', 'error');
      throw err;
    } finally {
      setSaving(false);
    }
  };

  return (
    <Container className="py-8 lg:py-12 max-w-lg mx-auto">
      <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight mb-8">
        Shipping Address
      </h1>

      <div className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-xl p-6 lg:p-8">
        {loading ? (
          <div className="flex justify-center py-8">
            <Spinner />
          </div>
        ) : (
          <AddressForm
            initialData={address}
            onSave={handleSave}
            saving={saving}
          />
        )}
      </div>
    </Container>
  );
}