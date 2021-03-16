const cordova = require('cordova')

/** @type {import('@vue/cli-service').ServicePlugin} */
const clean = api => {
  api.registerCommand('clean', args => {
    const [arg1, arg2] = process.argv
    return cordova.cli([arg1, arg2, 'clean'])
      .catch(e => console.error(e.message))
  })
}

/** @type {import('@vue/cli-service').ServicePlugin} */
const build = api => {
  const [arg1, arg2] = process.argv

  const { build } = api.service.commands

  const originalBuild = build.fn

  build.fn = (...args) => {
    return originalBuild(...args)
      .then(() => cordova.cli([arg1, arg2, 'prepare']))
      .then(() => cordova.cli([arg1, arg2, 'compile']))
      .catch(e => console.error(e.message))
  }
}

/** @type {import('@vue/cli-service').ServicePlugin} */
const develop = api => {
  api.registerCommand('develop', args => {
    const [arg1, arg2] = process.argv
    return cordova.cli([arg1, arg2, 'run'])
      .catch(e => console.error(e.message))
  })
}

/** @type {import('@vue/cli-service').ServicePlugin} */
module.exports = (api, options) => [clean, build, develop].map(fn => fn(api, options))
