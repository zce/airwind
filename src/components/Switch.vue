<template>
  <div class="switch">
    <button v-for="item in actions" :key="item.value" :class="{ active: modelValue === item.value }" @click="$emit('update:modelValue', item.value)">
      {{ item.text }}
    </button>
  </div>
</template>

<script setup>
import { defineEmit, defineProps } from 'vue'

const props = defineProps({
  modelValue: {
    type: String,
    default: 'manual'
  },
  align: {
    type: String
  }
})

defineEmit(['update:modelValue'])

const actions = [
  { value: 'focus', text: '聚焦' },
  { value: 'avoid', text: '避脸' },
  { value: 'sweep', text: '左右扫风' },
  { value: 'manual', text: '手动' }
]

props.align === 'right' && actions.reverse()
</script>

<style>
button {
  margin: 0.5rem;
  padding: 0.75rem 1.75rem;
  border: 0.125rem solid var(--primary-color);
  border-radius: 2rem;
  background: var(--primary-color-alpha);
  color: var(--primary-color);
  font-size: 1.25rem;
  font-weight: 500;
  box-shadow: 0 0 0.5rem var(--primary-color-alpha);
  outline: 0;
  transition: background 300ms, color 300ms;
}
button.active {
  background: var(--primary-color);
  color: #fff;
}
</style>
