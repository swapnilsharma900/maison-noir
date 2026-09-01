/**
 * Resolves product image paths from API/seed data for use in <img src>.
 * Supports ./Data/... (repo-relative seed paths) and absolute /Data/... URLs.
 */
export function resolveProductImage(url) {
  if (!url) return '/vite.svg';
  if (url.startsWith('./')) return url.slice(1);
  return url;
}
