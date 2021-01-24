<template>
  <!-- <img class="absolute z-0" src="./assets/background.png" alt="Background"> -->
  <h1>{{ selected }}</h1>
  <Picker :options="temps" v-model="selected" :size="7" />
</template>

<script setup>
// 2560x1600
import { ref } from 'vue'
import Picker from './components/Picker.vue'
const temps = Array(33).fill(16).map((item, i) => item + i * 0.5)
const selected = ref(24.5)
</script>

<style>
@font-face {
	font-family: 'DSDIGI';
	src: url('./assets/DSDIGI.eot');
	src: local('DSDIGI'), url('./assets/DSDIGI.woff') format('woff'), url('./assets/DSDIGI.ttf') format('truetype');
}
html {
  overflow: hidden;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
body {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0;
  height: 100vh;
}
h1 {
  width: 400px;
}
</style>
