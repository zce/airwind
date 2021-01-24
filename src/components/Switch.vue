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
.switch {
  padding: 0 3rem;
}
button {
  margin: 0.5rem;
  padding: 0.75rem 1.5rem;
  border: 1px solid rgba(98, 222, 250, 0.4);
  border-radius: 0.25rem;
  background: rgba(98, 222, 250, 0.2);
  color: #62defa;
  font-size: 1.5rem;
  box-shadow: 0 0 0.5rem rgba(98, 222, 250, 0.5);
  outline: 0;
  transition: all 300ms;
}
button:focus, button.active {
  border-color: rgba(98, 222, 250, 0.8);
  background: rgba(98, 222, 250, 0.6);
  color: #fff;
}
</style>
