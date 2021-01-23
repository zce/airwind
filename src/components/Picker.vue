<template>
  <select v-model="selected" multiple>
    <option v-for="item in data" :key="item">{{ item }}</option>
  </select>
</template>

<script setup>
import { defineProps, reactive } from 'vue'

defineProps({
  data: Array,
  selected: Number
})

const state = reactive({ count: 0 })
</script>
