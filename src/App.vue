<template>
  <Outlet :x="220" :y="415" v-model="oneAngle" />
  <Outlet :x="490" :y="415" v-model="twoAngle" />
  <Outlet :x="790" :y="415" v-model="threeAngle" />
  <Outlet :x="1060" :y="415" v-model="fourAngle" />
  <Picker class="left" v-model="leftTemp" />
  <Picker class="right" v-model="rightTemp" />
  <div class="bottom">
    <Slider v-model="airVolume" />
    <Switch align="left" v-model="leftPreset" />
    <Switch align="right" v-model="rightPreset" />
  </div>
</template>

<script setup>
import { ref, watchEffect, onMounted } from 'vue'

import Outlet from './components/Outlet.vue'
import Picker from './components/Picker.vue'
import Slider from './components/Slider.vue'
import Switch from './components/Switch.vue'

const leftTemp = ref(24.5)
const rightTemp = ref(26.5)
const airVolume = ref(4)
const leftPreset = ref()
const rightPreset = ref()
const oneAngle = ref({ vertical: 0, horizontal: 0 })
const twoAngle = ref({ vertical: 0, horizontal: 0 })
const threeAngle = ref({ vertical: 0, horizontal: 0 })
const fourAngle = ref({ vertical: 0, horizontal: 0 })

watchEffect(() => {
  console.log('leftTemp', leftTemp.value)
  console.log('rightTemp', rightTemp.value)
  console.log('airVolume', airVolume.value)
  console.log('leftPreset', leftPreset.value)
  console.log('rightPreset', rightPreset.value)
})
</script>

<style>
@font-face {
	font-family: 'DSDIGI';
	src: url('./assets/DSDIGI.eot');
	src: local('DSDIGI'), url('./assets/DSDIGI.woff') format('woff'), url('./assets/DSDIGI.ttf') format('truetype');
}
html {
  overflow: hidden;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
}
body {
  position: relative;
  overflow: hidden;
  margin: 0;
  height: 100vh;
  background: 0/contain url(./assets/background.png);
  font-family: DSDIGI;
  color: #fff;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
}
.left {
  position: absolute;
  top: 0;
  bottom: 0;
  left: 0;
  padding: 5rem 0 10rem;
  width: 10rem;
}
.right {
  position: absolute;
  top: 0;
  bottom: 0;
  right: 0;
  padding: 5rem 0 10rem;
  width: 10rem;
}
.bottom {
  position: absolute;
  left: 0;
  right: 0;
  bottom: 0;
  display: flex;
  justify-content: space-between;
  flex-wrap: wrap;
  padding: 3rem 0;
}

/* .air-volume {
  display: flex;
  justify-content: center;
}

.air-volume svg {
  width: 2rem;
} */

/* .bottom .van-slider {
  flex: 1 0 80%;
  margin: 0 auto;
}
.bottom .switch {
  flex: 1 0 50%;
} */
</style>
