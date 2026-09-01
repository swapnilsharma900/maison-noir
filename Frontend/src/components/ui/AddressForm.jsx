import { useState, useEffect } from 'react';
import Input from './Input';
import Button from './Button';

const emptyAddress = {
  lineOne: '',
  lineTwo: '',
  landmark: '',
  city: '',
  state: '',
  pincode: '',
  country: 'India',
};

export default function AddressForm({ initialData, onSave, saving = false }) {
  const [form, setForm] = useState(emptyAddress);
  const [error, setError] = useState('');

  useEffect(() => {
    if (initialData) {
      setForm({ ...emptyAddress, ...initialData });
    }
  }, [initialData]);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');
    try {
      await onSave(form);
    } catch (err) {
      setError(err.message || 'Failed to save address');
    }
  };

  return (
    <form onSubmit={handleSubmit} className="space-y-4">
      <Input
        label="Flat / House No."
        name="lineOne"
        value={form.lineOne}
        onChange={handleChange}
        required
        placeholder="Flat 4B, Block C"
      />
      <Input
        label="Building, Society, Street"
        name="lineTwo"
        value={form.lineTwo}
        onChange={handleChange}
        required
        placeholder="Park Avenue Society, MG Road"
      />
      <Input
        label="Landmark (optional)"
        name="landmark"
        value={form.landmark}
        onChange={handleChange}
        placeholder="Near City Center Mall"
      />
      <div className="grid grid-cols-2 gap-4">
        <Input
          label="City"
          name="city"
          value={form.city}
          onChange={handleChange}
          required
        />
        <Input
          label="State"
          name="state"
          value={form.state}
          onChange={handleChange}
          required
        />
      </div>
      <div className="grid grid-cols-2 gap-4">
        <Input
          label="Pincode"
          name="pincode"
          value={form.pincode}
          onChange={handleChange}
          required
          maxLength={6}
          pattern="\d{6}"
          placeholder="400001"
        />
        <Input
          label="Country"
          name="country"
          value={form.country}
          onChange={handleChange}
          required
        />
      </div>

      {error && <p className="text-sm text-[var(--color-error)]">{error}</p>}

      <Button type="submit" disabled={saving}>
        {saving ? 'Saving...' : 'Save Address'}
      </Button>
    </form>
  );
}