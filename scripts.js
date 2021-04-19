const { cordova } = require('cordova-lib')

/** @type {import('@vue/cli-service').ServicePlugin} */
const clean = api => {
  api.registerCommand('clean', args => {
    return cordova.clean()
      .catch(console.error)
  })
}

/** @type {import('@vue/cli-service').ServicePlugin} */
const build = api => {
  const { build } = api.service.commands

  const originalBuild = build.fn

  build.fn = (...args) => {
    return originalBuild(...args)
      .then(() => cordova.prepare())
      .then(() => cordova.compile({ options: args[0] }))
      .catch(console.error)
  }
}

/** @type {import('@vue/cli-service').ServicePlugin} */
const develop = api => {
  api.registerCommand('develop', args => {
    return cordova.run()
      .catch(console.error)
  })
}

/** @type {import('@vue/cli-service').ServicePlugin} */
module.exports = (api, options) => [clean, build, develop].map(fn => fn(api, options))
