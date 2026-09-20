const BASE_URL = import.meta.env.VITE_API_URL || 'http://localhost:8080';

async function request(endpoint, options = {}) {
  const token = localStorage.getItem('token');

  const isFormData = options.body instanceof FormData;

  const headers = {
    // Only set JSON content-type when we're not sending FormData.
    // For FormData, the browser sets Content-Type with the correct boundary.
    ...(isFormData ? {} : { 'Content-Type': 'application/json' }),
    ...options.headers,
  };

  if (token) {
    headers['Authorization'] = `Bearer ${token}`;
  }

  let response;
  try {
    response = await fetch(`${BASE_URL}${endpoint}`, {
      ...options,
      headers,
    });
  } catch {
    throw new Error(
        `Cannot reach the server. Start the backend on ${BASE_URL} and ensure MySQL/MongoDB are running.`
    );
  }

  if (response.status === 401) {
    localStorage.removeItem('token');
    localStorage.removeItem('user');
    window.location.href = '/login';
    throw new Error('Session expired. Please login again.');
  }

  let json;
  try {
    json = await response.json();
  } catch {
    throw new Error(`Invalid response from server (${response.status})`);
  }

  if (!response.ok) {
    const message = json.message || `Request failed with status ${response.status}`;
    throw new Error(message);
  }

  if (json.success === false) {
    throw new Error(json.message || 'Request failed');
  }

  // Returns the unwrapped `data` field from the ApiResponse wrapper.
  return json.data;
}

export const api = {
  get: (endpoint) => request(endpoint),
  post: (endpoint, body) => request(endpoint, { method: 'POST', body: JSON.stringify(body) }),
  put: (endpoint, body) => request(endpoint, { method: 'PUT', body: JSON.stringify(body) }),
  patch: (endpoint, body) => request(endpoint, { method: 'PATCH', body: JSON.stringify(body) }),
  delete: (endpoint) => request(endpoint, { method: 'DELETE' }),

  /**
   * Upload a file (or FormData) to the server.
   * Do NOT set Content-Type manually — the browser will set it with the boundary.
   *
   * @param {string} endpoint - API endpoint (e.g., '/api/admin/products/upload-image')
   * @param {FormData} formData - FormData containing the file(s)
   */
  upload: (endpoint, formData) => request(endpoint, { method: 'POST', body: formData }),
};