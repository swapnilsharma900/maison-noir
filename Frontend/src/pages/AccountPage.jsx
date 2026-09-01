import { useState } from 'react';
import { useAuth } from '../hooks/useAuth';
import Container from '../components/ui/Container';
import Input from '../components/ui/Input';
import Button from '../components/ui/Button';
import { toast } from '../components/ui/Toast';
import { userService } from '../services/user';

export default function AccountPage() {
  const { user, updateProfile } = useAuth();
  const [activeTab, setActiveTab] = useState('profile');

  const [profileForm, setProfileForm] = useState({
    firstName: user?.firstName || '',
    lastName: user?.lastName || '',
    email: user?.email || '',
    phone: user?.phone || '',
  });
  const [profileLoading, setProfileLoading] = useState(false);
  const [profileError, setProfileError] = useState('');

  const [passwordForm, setPasswordForm] = useState({
    oldPassword: '',
    newPassword: '',
    confirmPassword: '',
  });
  const [passwordLoading, setPasswordLoading] = useState(false);
  const [passwordError, setPasswordError] = useState('');

  const handleProfileChange = (e) => {
    setProfileForm({ ...profileForm, [e.target.name]: e.target.value });
  };

  const handleProfileSubmit = async (e) => {
    e.preventDefault();
    setProfileError('');
    setProfileLoading(true);
    try {
      await updateProfile(profileForm);
      toast('Profile updated successfully', 'success');
    } catch (err) {
      setProfileError(err.message || 'Failed to update profile');
    } finally {
      setProfileLoading(false);
    }
  };

  const handlePasswordChange = (e) => {
    setPasswordForm({ ...passwordForm, [e.target.name]: e.target.value });
  };

  const handlePasswordSubmit = async (e) => {
    e.preventDefault();
    setPasswordError('');
    if (passwordForm.newPassword !== passwordForm.confirmPassword) {
      setPasswordError('New passwords do not match');
      return;
    }
    setPasswordLoading(true);
    try {
      await userService.updatePassword(passwordForm);
      setPasswordForm({ oldPassword: '', newPassword: '', confirmPassword: '' });
      toast('Password updated successfully', 'success');
    } catch (err) {
      setPasswordError(err.message || 'Failed to update password');
    } finally {
      setPasswordLoading(false);
    }
  };

  const tabs = [
    { key: 'profile', label: 'Profile' },
    { key: 'password', label: 'Password' },
  ];

  return (
    <Container className="py-8 lg:py-12 max-w-lg mx-auto">
      <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight mb-8">
        My Account
      </h1>

      {/* Tabs */}
      <div className="flex gap-1 bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-lg p-1 mb-8">
        {tabs.map((tab) => (
          <button
            key={tab.key}
            onClick={() => setActiveTab(tab.key)}
            className={`flex-1 py-2 text-sm font-light rounded-md transition-colors cursor-pointer ${
              activeTab === tab.key
                ? 'bg-[var(--color-gold)] text-[var(--color-noir-page)]'
                : 'text-[var(--color-text-muted)] hover:text-[var(--color-text-primary)]'
            }`}
          >
            {tab.label}
          </button>
        ))}
      </div>

      {/* Profile Form */}
      {activeTab === 'profile' && (
        <form
          onSubmit={handleProfileSubmit}
          className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-xl p-6 lg:p-8 space-y-4"
        >
          <div className="grid grid-cols-2 gap-4">
            <Input
              label="First Name"
              name="firstName"
              value={profileForm.firstName}
              onChange={handleProfileChange}
            />
            <Input
              label="Last Name"
              name="lastName"
              value={profileForm.lastName}
              onChange={handleProfileChange}
            />
          </div>
          <Input
            label="Email"
            type="email"
            name="email"
            value={profileForm.email}
            onChange={handleProfileChange}
          />
          <Input
            label="Phone"
            type="tel"
            name="phone"
            value={profileForm.phone}
            onChange={handleProfileChange}
          />

          {profileError && (
            <p className="text-sm text-[var(--color-error)]">{profileError}</p>
          )}

          <Button type="submit" disabled={profileLoading}>
            {profileLoading ? 'Saving...' : 'Save Changes'}
          </Button>
        </form>
      )}

      {/* Password Form */}
      {activeTab === 'password' && (
        <form
          onSubmit={handlePasswordSubmit}
          className="bg-[var(--color-noir-surface)] border border-[var(--color-noir-border)] rounded-xl p-6 lg:p-8 space-y-4"
        >
          <Input
            label="Current Password"
            type="password"
            name="oldPassword"
            value={passwordForm.oldPassword}
            onChange={handlePasswordChange}
            required
          />
          <Input
            label="New Password"
            type="password"
            name="newPassword"
            value={passwordForm.newPassword}
            onChange={handlePasswordChange}
            required
            minLength={8}
          />
          <Input
            label="Confirm New Password"
            type="password"
            name="confirmPassword"
            value={passwordForm.confirmPassword}
            onChange={handlePasswordChange}
            required
          />

          {passwordError && (
            <p className="text-sm text-[var(--color-error)]">{passwordError}</p>
          )}

          <Button type="submit" disabled={passwordLoading}>
            {passwordLoading ? 'Updating...' : 'Change Password'}
          </Button>
        </form>
      )}
    </Container>
  );
}