<template>
  <img class="absolute z-0" src="./assets/background.png" alt="Background">
  <Picker :data="temps" v-model="selected"/>
</template>

<script setup>
// 2560x1600
import { ref } from 'vue'
import Picker from './components/Picker.vue'
const temps = Array(33).fill(16).map((item, i) => item + i * 0.5)
const selected = ref(24.5)
</script>
