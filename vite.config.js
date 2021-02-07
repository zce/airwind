import vue from '@vitejs/plugin-vue'
import legacy from '@vitejs/plugin-legacy'

/** @type {import('vite').UserConfig} */
export default {
  base: '',
  plugins: [vue(), legacy({ targets: 'IE 11' })]
}
