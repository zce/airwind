<template>
  <Picker
    :columns="columns"
    :defaultIndex="temps.indexOf(modelValue)"
    :visibleItemCount="7"
    :itemHeight="75"
    :showToolbar="false"
    @change="i => $emit('update:modelValue', i.value)"
  />
</template>

<script setup>
import Picker from 'vant/es/picker'
import { temps, columns } from '../utils/temperatures'

defineProps({
  modelValue: {
    type: Number,
    default: 25
  }
})

defineEmits(['update:modelValue'])
</script>

<style>
.van-picker {
  position: relative;
}
.van-picker__columns {
  position: relative;
  display: flex;
  cursor: grab;
}
.van-picker__frame {
  position: absolute;
  top: 50%;
  right: 1rem;
  left: 1rem;
  z-index: 2;
  border: 0.125rem solid var(--primary-color);
  border-radius: 0.4rem;
  transform: translateY(-50%);
  pointer-events: none;
}
.van-picker-column {
  flex: 1;
  overflow: hidden;
  font-size: 1.5rem;
  color: var(--primary-color);
}
.van-picker-column__wrapper {
  margin: 0;
  padding: 0;
  transition-timing-function: cubic-bezier(0.23, 1, 0.68, 1);
}
.van-picker-column__item {
  display: flex;
  align-items: center;
  justify-content: center;
  padding: 0 0.5rem;
  outline: 0;
  transition: font-size 200ms;
}
.van-picker-column__item--selected {
  font-size: 2.25rem;
}
</style>
