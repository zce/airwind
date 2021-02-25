package org.apache.cordova.usb2lin;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

/**
 * This class echoes a string called from JavaScript.
 */
public class USB2LIN extends CordovaPlugin {

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callbackContext) throws JSONException {
        if (action.equals("update")) {
            int id = args.getInt(0);
            int vertical = args.getInt(1);
            int horizontal = args.getInt(2);
            this.update(id, vertical, horizontal, callbackContext);
            return true;
        }
        return false;
    }

    private void update(int id, int vertical, int horizontal, CallbackContext callbackContext) {
        // TODO: to lin
        // 成功回調
        callbackContext.success(id);
        // 失敗回調
        // callbackContext.error("message");
    }
}
