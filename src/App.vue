<template>
  <Slider class="top" v-model="state.volume" />
  <Picker class="left" v-model="state.temperature.left" />
  <Picker class="right" v-model="state.temperature.right" />
  <div class="bottom">
    <Switch align="left" v-model="state.preset.left" />
    <Switch align="right" v-model="state.preset.right" />
  </div>
  <!-- x、y: 风口在屏幕上的位置 -->
  <Outlet :id="0" :x="250" :y="416" :mode="state.outlets[0].mode" :vertical="parse(state.outlets[0].vertical)" :horizontal="parse(state.outlets[0].horizontal)" :volume="state.volume" :temperature="state.temperature.left" :transition="state.outlets[0].transition"/>
  <Outlet :id="1" :x="490" :y="416" :mode="state.outlets[1].mode" :vertical="parse(state.outlets[1].vertical)" :horizontal="parse(state.outlets[1].horizontal)" :volume="state.volume" :temperature="state.temperature.left" :transition="state.outlets[1].transition"/>
  <Outlet :id="2" :x="790" :y="416" :mode="state.outlets[2].mode" :vertical="parse(state.outlets[2].vertical)" :horizontal="parse(state.outlets[2].horizontal)" :volume="state.volume" :temperature="state.temperature.right" :transition="state.outlets[2].transition"/>
  <Outlet :id="3" :x="1030" :y="416" :mode="state.outlets[3].mode" :vertical="parse(state.outlets[3].vertical)" :horizontal="parse(state.outlets[3].horizontal)" :volume="state.volume" :temperature="state.temperature.right" :transition="state.outlets[3].transition"/>
</template>

<script setup>
// 从 Vue.js 框架包里面提取需要用到的 API
import { reactive, watch, onMounted, onUnmounted } from 'vue'

// 导入自定义组件，这里的组件就可以在当前组件中直接使用（上面 template 中的 <Slider> 这些）
import Outlet from './components/Outlet.vue'
import Picker from './components/Picker.vue'
import Slider from './components/Slider.vue'
import Switch from './components/Switch.vue'

// 功能性的函数或者对象
import storage from './utils/storage'
import throttle from './utils/throttle'

const useState = () => {
  // 程序状态，默认值
  // 状态是程序运行过程中使用的数据，会在值发生改变的时候自动保存，下次可取
  // 状态里面的值最终绑定到界面元素上，界面元素操作会自动修改状态里的值
  const state = reactive({
    volume: 4, // 风量（0 - 10）
    temperature: {
      left: 24.5, // 左侧温度
      right: 26.5 // 右侧温度
    },
    preset: {
      left: 'manual', // 左侧预设模式，四个可选值：manual（手动）、focus（聚焦）、avoid（避脸）、sweep（扫风）
      right: 'manual' // 右侧预设模式
    },
    // 出风口位置（跟电机无关）及角度
    // vertical: 垂直角度（0 - 650）
    // horizontal: 水平角度（0 - 650）
    outlets: [
      { vertical: 300, horizontal: 325 }, // 0 号风口
      { vertical: 300, horizontal: 325 }, // 1 号风口
      { vertical: 300, horizontal: 325 }, // 2 号风口
      { vertical: 300, horizontal: 325 } // 3 号风口
    ]
  })

  // 当前组件挂载到界面上（开始工作）的时候
  onMounted(() => {
    // 尝试获取之前保存的状态值
    const store = storage.get('state')
    // 如果有，更新到状态中
    store && Object.assign(state, store)

    // 监视整体状态数据，一旦变化就自动保存数据值，供下次使用
    watch(() => state, throttle(value => storage.set('state', value), 500), { deep: true })
  })

  return state
}

// 程序状态
const state = useState()

// #region 风口角度控制逻辑
// 角度转换函数，程序界面上是 10 - -10，而业务数据是 0 - 650
const parse = input => input / 650 * 20 - 10
const revert = input => (input + 10) / 20 * 650

// 拖拽事件处理函数，风口角度更新逻辑，当拖拽任何一个风口自动执行
const update = e => {
  // 如果触摸的初始目标不是风口，忽略
  if (e.target.tagName !== 'I') return

  // 获取触摸位置的横纵坐标
  const { clientX, clientY } = e.touches[0]
  // 拿到触摸的风口对应的 id 和相应的界面元素
  const { id, parentElement } = e.target.parentElement.parentElement
  // 获取该风口对应的界面元素在屏幕上的位置
  const { offsetLeft, offsetTop } = parentElement

  // 根据风口位置和触摸点位置，计算以风口为坐标原点的横纵坐标值
  const x = clientX - offsetLeft
  const y = clientY - offsetTop

  // 获取数据状态中对应风口的数据值
  const current = state.outlets[id]

  // 手动拖拽风口，导致预设模式变为手动
  if (id < 2) {
    state.preset.left = 'manual'
  } else {
    state.preset.right = 'manual'
  }

  // 三角函数，根据 x y 位置计算角度
  const va = revert(-180 / (Math.PI / Math.atan((y - current.y) / 500)))
  const ha = revert(180 / (Math.PI / Math.atan((x - current.x) / 500)))

  // 控制拖拽角度变化的最大值与最小值
  if (va > 0 && va < 650) current.vertical = va
  if (ha > 0 && ha < 650) current.horizontal = ha
}

