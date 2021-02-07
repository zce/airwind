export const temps = Array(33).fill(16).map((item, i) => item + i * 0.5).reverse()

export const columns = temps.map(i => ({ text: i.toFixed(1), value: i }))

// export const colors = temps.reduce((prev, current, i) => ({
//   ...prev,
//   [current]: `rgb(${Math.floor(255 / 33 * i)}, 180, 180)`
// }), {})

const presetColors = [
  '#bf0000',
  '#d20000',
  '#e30000',
  '#ff0000',
  '#ef5400',
  '#ff5a00',
  '#ff7200',
  '#ff8e33',
  '#ffba00',
  '#ffd800',
  '#ffdb17',
  '#edf000',
  '#fcff00',
  '#d2ff00',
  '#c8f300',
  '#a2ff00',
  '#42ff00',
  '#21e800',
  '#45e000',
  '#3eca00',
  '#00ca64',
  '#00ca9d',
  '#00dfad',
  '#00f2bc',
  '#00ffc6',
  '#00f0ff',
  '#00b2f4',
  '#00a6e4',
  '#0091e4',
  '#0091e4',
  '#2069ff',
  '#0053fc',
  '#0047e9'
]
export const colors = temps.reduce((prev, current, i) => ({
  ...prev,
  [current]: presetColors[i]
}), {})
