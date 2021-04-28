// 节流函数，避免一个函数在一段时间内调用多次
// export default (fn, delay = 500) => {
//   let start = 0
//   return (...args) => {
//     if (Date.now() - start >= delay) {
//       fn(...args)
//       start = Date.now()
//     }
//   }
// }

// https://gist.github.com/beaucharman/e46b8e4d03ef30480d7f4db5a78498ca
export default (fn, delay) => {
  let timeout = null
  return (...args) => {
    timeout = timeout || setTimeout(() => {
      fn(...args)
      timeout = null
    }, delay)
  }
}
