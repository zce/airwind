<template>
  <Picker
    :columns="columns"
    :defaultIndex="temps.indexOf(modelValue)"
    :visibleItemCount="7"
    :itemHeight="80"
    :showToolbar="false"
    @change="i => $emit('update:modelValue', i.value)"
  />
</template>

<script setup>
import { defineEmit, defineProps } from 'vue'
import Picker from 'vant/es/picker'
import { temps, columns } from '../utils/temperatures'

defineProps({
  modelValue: {
    type: Number,
    default: 25
  }
})

defineEmit(['update:modelValue'])
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
  border: 0.25rem solid var(--primary-color);
  border-radius: 1rem;
  box-shadow: 0 0 0.5rem var(--primary-color-alpha);
  transform: translateY(-50%);
  pointer-events: none;
}
.van-picker__mask {
  position: absolute;
  top: 0;
  left: 0;
  z-index: 1;
  width: 100%;
  height: 100%;
  background-image: linear-gradient(180deg, rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0)),
    linear-gradient(0deg, rgba(0, 0, 0, 0.6), rgba(0, 0, 0, 0));
  background-repeat: no-repeat;
  background-position: top, bottom;
  transform: translateZ(0);
  pointer-events: none;
}
.van-picker-column {
  flex: 1;
  overflow: hidden;
  font-size: 1.75rem;
  color: var(--primary-color);
  text-shadow: 0 0 0.25rem var(--primary-color-alpha);
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
  font-size: 3rem;
}
</style>
