<template>
  <Slider class="top" v-model="state.volume" />
  <Picker class="left" v-model="state.temperature.left" />
  <Picker class="right" v-model="state.temperature.right" />
  <div class="bottom">
    <Switch align="left" v-model="state.preset.left" />
    <Switch align="right" v-model="state.preset.right" />
  </div>
  <Outlet
    v-for="(item, i) in state.outlets"
    :key="i"
    :id="i"
    :mode="item.mode"
    :volume="state.volume"
    :temperature="i < 2 ? state.temperature.left : state.temperature.right"
    :x="item.x"
    :y="item.y"
    :vertical="parse(item.vertical)"
    :horizontal="parse(item.horizontal)"
    :transition="item.transition"
  />
</template>

<script setup>
import { reactive, watch, onMounted, onUnmounted } from 'vue'

import Outlet from './components/Outlet.vue'
import Picker from './components/Picker.vue'
import Slider from './components/Slider.vue'
import Switch from './components/Switch.vue'

import storage from './utils/storage'

const defaults = {
  volume: 4,
  temperature: { left: 24.5, right: 26.5 },
  preset: { left: 'manual', right: 'manual' },
  outlets: [
    { x: 250, y: 416, vertical: 65, horizontal: 75 },
    { x: 490, y: 416, vertical: 65, horizontal: 75 },
    { x: 790, y: 416, vertical: 65, horizontal: 75 },
    { x: 1030, y: 416, vertical: 65, horizontal: 75 }
  ]
}

const state = reactive(storage.get('state') || defaults)

const persetChange = (align, o1, o2) => preset => {
  state.outlets[o1].transition = true
  state.outlets[o2].transition = true
  if (preset === 'focus') {
    USB2LIN.coolMethod('123213', alert)
    state.outlets[o1].horizontal = 140
    state.outlets[o2].horizontal = 10
    return
  }
  if (preset === 'avoid') {
    state.outlets[o1].horizontal = 10
    state.outlets[o2].horizontal = 140
    return
  }
  if (preset === 'sweep') {
    let inc1 = 0.3
    let inc2 = 0.3
    let angle1 = state.outlets[o1].horizontal
    let angle2 = state.outlets[o2].horizontal
    const sweep = () => {
      if (state.preset[align] !== 'sweep') return
      if (angle1 > 150 || angle1 < 0) inc1 = -inc1
      if (angle2 > 150 || angle2 < 0) inc2 = -inc2
      angle1 += inc1
      angle2 += inc2
      state.outlets[o1].horizontal = angle1
      state.outlets[o2].horizontal = angle2
      requestAnimationFrame(sweep)
    }
    requestAnimationFrame(sweep)
  }
  state.outlets[o1].transition = false
  state.outlets[o2].transition = false
}

watch(() => state.preset.left, persetChange('left', 0, 1))

watch(() => state.preset.right, persetChange('right', 2, 3))

watch(() => state, value => storage.set('state', value), { deep: true })

const parse = input => input / 150 * 20 - 10
const revert = input => (input + 10) / 20 * 150

const update = e => {
  if (e.target.tagName !== 'I') return

  const { clientX, clientY } = e.touches[0]
  const { id, parentElement } = e.target.parentElement.parentElement
  const { offsetLeft, offsetTop } = parentElement

  const x = clientX - offsetLeft
  const y = clientY - offsetTop

  const current = state.outlets[id]

  if (id < 2) {
    state.preset.left = 'manual'
  } else {
    state.preset.right = 'manual'
  }

  const va = revert(-180 / (Math.PI / Math.atan((y - current.y) / 500)))
  const ha = revert(180 / (Math.PI / Math.atan((x - current.x) / 500)))

  if (va > 0 && va < 150) current.vertical = va
  if (ha > 0 && ha < 150) current.horizontal = ha
}

onMounted(() => document.addEventListener('touchmove', update, false))

onUnmounted(() => document.removeEventListener('touchmove', update))

// window.state = state
</script>

<style>
@font-face {
	font-family: 'DSDIGI';
	src: url(assets/DSDIGI.woff) format('woff');
}
:root {
  --primary-color: #5dabee;
  --primary-color-alpha: #5dabee40;
}
html, body {
  overflow: hidden;
  height: 100%;
  width: 100%;
  touch-action: none;
}
body {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  margin: 0;
  padding: env(safe-area-inset-top, 0) env(safe-area-inset-right, 0) env(safe-area-inset-bottom, 0) env(safe-area-inset-left, 0);
  color: #fff;
  background: #000;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
  -webkit-touch-callout: none;                /* prevent callout to copy image, etc when tap to hold */
  -webkit-text-size-adjust: none;             /* prevent webkit from resizing text to fit */
  -webkit-user-select: none;                  /* prevent copy paste, to allow, change 'none' to 'text' */
  user-select: none;
}
#app {
  position: relative;
  height: 800px;
  width: 1280px;
  background: center/contain no-repeat url(assets/background.png);
  font-family: DSDIGI, system-ui;
}
.left {
  z-index: 1;
  position: absolute;
  top: 5rem;
  bottom: 10rem;
  left: 0;
  width: 10rem;
}
.right {
  z-index: 1;
  position: absolute;
  top: 5rem;
  bottom: 10rem;
  right: 0;
  width: 10rem;
}
.bottom {
  z-index: 1;
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: space-between;
}
</style>
