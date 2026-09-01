import { fileURLToPath, URL } from 'node:url'

import { defineConfig } from 'vite'
import vue from '@vitejs/plugin-vue'
import vueDevTools from 'vite-plugin-vue-devtools'
import UnoCSS from 'unocss/vite'

// https://vite.dev/config/
export default defineConfig({
  plugins: [
    vue(),
    vueDevTools(),
    UnoCSS(),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
  build: {
    emptyOutDir: true,
    outDir: '../backend/backend/src/main/resources/static'
  },
  server: {
    port: 1235,
    proxy: {
      "^/jmeter.*": {
        target: "http://localhost:8080",
      },
      "/api/": {
        target: "http://localhost:8080",
      },
    },
  },
})
