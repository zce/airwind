<template>
  <div class="outlet" ref="wrapper" :style="{ top: `${y}px`, left: `${x}px`, transform: `perspective(${perspective}px) rotateX(${87 + vertical}deg) skewX(${horizontal}deg)` }">
    <div class="layer" v-for="l in layerCount" :key="l">
      <i
        v-for="p in particleCount"
        :key="p"
        :style="{
          left: `${10 - Math.floor(Math.random() * 50)}px`,
          animationDelay: `-${Math.floor(Math.random() * 1000)}ms`
        }"
      />
    </div>
  </div>
</template>

<script>

export default {
  props: {
    x: Number,
    y: Number,
    vertical: Number,
    horizontal: Number
  },
  data () {
    return {
      layerCount: 2,
      particleCount: 30,
      perspective: 500
    }
  },
  mounted () {
    window.addEventListener('touchmove', this.update, false)
  },
  unmounted () {
    window.removeEventListener('touchmove', update)
  },
  methods: {
    update (e) {
      const { clientX, clientY } = e.touches[0]
      const x = clientX - this.x
      const y = clientY - this.y
      if (Math.abs(x) > 100) return
      if (Math.abs(y) > 120) return

      const va = -180 / (Math.PI / Math.atan(y / this.perspective))
      const ha = 180 / (Math.PI / Math.atan(x / this.perspective))

      this.transform(va, ha)
      this.$emit('update', va, ha)
    },
    transform (va, ha) {
      this.$refs.wrapper.style.transform = `perspective(${this.perspective}px) rotateX(${87 + va}deg) skewX(${ha}deg)`
    }
  }
}
</script>

<!-- <script setup>
import { ref, defineProps, defineEmit, onMounted, onUnmounted } from 'vue'
const layerCount = 2
const particleCount = 30
const perspective = 500

const props = defineProps({
  x: Number,
  y: Number,
  vertical: Number,
  horizontal: Number
})

const emit = defineEmit(['update'])

const wrapper = ref(null)

const update = e => {
  const { clientX, clientY } = e.touches[0]
  const x = clientX - props.x
  const y = clientY - props.y
  if (Math.abs(x) > 100) return
  if (Math.abs(y) > 120) return

  const va = -180 / (Math.PI / Math.atan(y / perspective))
  const ha = 180 / (Math.PI / Math.atan(x / perspective))

  wrapper.value.style.transform = `perspective(${perspective}px) rotateX(${87 + va}deg) skewX(${ha}deg)`
  emit('update', va, ha)
}

onMounted(() => window.addEventListener('touchmove', update, false))

onUnmounted(() => window.removeEventListener('touchmove', update))
</script> -->

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
  transform: perspective(1000px) rotateX(1deg) scaleX(0.5);
}

.layer:nth-child(2) {
  transform: perspective(1000px) rotateX(-1deg) scaleX(0.5);
}

.layer i {
  position: absolute;
  top: 0;
  width: 30px;
  height: 150px;
  background: #3dfff8;
  border-radius: 50%;
  /* mix-blend-mode: screen; */
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
