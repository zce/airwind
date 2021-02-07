/**
 * Storage
 *
 * @example
 *   storage.get( ... )
 *   storage.set( ... )
 *   storage.remove( ... )
 *   storage.clear( ... )
 */

// @ts-check

const prefix = 'air-wind-'

export default {
  /** @param {string} key */
  get: key => {
    const json = localStorage.getItem(prefix + key)
    try {
      if (!json) return json
      return JSON.parse(json)
    } catch {
      return json
    }
  },
  /** @param {string} key @param {any} value */
  set: (key, value) => {
    const json = JSON.stringify(value)
    localStorage.setItem(prefix + key, json)
  },
  /** @param {string} key */
  remove: key => {
    localStorage.removeItem(prefix + key)
  },
  clear: () => {
    localStorage.clear()
  }
}
