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

const temps = Array(33).fill(16).map((item, i) => item + i * 0.5)

defineProps({
  modelValue: {
    type: Number,
    default: 25
  }
})

defineEmit(['update:modelValue'])

const columns = temps.map(i => ({ text: i.toFixed(1), value: i }))
</script>

<style>
.van-picker {
  position: relative;
  user-select: none;
}
.van-picker__columns {
  position: relative;
  display: flex;
  cursor: grab;
}
/* .van-picker__columns::after {
  content: '';
  position: absolute;
  top: 0.5rem;
  left: 0.5rem;
  right: 0.5rem;
  bottom: 0;
  z-index: -1;
  background: center/contain no-repeat url(../assets/temp-bg.png);
  opacity: 0.8;
} */
.van-picker__frame {
  position: absolute;
  top: 50%;
  right: 1rem;
  left: 1rem;
  z-index: 2;
  border: 0.25rem solid rgba(98, 222, 250, 1);
  border-radius: 0.5rem;
  box-shadow: 0 0 0.5rem rgba(98, 222, 250, 0.5);
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
  background-image: linear-gradient(180deg, hsla(0, 0%, 0%, 0.5), hsla(0, 0%, 0%, 0)),
    linear-gradient(0deg, hsla(0, 0%, 0%, 0.5), hsla(0, 0%, 0%, 0));
  background-repeat: no-repeat;
  background-position: top, bottom;
  transform: translateZ(0);
  pointer-events: none;
}
.van-picker-column {
  flex: 1;
  overflow: hidden;
  font-size: 1.75rem;
  color: #62defa;
  text-shadow: 0 0 0.5rem rgba(98, 222, 250, 0.8);
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
