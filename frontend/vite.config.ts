import { defineConfig } from 'vite'
import react from '@vitejs/plugin-react'

// https://vite.dev/config/
export default defineConfig({
  plugins: [react()],
  base: '/',
  envPrefix: 'VITE_',
  build: {
    // куда складывать результат сборки
    outDir: 'dist',
    // перед сборкой очищать папку, чтобы не было старых файлов
    emptyOutDir: true,
    // опционально: включить source map для prod-билда
    sourcemap: false,
  },
  server: {
    proxy: {
      '/api': 'http://localhost:8080',
    },
  },
})