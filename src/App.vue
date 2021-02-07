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
    :key="item.x"
    :mode="item.mode"
    :volume="state.volume"
    :temperature="i < 2 ? state.temperature.left : state.temperature.right"
    :x="item.x"
    :y="item.y"
    v-model:vertical="item.vertical"
    v-model:horizontal="item.horizontal"
  />
</template>

<script setup>
import { reactive, useCssVars, watch } from 'vue'

import Outlet from './components/Outlet.vue'
import Picker from './components/Picker.vue'
import Slider from './components/Slider.vue'
import Switch from './components/Switch.vue'

const state = reactive({
  temperature: { left: 24.5, right: 26.5 },
  volume: 4,
  preset: { left: 'manual', right: 'manual' },
  outlets: [
    { mode: 'manual', x: 220, y: 415, vertical: 75, horizontal: 75 },
    { mode: 'manual', x: 490, y: 415, vertical: 75, horizontal: 75 },
    { mode: 'manual', x: 790, y: 415, vertical: 75, horizontal: 75 },
    { mode: 'manual', x: 1060, y: 415, vertical: 75, horizontal: 75 }
  ]
})

watch(() => state.preset.left, preset => {
  console.log('preset.left', preset)
  state.outlets[0].mode = preset
  state.outlets[1].mode = preset
  if (preset === 'focus') {
    state.outlets[0].horizontal = 140
    state.outlets[1].horizontal = 10
  }
  if (preset === 'avoid') {
    state.outlets[0].horizontal = 10
    state.outlets[1].horizontal = 140
  }
})

watch(() => state.preset.right, preset => {
  console.log('preset.right', preset)
  state.outlets[2].mode = preset
  state.outlets[3].mode = preset
  if (preset === 'focus') {
    state.outlets[2].horizontal = 140
    state.outlets[3].horizontal = 10
  }
  if (preset === 'avoid') {
    state.outlets[2].horizontal = 10
    state.outlets[3].horizontal = 140
  }
})

window.state = state
</script>

<style>
@font-face {
	font-family: 'DSDIGI';
	src: url(assets/DSDIGI.eot);
	src: local('DSDIGI'), url(assets/DSDIGI.woff) format('woff'), url(assets/DSDIGI.ttf) format('truetype');
}
:root {
  --primary-color: #5dabee;
  --primary-color-alpha: #5dabee40;
}
body {
  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  font-family: DSDIGI, system-ui;
  color: #fff;
  background: #000;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}
#app {
  position: relative;
  height: 800px;
  width: 1280px;
  background: center/contain url(assets/background.png);
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
