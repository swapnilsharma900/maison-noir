export const SITE = {
  name: 'Maison Noir',
  tagline: 'The House of Vigour',
  description: 'Luxury menswear for the modern gentleman.',

  hero: {
    headline: 'NOIR & BLANC',
    subtext: "Découvrez l'élégance intemporelle. Chaque pièce incarne la sophistication française.",
    subtitle: 'Curated fashion for men who lead.',
    cta: 'Explore Collection',
  },

  collection: {
    headline: 'Elevate Your Style',
    subtext: 'Timeless pieces crafted with precision and care.'
  },

  categories: [
    { headline: 'Redefine Your Presence' },
    { key: 'T-Shirts', label: 'T-Shirts' },
    { key: 'Hoodies', label: 'Hoodies' },
    { key: 'Joggers', label: 'Joggers' },
    { key: 'Jackets', label: 'Jackets' },
    { key: 'Accessories', label: 'Accessories' },
  ],

  nav: [
    { label: 'Home', href: '/' },
    { label: 'Shop', href: '/products' },
    { label: 'New Arrivals', href: '/products' },
  ],

  footer: {
    about:
      'Maison Noir is a luxury men’s fashion house dedicated to timeless elegance and modern sophistication. Every piece is crafted for the man who commands presence.',
    columns: [
      {
        title: 'Shop',
        links: [
          { label: 'All Products', href: '/products' },
          { label: 'T-Shirts', href: '/products/category/T-Shirts' },
          { label: 'Hoodies', href: '/products/category/Hoodies' },
          { label: 'Jackets', href: '/products/category/Jackets' },
        ],
      },
      {
        title: 'Support',
        links: [
          { label: 'Contact Us', href: '#' },
          { label: 'Shipping & Returns', href: '#' },
          { label: 'Size Guide', href: '#' },
          { label: 'FAQ', href: '#' },
        ],
      },
      {
        title: 'Company',
        links: [
          { label: 'About Us', href: '#' },
          { label: 'Careers', href: '#' },
          { label: 'Privacy Policy', href: '#' },
          { label: 'Terms of Service', href: '#' },
        ],
      },
    ],
    copyright: `© ${new Date().getFullYear()} MAISON NOIR. L'ÉLÉGANCE FRANÇAISE.`
  },

  paymentMethods: ['COD', 'CARD', 'UPI'],

  orderStatusLabels: {
    PENDING: 'Pending',
    CONFIRMED: 'Confirmed',
    SHIPPED: 'Shipped',
    DELIVERED: 'Delivered',
    CANCELLED: 'Cancelled',
    RETURNED: 'Returned',
  },

  paymentStatusLabels: {
    PENDING: 'Pending',
    PAID: 'Paid',
    FAILED: 'Failed',
  },
};