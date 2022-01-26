<template>
  <div class="switch">
    <button v-for="item in actions" :key="item.value" :class="{ active: modelValue === item.value }" @click="$emit('update:modelValue', item.value)">
      {{ item.text }}
    </button>
  </div>
</template>

<script setup>
const props = defineProps({
  modelValue: {
    type: String,
    default: 'manual'
  },
  align: {
    type: String
  }
})

defineEmits(['update:modelValue'])

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
  margin: 0.8rem;
  padding: 0.75rem 1.75rem;
  border: 0;
  border-radius: 0.25rem;
  background: var(--primary-color-alpha);
  color: var(--primary-color);
  font-weight: 500;
  outline: 0;
  transition: background 300ms, color 300ms;
}
button.active {
  background: var(--primary-color);
  color: #000;
}
</style>
