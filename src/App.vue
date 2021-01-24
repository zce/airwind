<template>
  <Outlet ref="oneOutlet" :x="220" :y="415" :vertical="-5" :horizontal="0" @update="(vertical, horizontal) => Object.assign(oneAngle, { vertical, horizontal })" />
  <Outlet ref="twoOutlet" :x="490" :y="415" :vertical="-5" :horizontal="0" @update="(vertical, horizontal) => Object.assign(twoAngle, { vertical, horizontal })" />
  <Outlet ref="threeOutlet" :x="790" :y="415" :vertical="-5" :horizontal="0" @update="(vertical, horizontal) => Object.assign(threeAngle, { vertical, horizontal })" />
  <Outlet ref="fourOutlet" :x="1060" :y="415" :vertical="-5" :horizontal="0" @update="(vertical, horizontal) => Object.assign(fourAngle, { vertical, horizontal })" />
  <Picker class="left" v-model="leftTemp" />
  <Picker class="right" v-model="rightTemp" />
  <div class="bottom">
    <Slider v-model="airVolume" />
    <Switch align="left" v-model="leftPreset" />
    <Switch align="right" v-model="rightPreset" />
  </div>
</template>

<script setup>
import { onMounted, ref, watchEffect } from 'vue'

import Outlet from './components/Outlet.vue'
import Picker from './components/Picker.vue'
import Slider from './components/Slider.vue'
import Switch from './components/Switch.vue'

const leftTemp = ref(24.5)
const rightTemp = ref(26.5)
const airVolume = ref(4)
const leftPreset = ref()
const rightPreset = ref()
const oneOutlet = ref(null)
const oneAngle = { vertical: -3, horizontal: 0 }
const twoOutlet = ref(null)
const twoAngle = { vertical: -3, horizontal: 0 }
const threeOutlet = ref(null)
const threeAngle = { vertical: -3, horizontal: 0 }
const fourOutlet = ref(null)
const fourAngle = { vertical: -3, horizontal: 0 }

const presets = {
  sweep: () => {
    let angle = 0
    let inc = 0.1
    oneAngle.horizontal = angle
    twoAngle.horizontal = angle
    threeAngle.horizontal = angle
    fourAngle.horizontal = angle
    const update = () => {
      if (angle > 10) inc = -0.1
      if (angle < -10) inc = 0.1
      angle += inc
      oneOutlet.value.transform(oneAngle.vertical, angle)
      twoOutlet.value.transform(twoAngle.vertical, angle)
      threeOutlet.value.transform(threeAngle.vertical, angle)
      fourOutlet.value.transform(fourAngle.vertical, angle)
      leftPreset.value === 'sweep' && requestAnimationFrame(update)
    }
    update()
  }
}

watchEffect(() => {
  console.log('leftTemp', leftTemp.value)
  console.log('rightTemp', rightTemp.value)
})

watchEffect(() => {
  console.log('airVolume', airVolume.value)
})

watchEffect(() => {
  console.log('leftPreset', leftPreset.value)
  if (leftPreset.value === 'sweep') presets.sweep()
  // console.log('rightPreset', rightPreset.value)
})
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
