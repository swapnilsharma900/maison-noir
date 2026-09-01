import { createContext, useState, useCallback, useContext, useMemo } from 'react';
import { cartService } from '../services/cart';
import { AuthContext } from './AuthContext';

export const CartContext = createContext(null);

export function CartProvider({ children }) {
  const { isAuthenticated } = useContext(AuthContext);
  const [cart, setCart] = useState(null);
  const [loading, setLoading] = useState(false);

  const fetchCart = useCallback(async () => {
    if (!isAuthenticated) return;
    setLoading(true);
    try {
      const data = await cartService.get();
      setCart(data);
    } catch {
      setCart(null);
    } finally {
      setLoading(false);
    }
  }, [isAuthenticated]);

  const addItem = async (variantId, quantity) => {
    const data = await cartService.addItem(variantId, quantity);
    setCart(data);
    return data;
  };

  const updateItem = async (cartItemId, quantity) => {
    const data = await cartService.updateItem(cartItemId, quantity);
    setCart(data);
    return data;
  };

  const removeItem = async (cartItemId) => {
    const data = await cartService.removeItem(cartItemId);
    setCart(data);
    return data;
  };

  const clearCart = async () => {
    await cartService.clear();
    setCart(null);
  };

  const itemCount = useMemo(() => {
    if (!cart?.items) return 0;
    return cart.items.reduce((sum, item) => sum + item.quantity, 0);
  }, [cart]);

  return (
    <CartContext.Provider
      value={{ cart, itemCount, loading, fetchCart, addItem, updateItem, removeItem, clearCart }}
    >
      {children}
    </CartContext.Provider>
  );
}