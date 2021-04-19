// 所有的温度值（16 - 32）
export const temps = Array(33).fill(16).map((item, i) => item + i * 0.5).reverse()

// 温度值展示到界面上的文本（20 → 20.0）
export const columns = temps.map(i => ({ text: i.toFixed(1), value: i }))

// 色阶
const presetColors = [
  '#ed5a65',
  '#ff606c',
  '#ff606c',
  '#ff7b89',
  '#ff7b89',
  '#f07c82',
  '#f07c82',
  '#f09191',
  '#f09191',
  '#f09191',
  '#e2b9b3',
  '#e2b9b3',
  '#e2b9b3',
  '#dbcb8e',
  '#dbcb8e',
  '#dbcb8e',
  '#e8dbcb',
  '#e8dbcb',
  '#e6ee9c',
  '#e6ee9c',
  '#f0f4c3',
  '#f0f4c3',
  '#c5e1a5',
  '#c5e1a5',
  '#dcedc8',
  '#dcedc8',
  '#a5d6a7',
  '#a5d6a7',
  '#b9f6ca',
  '#b9f6ca',
  '#55bb8a',
  '#55bb8a',
  '#55bb8a'
]

// 温度值与颜色之间的映射关系
export const colors = temps.reduce((prev, current, i) => ({
  ...prev,
  [current]: presetColors[i]
}), {})
