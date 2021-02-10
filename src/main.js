import { createApp } from 'vue'
import App from './App.vue'

createApp(App).mount('#app')

document.addEventListener('deviceready', () => StatusBar.hide(), false)
