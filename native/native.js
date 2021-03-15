const exec = require('cordova/exec')

exports.update = (id, vertical, horizontal, success, error) => {
  const args = [id, Math.round(vertical), Math.round(horizontal)]
  exec(success, error, 'USB2LIN', 'update', args)
}
