<template>
  <div class="slider">
    <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><path d="M20 17.58A5 5 0 0 0 18 8h-1.26A8 8 0 1 0 4 16.25"></path><line x1="8" y1="16" x2="8.01" y2="16"></line><line x1="8" y1="20" x2="8.01" y2="20"></line><line x1="12" y1="18" x2="12.01" y2="18"></line><line x1="12" y1="22" x2="12.01" y2="22"></line><line x1="16" y1="16" x2="16.01" y2="16"></line><line x1="16" y1="20" x2="16.01" y2="20"></line></svg>
    <Slider :modelValue="modelValue" @update:modelValue="$emit('update:modelValue', $event)" :max="10">
      <template #button>
        <div class="van-slider__button">{{ modelValue }}</div>
      </template>
    </Slider>
    <svg class="icon" viewBox="0 0 24 24" fill="none" stroke="currentColor" stroke-width="2" stroke-linecap="round" stroke-linejoin="round"><line x1="16" y1="13" x2="16" y2="21"></line><line x1="8" y1="13" x2="8" y2="21"></line><line x1="12" y1="15" x2="12" y2="23"></line><path d="M20 16.58A5 5 0 0 0 18 7h-1.26A8 8 0 1 0 4 15.25"></path></svg>
  </div>
</template>

<script setup>
import { defineEmit, defineProps } from 'vue'
import Slider from 'vant/es/slider'

defineProps({
  modelValue: {
    type: Number,
    default: 25
  }
})

defineEmit(['update:modelValue'])
</script>

<style>
.slider {
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 5rem 20rem;
  padding: 1rem;
  border-radius: 2rem;
  background: rgba(0, 0, 0, 0.5);
}
.van-slider {
  position: relative;
  flex: 1 1 0;
  height: 0.5rem;
  background: #fff;
  border-radius: 2rem;
  cursor: pointer;
  user-select: none;
}
.van-slider::before {
  position: absolute;
  top: -0.5rem;
  right: 0;
  bottom: -0.5rem;
  left: 0;
  content: '';
}
.van-slider__bar {
  position: relative;
  width: 100%;
  height: 100%;
  background: var(--primary-color);
  box-shadow: 0 0 0.5rem var(--primary-color-alpha);
  border-radius: inherit;
  transition: all 0.2s;
}
.van-slider__button {
  width: 1.75rem;
  height: 1.75rem;
  background: var(--primary-color);
  line-height: 3;
  text-align: center;
  color: var(--primary-color);
  font-size: 2rem;
  border-radius: 50%;
  box-shadow: 0 1px 2px rgba(0, 0, 0, 0.5), 0 0 0.5rem var(--primary-color-alpha);
}
.van-slider__button-wrapper{
  position: absolute;
  top: 50%;
  right: 0;
  transform: translate3d(50%, -50%, 0);
  cursor: grab;
  outline: 0;
}
.van-slider__button-wrapper-left {
  position: absolute;
  top: 50%;
  left: 0;
  transform: translate3d(-50%, -50%, 0);
  cursor: grab;
}
.icon {
  margin: 0 1rem;
  width: 1.75rem;
  height: 1.75rem;
  color: var(--primary-color);
}
</style>
