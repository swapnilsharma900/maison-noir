import { useState } from 'react';
import { Link } from 'react-router-dom';
import Container from '../components/ui/Container';
import Button from '../components/ui/Button';
import Input from '../components/ui/Input';

export default function AuthForm({ mode, onSubmit }) {
  const isLogin = mode === 'login';
  const [form, setForm] = useState({
    firstName: '',
    lastName: '',
    email: '',
    phone: '',
    password: '',
    confirmPassword: '',
  });
  const [error, setError] = useState('');
  const [loading, setLoading] = useState(false);

  const handleChange = (e) => {
    setForm({ ...form, [e.target.name]: e.target.value });
  };

  const handleSubmit = async (e) => {
    e.preventDefault();
    setError('');

    if (!isLogin && form.password !== form.confirmPassword) {
      setError('Passwords do not match');
      return;
    }

    setLoading(true);
    try {
      await onSubmit(isLogin ? { email: form.email, password: form.password } : form);
    } catch (err) {
      setError(err.message || 'Something went wrong');
    } finally {
      setLoading(false);
    }
  };

  return (
    <Container className="min-h-[80vh] flex items-center justify-center py-16">
      <div className="w-full max-w-md">
        <div className="text-center mb-8">
          <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight">
            {isLogin ? 'Welcome Back' : 'Create Account'}
          </h1>
          <p className="mt-2 text-[var(--color-text-muted)] font-light text-sm">
            {isLogin
              ? 'Sign in to access your account.'
              : 'Join Maison Noir and elevate your wardrobe.'}
          </p>
        </div>

        <form
          onSubmit={handleSubmit}
          className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-xl p-6 lg:p-8"
        >
          {!isLogin && (
            <div className="grid grid-cols-2 gap-4 mb-4">
              <Input
                label="First Name"
                name="firstName"
                value={form.firstName}
                onChange={handleChange}
                required
              />
              <Input
                label="Last Name"
                name="lastName"
                value={form.lastName}
                onChange={handleChange}
                required
              />
            </div>
          )}

          <div className="mb-4">
            <Input
              label="Email"
              type="email"
              name="email"
              value={form.email}
              onChange={handleChange}
              required
              autoComplete="email"
            />
          </div>

          {!isLogin && (
            <div className="mb-4">
              <Input
                label="Phone (optional)"
                type="tel"
                name="phone"
                value={form.phone}
                onChange={handleChange}
              />
            </div>
          )}

          <div className="mb-4">
            <Input
              label="Password"
              type="password"
              name="password"
              value={form.password}
              onChange={handleChange}
              required
              autoComplete={isLogin ? 'current-password' : 'new-password'}
            />
          </div>

          {!isLogin && (
            <div className="mb-6">
              <Input
                label="Confirm Password"
                type="password"
                name="confirmPassword"
                value={form.confirmPassword}
                onChange={handleChange}
                required
              />
            </div>
          )}

          {error && (
            <p className="mb-4 text-sm text-[var(--color-error)]">{error}</p>
          )}

          <Button type="submit" className="w-full" size="lg" disabled={loading}>
            {loading ? 'Please wait...' : isLogin ? 'Sign In' : 'Create Account'}
          </Button>

          <p className="mt-6 text-center text-sm font-light text-[var(--color-text-muted)]">
            {isLogin ? "Don't have an account? " : 'Already have an account? '}
            <Link
              to={isLogin ? '/register' : '/login'}
              className="text-[var(--color-gold)] hover:text-[var(--color-gold-hover)] transition-colors"
            >
              {isLogin ? 'Sign Up' : 'Sign In'}
            </Link>
          </p>
        </form>
      </div>
    </Container>
  );
}