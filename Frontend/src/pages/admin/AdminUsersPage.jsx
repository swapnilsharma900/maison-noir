import { useState, useEffect } from 'react';
import { Link } from 'react-router-dom';
import { userService } from '../../services/user';
import { useAuth } from '../../hooks/useAuth';
import Spinner from '../../components/ui/Spinner';
import Badge from '../../components/ui/Badge';
import Button from '../../components/ui/Button';
import ConfirmDialog from '../../components/admin/ConfirmDialog';
import { toast } from '../../components/ui/Toast';

export default function AdminUsersPage() {
  const { user: currentUser } = useAuth();
  const [users, setUsers] = useState([]);
  const [loading, setLoading] = useState(true);
  const [deleteTarget, setDeleteTarget] = useState(null);
  const [deleting, setDeleting] = useState(false);

  const loadUsers = () => {
    setLoading(true);
    userService
      .getAll()
      .then((data) => setUsers(Array.isArray(data) ? data : []))
      .catch((err) => {
        toast(err.message || 'Failed to load users', 'error');
        setUsers([]);
      })
      .finally(() => setLoading(false));
  };

  useEffect(() => {
    loadUsers();
  }, []);

  const handleDelete = async () => {
    if (!deleteTarget) return;
    setDeleting(true);
    try {
      await userService.deleteById(deleteTarget.id);
      toast('User deleted', 'success');
      setDeleteTarget(null);
      loadUsers();
    } catch (err) {
      toast(err.message || 'Failed to delete user', 'error');
    } finally {
      setDeleting(false);
    }
  };

  return (
    <div>
      <h1 className="text-2xl lg:text-3xl font-light text-[var(--color-text-heading)] tracking-tight mb-8">
        Users
      </h1>

      {loading ? (
        <div className="flex justify-center py-16">
          <Spinner size="lg" />
        </div>
      ) : users.length === 0 ? (
        <p className="text-sm text-[var(--color-text-muted)] font-light">No users found.</p>
      ) : (
        <div className="overflow-x-auto border border-[var(--color-noir-border)] rounded-lg">
          <table className="w-full text-sm font-light">
            <thead>
              <tr className="border-b border-[var(--color-noir-border)] bg-[var(--color-noir-surface)]">
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Name</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Email</th>
                <th className="text-left p-3 text-[var(--color-text-muted)] font-medium">Role</th>
                <th className="text-right p-3 text-[var(--color-text-muted)] font-medium">Actions</th>
              </tr>
            </thead>
            <tbody>
              {users.map((u) => {
                const isSelf = String(u.id) === String(currentUser?.id);
                return (
                  <tr
                    key={u.id}
                    className="border-b border-[var(--color-noir-border)] last:border-0 hover:bg-[var(--color-noir-elevated)]/50"
                  >
                    <td className="p-3 text-[var(--color-text-heading)]">
                      {u.firstName} {u.lastName}
                      {isSelf && (
                        <span className="ml-2 text-xs text-[var(--color-text-muted)]">(you)</span>
                      )}
                    </td>
                    <td className="p-3 text-[var(--color-text-muted)]">{u.email}</td>
                    <td className="p-3">
                      <Badge variant={u.role === 'ADMIN' ? 'gold' : 'default'}>
                        {u.role}
                      </Badge>
                    </td>
                    <td className="p-3 text-right space-x-2">
                      <Link to={`/admin/users/${u.id}`}>
                        <Button variant="outline" size="sm">
                          View
                        </Button>
                      </Link>
                      <Button
                        variant="ghost"
                        size="sm"
                        className="!text-[var(--color-error)]"
                        disabled={isSelf}
                        title={isSelf ? 'You cannot delete your own account' : undefined}
                        onClick={() => !isSelf && setDeleteTarget(u)}
                      >
                        Delete
                      </Button>
                    </td>
                  </tr>
                );
              })}
            </tbody>
          </table>
        </div>
      )}

      <ConfirmDialog
        open={!!deleteTarget}
        onClose={() => setDeleteTarget(null)}
        onConfirm={handleDelete}
        title="Delete user"
        message={`Permanently delete ${deleteTarget?.firstName} ${deleteTarget?.lastName} (${deleteTarget?.email})?`}
        confirmLabel="Delete"
        loading={deleting}
        danger
      />
    </div>
  );
}
