package me.zce.airwind;

import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;

import android.util.Log;

/**
 * This class echoes a string called from JavaScript.
 */
public class Native extends CordovaPlugin {
    private int[] devices = new int[10];
    private int deviceCount = 0;

    private void update(int id, int vertical, int horizontal, CallbackContext callback) {
        // // TODO: to lin
        // Log.i("USB2LIN", "id: " + id + ", vertical:" + vertical + ", horizontal:" + horizontal);

        // 这里发送数据

        // 主机模式发送数据
        byte write_pid = 0x11;
        byte[] write_data = {0x01, 0x00, 0x00, (byte) 0xd0, 0x00, 0x00, 0x00, 0x00};
        int ret = USB2LINEX.INSTANCE.LIN_EX_MasterWrite(id, (byte) 0, write_pid, write_data, (byte) write_data.length, USB2LINEX.LIN_EX_CHECK_EXT);

        if (ret == USB2LINEX.LIN_EX_SUCCESS) {
            Log.i("USB2LIN", "【" + id + "】信号发送成功");
        } else {
            Log.i("USB2LIN", "【" + id + "】信号发送失败");
        }

        // 成功回调
        callback.success(id);
        // 失败回调
        // callback.error("message");
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        Log.i("USB2LIN", "开始初始化设备");

        // 扫描设备
        deviceCount = USBDevice.INSTANCE.USB_ScanDevice(devices);

        // 没找到设备
        if (deviceCount == 0) {
            Log.i("USB2LIN", "没找到设备");
            return;
        }

        Log.i("USB2LIN", "发现 " + deviceCount + " 个设备");

        // 打开全部设备
        for (int i = 0; i < deviceCount; i++) {
            int device = devices[i];
            // 打开设备
            boolean state = USBDevice.INSTANCE.USB_OpenDevice(device);

            if (!state) {
                Log.i("USB2LIN", "打开设备【" + device + "】失败");
                return;
            }

            Log.i("USB2LIN", "打开设备【" + device + "】成功");

            try {
                USBDevice.DEVICE_INFO info = new USBDevice.DEVICE_INFO();
                byte[] introduce = new byte[128];
                // 获取设备信息
                state = USBDevice.INSTANCE.DEV_GetDeviceInfo(device, info, introduce);
                if (!state) {
                    USBDevice.INSTANCE.USB_CloseDevice(device);
                    Log.i("USB2LIN", "获取设备【" + device + "】信息失败");
                    return;
                }

                Log.i("USB2LIN", "【" + device + "】Name: " + info.FirmwareName);
                Log.i("USB2LIN", "【" + device + "】Build Date: " + info.BuildDate);
                Log.i("USB2LIN", "【" + device + "】" + String.format("Firmware Version: v%d.%d.%d", (info.FirmwareVersion >> 24) & 0xFF, (info.FirmwareVersion >> 16) & 0xFF, info.FirmwareVersion & 0xFFFF));
                Log.i("USB2LIN", "【" + device + "】" + String.format("Hardware Version: v%d.%d.%d", (info.HardwareVersion >> 24) & 0xFF, (info.HardwareVersion >> 16) & 0xFF, info.HardwareVersion & 0xFFFF));
                Log.i("USB2LIN", "【" + device + "】Functions: " + introduce);
            } catch (Exception ep) {
                ep.printStackTrace();
            }

            // 初始化设备
            int result = USB2LINEX.INSTANCE.LIN_EX_Init(device, (byte) 0, 19200, (byte) 1);
            if (result == USB2LINEX.LIN_EX_SUCCESS) {
                Log.i("USB2LIN", "初始化设备【" + device + "】成功");
            } else {
                Log.i("USB2LIN", "初始化设备【" + device + "】失败: " + result);
            }
        }
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
