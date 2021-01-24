<template>
  <select :value="modelValue" @change="update" :size="size" multiple>
    <option v-for="item in data" :key="item" :value="item">{{ item.toFixed(1) }}</option>
  </select>
</template>

<script setup>
import { defineProps, reactive, defineEmit, nextTick } from 'vue'

const props = defineProps({
  data: Array,
  size: {
    type: Number,
    default: 5,
    validator: i => {
      if (i % 2 === 0) {
        console.warn('Picker size should be odd')
      }
      return true
    }
  },
  modelValue: Number
})

const emit = defineEmit(['update:modelValue'])

const state = reactive({ count: 0 })

/** @param {Event} e */
const update = e => {
  /** @type {HTMLSelectElement} */
  const el = e.target

  emit('update:modelValue', ~~el.value)

  nextTick(() => {
    el.scrollTop = (el.selectedIndex - Math.floor(props.size / 2)) * el[0].clientHeight
    console.log(el.scrollTop, el.scrollHeight)
  })
}
</script>

<style scoped>
select {
  border: 0;
  font-family: DSDIGI;
  font-size: 2.5rem;
  text-align: center;
  scroll-snap-type: y mandatory;
  -webkit-overflow-scrolling: touch;
  outline: 0;
}
option {
  display: flex;
  justify-content: center;
  align-items: center;
  height: 4rem;
  scroll-snap-align: start;
  transition: font-weight 300ms;
}
option:checked {
  font-weight: 600;
}
::-webkit-scrollbar {
  display: none;
}
</style>
