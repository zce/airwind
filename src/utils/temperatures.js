
export const temps = Array(33).fill(16).map((item, i) => item + i * 0.5).reverse()

export const columns = temps.map(i => ({ text: i.toFixed(1), value: i }))

export const colors = temps.reduce((prev, current, i) => ({
  ...prev,
  [current]: `rgb(${Math.floor(255 / 33 * i)}, 180, 180)`
}), {})
