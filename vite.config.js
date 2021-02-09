import vue from '@vitejs/plugin-vue'

/** @type {import('vite').UserConfig} */
const config = {
  // base: '',
  build: {
    outDir: 'www'
  },
  plugins: [vue()]
}

export default config
