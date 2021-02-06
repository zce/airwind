<template>
  <Outlet
    v-for="item in state.outlets"
    :key="item.x"
    :mode="item.mode"
    :x="item.x"
    :y="item.y"
    v-model:vertical="item.vertical"
    v-model:horizontal="item.horizontal"
  />
  <Picker class="left" v-model="state.temperature.left" />
  <Picker class="right" v-model="state.temperature.right" />
  <div class="bottom">
    <Slider v-model="state.volume" />
    <Switch align="left" v-model="state.preset.left" />
    <Switch align="right" v-model="state.preset.right" />
  </div>
</template>

<script setup>
import { reactive, watch } from 'vue'

import Outlet from './components/Outlet.vue'
import Picker from './components/Picker.vue'
import Slider from './components/Slider.vue'
import Switch from './components/Switch.vue'

const state = reactive({
  temperature: { left: 24.5, right: 26.5 },
  volume: 4,
  preset: { left: null, right: null },
  outlets: [
    { mode: 'manual', x: 220, y: 415, vertical: -4, horizontal: 0 },
    { mode: 'manual', x: 490, y: 415, vertical: -4, horizontal: 0 },
    { mode: 'manual', x: 790, y: 415, vertical: -4, horizontal: 0 },
    { mode: 'manual', x: 1060, y: 415, vertical: -4, horizontal: 0 }
  ]
})

watch(() => state.preset.left, preset => {
  console.log('preset.left', preset)
  state.outlets[0].mode = preset
  state.outlets[1].mode = preset
  if (preset === 'focus') {
    state.outlets[0].horizontal = 10
    state.outlets[1].horizontal = -10
  }
  if (preset === 'avoid') {
    state.outlets[0].horizontal = -10
    state.outlets[1].horizontal = 10
  }
})

watch(() => state.preset.right, preset => {
  console.log('preset.right', preset)
  state.outlets[2].mode = preset
  state.outlets[3].mode = preset
  if (preset === 'focus') {
    state.outlets[2].horizontal = 10
    state.outlets[3].horizontal = -10
  }
  if (preset === 'avoid') {
    state.outlets[2].horizontal = -10
    state.outlets[3].horizontal = 10
  }
})

window.state = state
</script>

<style>
@font-face {
	font-family: 'DSDIGI';
	src: url('./assets/DSDIGI.eot');
	src: local('DSDIGI'), url('./assets/DSDIGI.woff') format('woff'), url('./assets/DSDIGI.ttf') format('truetype');
}
body {
  position: relative;
  display: flex;
  align-items: center;
  background: center/contain no-repeat #000 url(./assets/background.png);
  font-family: DSDIGI, system-ui;
  color: #fff;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}
#app {
  position: relative;
  height: 800px;
  width: 100%;
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
  z-index: 0;
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  padding: 4rem 0;
}
</style>
