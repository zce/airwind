export default (fn, delay = 500) => {
  let start = 0
  return (...args) => {
    if (Date.now() - start >= delay) {
      fn(...args)
      start = Date.now()
    }
  }
}
