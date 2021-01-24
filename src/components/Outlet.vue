<template>
  <div class="outlet" ref="wrapper" :style="style">
    <div class="layer" v-for="l in layerCount" :key="l">
      <i
        v-for="p in particleCount"
        :key="p"
        :style="{
          left: `${5 - Math.floor(Math.random() * 50)}px`,
          animationDelay: `-${Math.floor(Math.random() * 1000)}ms`
        }"
      />
    </div>
  </div>
</template>

<script setup>
import { ref, defineProps, computed, defineEmit, watchEffect, onMounted, onUnmounted } from 'vue'
import { useMouse } from '@vueuse/core'
const layerCount = 2
const particleCount = 20
const perspective = 500

const props = defineProps({
  x: Number,
  y: Number,
  modelValue: {
    type: Object,
    default: { vertical: 0, horizontal: 0 }
  }
})

const emit = defineEmit(['update:modelValue'])

const wrapper = ref(null)
const vertical = ref(props.modelValue.vertical)
const horizontal = ref(props.modelValue.horizontal)

const update = e => {
  const { clientX, clientY } = e.touches[0]
  const x = clientX - props.x
  const y = clientY - props.y
  if (Math.abs(x) > 100) return
  if (Math.abs(y) > 100) return

  const va = -180 / (Math.PI / Math.atan(y / perspective))
  const ha = 180 / (Math.PI / Math.atan(x / perspective))

  wrapper.value.style.transform = `perspective(${perspective}px) rotateX(${87 + va}deg) skewX(${ha}deg)`
}

onMounted(() => window.addEventListener('touchmove', update, false))

onUnmounted(() => window.removeEventListener('touchmove', update))

const style = {
  top: `${props.y}px`,
  left: `${props.x}px`,
  transform: `perspective(${perspective}px) rotateX(${87 + vertical.value}deg) skewX(${horizontal.value}deg)`
}

// const { x, y } = useMouse()

// watchEffect(() => {
//   const xOffset = x.value - props.x
//   const yOffset = y.value - props.y
//   if (Math.abs(xOffset) > 100) return
//   if (Math.abs(yOffset) > 100) return
//   const va = -180 / (Math.PI / Math.atan(yOffset / perspective))
//   const ha = 180 / (Math.PI / Math.atan(xOffset / perspective))
//   console.log(va, ha, wrapper.value)
//   if (wrapper.value)
//     wrapper.value.style.transform = `perspective(${perspective}px) rotateX(${87 + va}deg) skewX(${ha}deg)`
// })

</script>

<style>
.outlet {
  position: absolute;
  transform-style: preserve-3d;
  /* transition: transform 100ms; */
}

.layer {
  position: absolute;
}

.layer:nth-child(1) {
  transform: perspective(1000px) rotateX(1deg) scaleX(0.6);
}

.layer:nth-child(2) {
  transform: perspective(1000px) rotateX(-1deg) scaleX(0.6);
}

/* .layer:nth-child(3) {
  transform: perspective(1000px) rotateX(86.5deg) scaleX(0.5);
}

.layer:nth-child(4) {
  transform: perspective(1000px) rotateX(85.5deg) scaleX(0.5);
} */

.layer i {
  position: absolute;
  top: 0;
  width: 40px;
  height: 100px;
  background: #3dfff870;
  border-radius: 50%;
  mix-blend-mode: screen;
  will-change: transform;
  animation: move 1000ms linear infinite;
}

@keyframes move {
  0% {
    transform: translate3d(0, 0, 0);
  }
  10% {
    transform: translate3d(0, 0, 0);
  }
  87.7% {
    transform: translate3d(0, 180px, 0);
    opacity: 0;
  }
  100% {
    transform: translate3d(0, 200px, 0);
    opacity: 0;
  }
}
</style>
