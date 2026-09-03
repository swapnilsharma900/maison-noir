/* eslint-env node */
import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'
import tailwindcss from '@tailwindcss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    react(),
    tailwindcss()
  ],
  
  // Base path for production - MUST be '/' for Render
  base: '/',
  
  server: {
    port: 3000,
    proxy: {
      '/api': {
        target: process.env.VITE_API_URL || 'http://localhost:8080',
        changeOrigin: true,
        secure: false,
      }
    }
  },
  
  build: {
    outDir: 'dist',
    sourcemap: false, // ✅ Set to false for production (smaller files)
    minify: 'terser',
    terserOptions: {
      compress: {
        drop_console: true,   // Remove console.log in production
        drop_debugger: true,  // Remove debugger statements
      },
    },
    rollupOptions: {
      output: {
        // Better caching by splitting vendor chunks
        manualChunks: {
          'react-vendor': ['react', 'react-dom'],
          'ui-vendor': ['@mui/material', '@emotion/react', '@emotion/styled'],
          'router-vendor': ['react-router-dom'],
        },
        // Cleaner file names
        chunkFileNames: 'assets/[name]-[hash].js',
        entryFileNames: 'assets/[name]-[hash].js',
        assetFileNames: 'assets/[name]-[hash].[ext]',
      },
    },
    // Increase warning limit if needed
    chunkSizeWarningLimit: 1000,
    // Empty outDir before building
    emptyOutDir: true,
  },
  
  // Preview server config (for local testing of production build)
  preview: {
    port: 3000,
  },
})
