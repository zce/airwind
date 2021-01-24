<template>
  <Picker
    :columns="columns"
    :defaultIndex="options.indexOf(modelValue)"
    :visibleItemCount="size"
    :itemHeight="itemHeight"
    :showToolbar="false"
    @change="i => $emit('update:modelValue', i.value)"
  />
</template>

<script setup>
import { computed, defineEmit, defineProps } from 'vue'
import Picker from 'vant/es/picker'
// import 'vant/es/picker/style'

const props = defineProps({
  options: {
    type: Array,
    default: []
  },
  modelValue: {
    type: Number,
    default: ({ options }) => options[0]
  },
  size: {
    type: Number,
    default: 5
  },
  itemHeight: {
    type: Number,
    default: 80
  }
})

defineEmit(['update:modelValue'])

const columns = computed(() => props.options.map(i => ({ text: i.toFixed(1), value: i })))
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
.van-picker__frame {
  position: absolute;
  top: 50%;
  right: 0;
  left: 0;
  z-index: 2;
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
  font-size: 1.5rem;
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
}
</style>

<!-- <template>
  <div :style="{ height: `${wrapperHeight}px` }">
    <p>{{ x }} {{ y }}</p>
    <ul ref="inner">
      <li role="button" v-for="item in options" :key="item" :aria-pressed="item === modelValue" :style="{ height: `${itemHeight}px` }">
        {{ item.toFixed(1) }}
      </li>
    </ul>
  </div>
</template>

<script setup>
import { ref, defineEmit, defineProps, onMounted, computed } from 'vue'
import { useMouse } from '@vueuse/core'

const props = defineProps({
  options: {
    type: Array,
    default: []
  },
  modelValue: {
    type: Number,
    default: ({ options }) => options[0]
  },
  size: {
    type: Number,
    default: 5
  },
  itemHeight: {
    type: Number,
    default: 80
  }
})

const emit = defineEmit(['update:modelValue'])

// const wrapper = ref(null)
// const inner = ref(null)

const wrapperHeight = computed(() => props.itemHeight * props.size)

const { x, y } = useMouse({ touch: false })

// onMounted(() => {
//   /** @type {HTMLElement} */
//   const wrapperEl = wrapper.value

//   /** @type {HTMLElement} */
//   const innerEl = inner.value

//   const itemHeight = innerEl.children[0].clientHeight

//   wrapperEl.style.height = `${itemHeight * props.size}px`

//   innerEl.style.transform = `translateY(-${itemHeight}px)`
// })
</script>

<style>
div {
  overflow: hidden;
  max-height: 100%;
}

ul {
  margin: 0;
  padding: 0;
  list-style: none;
  text-align: center;
  transition: transform 50ms;
}

li {
  padding: 0.5rem;
  font-size: 2rem;
  line-height: 2;
  box-sizing: border-box;
}
</style> -->
