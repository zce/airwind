package me.zce.airwind;

import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import android.util.Log;

/**
 * This class echoes a string called from JavaScript.
 */
public class Native extends CordovaPlugin {
    private void update(int id, int vertical, int horizontal, CallbackContext callback) {
        // TODO: to lin
        Log.i("USB2LIN", "id: " + id + ", vertical:" + vertical + ", horizontal:" + horizontal);
        // 成功回调
        callback.success(id);
        // 失败回调
        // callback.error("message");
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callback) throws JSONException {
        if (!action.equals("update")) return false;
        int id = args.getInt(0);
        int vertical = args.getInt(1);
        int horizontal = args.getInt(2);
        this.update(id, vertical, horizontal, callback);
        return true;
    }
}
