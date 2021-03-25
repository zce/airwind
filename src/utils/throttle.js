// 节流函数，避免一个函数在一段时间内调用多次
export default (fn, delay = 500) => {
  let start = 0
  return (...args) => {
    if (Date.now() - start >= delay) {
      fn(...args)
      start = Date.now()
    }
  }
}
