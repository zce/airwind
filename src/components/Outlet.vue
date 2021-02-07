<template>
  <div class="outlet" :style="style">
    <div class="layer">
      <i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/>
    </div>
    <div class="layer">
      <i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/><i/>
    </div>
  </div>
</template>

<script setup>
import { computed, watch, onMounted, onUnmounted, defineProps, defineEmit } from 'vue'
import { colors } from '../utils/temperatures'

const perspective = 500

const parse = input => input / 150 * 20 - 10
const revert = input => (input + 10) / 20 * 150

const props = defineProps({
  mode: String,
  volume: Number,
  temperature: Number,
  x: Number,
  y: Number,
  vertical: Number,
  horizontal: Number
})

const emit = defineEmit(['update:vertical', 'update:horizontal'])

const update = e => {
  const { clientX, clientY } = e.touches[0]
  const x = clientX - props.x
  const y = clientY - props.y
  if (Math.abs(x) > 100) return
  if (Math.abs(y) > 120) return

  const va = -180 / (Math.PI / Math.atan(y / perspective))
  const ha = 180 / (Math.PI / Math.atan(x / perspective))

  emit('update:vertical', revert(va))
  emit('update:horizontal', revert(ha))
}

onMounted(() => window.addEventListener('touchmove', update, false))

onUnmounted(() => window.removeEventListener('touchmove', update))

const sweep = () => {
  let inc = 0.3
  let angle = props.horizontal
  const update = () => {
    if (props.mode !== 'sweep') return
    if (angle > 150 || angle < 0) inc = -inc
    angle += inc
    emit('update:horizontal', angle)
    requestAnimationFrame(update)
  }
  requestAnimationFrame(update)
}

watch(() => props.mode, mode => mode === 'sweep' && sweep(), { immediate: true })

const style = computed(() => ({
  top: `${props.y}px`,
  left: `${props.x}px`,
  transform: `perspective(${perspective}px) rotateX(${87 + parse(props.vertical)}deg) skewX(${parse(props.horizontal)}deg)`,

  '--size': 1 + 0.03 * props.volume,
  '--color': colors[props.temperature]
}))
</script>

<style>
.outlet {
  position: absolute;
  transform-style: preserve-3d;
  transition: transform 300ms;
}
.layer {
  position: absolute;
}
.layer:nth-child(1) {
  transform: perspective(1000px) rotateX(1deg) translateZ(-1px) scale(0.8, var(--size, 1));
}
.layer:nth-child(2) {
  transform: perspective(1000px) rotateX(-1deg) translateZ(-5px) scale(0.8, var(--size, 1));
}
.layer i {
  position: absolute;
  top: 0;
  width: 30px;
  height: 150px;
  background: linear-gradient(var(--color, var(--primary-color)), #00000000);
  border-bottom-left-radius: 0;
  border-bottom-right-radius: 0;
  transform-origin: top;
  mix-blend-mode: screen;
  will-change: transform;
  transition: background 300ms;
  animation: move 1000ms linear infinite;
}
.layer i:nth-child(1) {
  left: -29px;
  animation-delay: -702ms;
}
.layer i:nth-child(2) {
  left: -28px;
  animation-delay: -96ms;
}
.layer i:nth-child(3) {
  left: -27px;
  animation-delay: -203ms;
}
.layer i:nth-child(4) {
  left: -26px;
  animation-delay: -304ms;
}
.layer i:nth-child(5) {
  left: -25px;
  animation-delay: -92ms;
}
.layer i:nth-child(6) {
  left: -24px;
  animation-delay: -581ms;
}
.layer i:nth-child(7) {
  left: -23px;
  animation-delay: -534ms;
}
.layer i:nth-child(8) {
  left: -22px;
  animation-delay: -589ms;
}
.layer i:nth-child(9) {
  left: -21px;
  animation-delay: -71ms;
}
.layer i:nth-child(10) {
  left: -20px;
  animation-delay: -743ms;
}
.layer i:nth-child(11) {
  left: -19px;
  animation-delay: -989ms;
}
.layer i:nth-child(12) {
  left: -18px;
  animation-delay: -127ms;
}
.layer i:nth-child(13) {
  left: -17px;
  animation-delay: -809ms;
}
.layer i:nth-child(14) {
  left: -16px;
  animation-delay: -937ms;
}
.layer i:nth-child(15) {
  left: -15px;
  animation-delay: -227ms;
}
.layer i:nth-child(16) {
  left: -14px;
  animation-delay: -893ms;
}
.layer i:nth-child(17) {
  left: -13px;
  animation-delay: -418ms;
}
.layer i:nth-child(18) {
  left: -12px;
  animation-delay: -641ms;
}
.layer i:nth-child(19) {
  left: -11px;
  animation-delay: -863ms;
}
.layer i:nth-child(20) {
  left: -10px;
  animation-delay: -297ms;
}
.layer i:nth-child(21) {
  left: -9px;
  animation-delay: -567ms;
}
.layer i:nth-child(22) {
  left: -8px;
  animation-delay: -596ms;
}
.layer i:nth-child(23) {
  left: -7px;
  animation-delay: -761ms;
}
.layer i:nth-child(24) {
  left: -6px;
  animation-delay: -378ms;
}
.layer i:nth-child(25) {
  left: -5px;
  animation-delay: -10ms;
}
.layer i:nth-child(26) {
  left: -4px;
  animation-delay: -681ms;
}
.layer i:nth-child(27) {
  left: -3px;
  animation-delay: -786ms;
}
.layer i:nth-child(28) {
  left: -2px;
  animation-delay: -744ms;
}
.layer i:nth-child(29) {
  left: -1px;
  animation-delay: -110ms;
}
.layer i:nth-child(30) {
  left: -0px;
  animation-delay: -163ms;
}
@keyframes move {
  87.7% {
    transform: scaleY(2.6);
    opacity: 0;
  }
  100% {
    opacity: 0;
  }
}
</style>
