import { fileURLToPath, URL } from 'node:url'
import { defineConfig } from 'vite'
import Vue from '@vitejs/plugin-vue'
import Vuetify from 'vite-plugin-vuetify'
import packageVersion from 'vite-plugin-package-version'

// https://vitejs.dev/config/
export default defineConfig({
  plugins: [
    {
      name: 'build-html',
      apply: 'build',
      transformIndexHtml: (html) => {
        return {
          html,
          tags: [
            {
              tag: 'script',
              attrs: {
                src: '/env.js',
              },
              injectTo: 'head',
            },
          ],
        }
      },
    },
    Vue(),
    packageVersion(),
    Vuetify({
      autoImport: true,
    }),
  ],
  resolve: {
    alias: {
      '@': fileURLToPath(new URL('./src', import.meta.url)),
    },
  },
})