// 绑定拖拽事件
onMounted(() => document.addEventListener('touchmove', update, false))
onUnmounted(() => document.removeEventListener('touchmove', update))

// 风口角度变化后自动执行，id 为发生变化的风口 id
const outletUpdate = id => throttle(outlet => {
  // Native 是运行环境暴露的一个 JSBridge，只有在 Android 环境中才有
  if (typeof Native !== 'undefined')
    // 调用原生 API 发送信号
    Native.update(id, outlet.vertical, outlet.horizontal, console.log)
  else
    // 打印数据到控制台
    console.log(id, outlet.vertical, outlet.horizontal)
}, 1000) // 最小执行间隔 1000ms

// 分别监视每一个风口数据变化
state.outlets.forEach((_, id) => watch(() => state.outlets[id], outletUpdate(id), { deep: true }))
// #endregion

// #region 预设模式变化逻辑
// 高阶函数，左右预设模式发生修改后执行
const persetChange = (align, o1, o2) => preset => {
  // 启动风口位置变化时的过渡动画（手动和扫风不需要动画）
  state.outlets[o1].transition = true
  state.outlets[o2].transition = true

  // 聚焦模式
  if (preset === 'focus') {
    state.outlets[o1].horizontal = 620 // 单侧第一个风口 620 步
    state.outlets[o2].horizontal = 30 // 单侧第二个风口 30 步
    return
  }

  // 避脸模式
  if (preset === 'avoid') {
    state.outlets[o1].horizontal = 30 // 单侧第一个风口 30 步
    state.outlets[o2].horizontal = 620 // 单侧第二个风口 620 步
    return
  }

  // 扫风
  if (preset === 'sweep') {
    let inc1 = 2.5 // 单侧第一个风口每帧增加 2.5 步，值越大，扫风运动越快速
    let inc2 = 2.5 // 单侧第二个风口每帧增加 2.5 步

    let angle1 = state.outlets[o1].horizontal // 当前单侧第一个风口横向角度
    let angle2 = state.outlets[o2].horizontal // 当前单侧第二个风口横向角度
    const sweep = () => {
      // 如果此侧预设模式不再是扫风，则停止角度变化
      if (state.preset[align] !== 'sweep') return

      // 边界值，只允许在角度 0 - 650 之间
      if (angle1 > 650 || angle1 < 0) inc1 = -inc1
      if (angle2 > 650 || angle2 < 0) inc2 = -inc2

      // 角度变化
      angle1 += inc1
      angle2 += inc2
      state.outlets[o1].horizontal = angle1
      state.outlets[o2].horizontal = angle2

      // 继续下一帧
      requestAnimationFrame(sweep)
    }

    // 动画第一帧
    requestAnimationFrame(sweep)
  }

  // 关闭风口过渡动画
  state.outlets[o1].transition = false
  state.outlets[o2].transition = false
}

// 分别监视左右侧预设模式，左侧修改 0 1 风口，右侧修改 2 3 风口
watch(() => state.preset.left, persetChange('left', 0, 1))
watch(() => state.preset.right, persetChange('right', 2, 3))
// #endregion
</script>

<style>
@font-face {
	font-family: 'DSDIGI';
	src: url(assets/DSDIGI.woff) format('woff');
}
html {
  height: 100%;
}
body {
  --primary-color: #5dabee;
  --primary-color-alpha: #5dabee50;

  position: relative;
  display: flex;
  justify-content: center;
  align-items: center;
  height: 100%;
  margin: 0;
  color: #fff;
  background: #000;
  -webkit-font-smoothing: antialiased;
  -moz-osx-font-smoothing: grayscale;
  -webkit-tap-highlight-color: rgba(0, 0, 0, 0);
  -webkit-touch-callout: none;
  text-size-adjust: none;
  touch-action: none;
  user-select: none;
}
#app {
  position: relative;
  width: 1280px;
  height: 800px;
  background: center/contain no-repeat url(assets/background.png);
  font-family: DSDIGI, system-ui;
}
#app::after {
  content: '';
  display: block;
  padding-bottom: 62.5%;
}
.top {
  position: absolute;
  top: 5rem;
  left: 20rem;
  right: 20rem;
  z-index: 1;
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
  position: absolute;
  left: 5rem;
  right: 5rem;
  bottom: 4rem;
  z-index: 1;
  display: flex;
  justify-content: space-between;
}
</style>
