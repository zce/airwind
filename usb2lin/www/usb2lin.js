var exec = require('cordova/exec');

exports.update = function (id, vertical, horizontal, success, error) {
  var args = [id, Math.round(vertical), Math.round(horizontal)];
  exec(success, error, 'USB2LIN', 'update', args);
};
