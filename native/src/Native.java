package me.zce.airwind;

import org.json.JSONArray;
import org.json.JSONException;

import org.apache.cordova.CordovaInterface;
import org.apache.cordova.CordovaPlugin;
import org.apache.cordova.CallbackContext;
import org.apache.cordova.CordovaWebView;

import android.webkit.WebView;
import android.webkit.WebSettings;
import android.util.Log;

/**
 * This class echoes a string called from JavaScript.
 */
public class Native extends CordovaPlugin {
    private static final String TAG = "USB2LIN";

    private USBManager usbManager = new USBManager();

    private int deviceCount = 0;
    private int[] devices = new int[5];

    private void init() {
        Log.i(TAG, "开始初始化 USB");

        if (!usbManager.USBInit(cordova.getActivity().getApplicationContext())) {
            Log.i(TAG, "初始化 USB 失败");
            return;
        }

        Log.i(TAG, "开始初始化设备");

        // 扫描设备
        deviceCount = USBDevice.INSTANCE.USB_ScanDevice(devices);

        // 没找到设备
        if (deviceCount == 0) {
            Log.i(TAG, "没找到设备");
            return;
        }

        Log.i(TAG, "发现 " + deviceCount + " 个设备");

        // 打开全部设备
        for (int i = 0; i < deviceCount; i++) {
            int device = devices[i];
            // 打开设备
            boolean state = USBDevice.INSTANCE.USB_OpenDevice(device);

            if (!state) {
                Log.i(TAG, "打开设备【" + device + "】失败");
                return;
            }

            Log.i(TAG, "打开设备【" + device + "】成功");

            try {
                USBDevice.DEVICE_INFO info = new USBDevice.DEVICE_INFO();
                byte[] introduce = new byte[128];
                // 获取设备信息
                state = USBDevice.INSTANCE.DEV_GetDeviceInfo(device, info, introduce);
                if (!state) {
                    USBDevice.INSTANCE.USB_CloseDevice(device);
                    Log.i(TAG, "获取设备【" + device + "】信息失败");
                    return;
                }

                Log.i(TAG, "【" + device + "】Name: " + info.FirmwareName);
                Log.i(TAG, "【" + device + "】Build Date: " + info.BuildDate);
                Log.i(TAG, "【" + device + "】" + String.format("Firmware Version: v%d.%d.%d", (info.FirmwareVersion >> 24) & 0xFF, (info.FirmwareVersion >> 16) & 0xFF, info.FirmwareVersion & 0xFFFF));
                Log.i(TAG, "【" + device + "】" + String.format("Hardware Version: v%d.%d.%d", (info.HardwareVersion >> 24) & 0xFF, (info.HardwareVersion >> 16) & 0xFF, info.HardwareVersion & 0xFFFF));
                Log.i(TAG, "【" + device + "】Functions: " + introduce);
            } catch (Exception ep) {
                ep.printStackTrace();
            }

            // 初始化设备
            int result = USB2LINEX.INSTANCE.LIN_EX_Init(device, (byte) 0, 19200, (byte) 1);
            if (result == USB2LINEX.LIN_EX_SUCCESS) {
                Log.i(TAG, "初始化设备【" + device + "】成功");
            } else {
                Log.i(TAG, "初始化设备【" + device + "】失败: " + result);
            }
        }
    }

    private void update(int id, int vertical, int horizontal, CallbackContext callback) {
        // 没找到默认 USB 设备
        if (devices[0] == 0) {
            // 尝试初始化 USB
            init();
            // 如果还没找到就结束
            if (devices[0] == 0) return;
        }

        // 主机模式发送数据
        byte write_pid = 0x11;

        // 水平电机信号
        byte horizontal_id = (byte) ((id + 1) * 16);
        byte horizontal_high = (byte) ((horizontal >> 8) & 0xff);
        byte horizontal_low = (byte) (horizontal & 0xff);
        byte[] horizontal_data = {horizontal_id, 0x00, 0x00, horizontal_high, horizontal_low, 0x00, 0x00, 0x00};

        // 垂直电机信号
        byte vertical_id = (byte) ((id + 1) * 16 + 1);
        byte vertical_high = (byte) ((vertical >> 8) & 0xff);
        byte vertical_low = (byte) (vertical & 0xff);
        byte[] vertical_data = {vertical_id, 0x00, 0x00, vertical_high, vertical_low, 0x00, 0x00, 0x00};

        // 通过默认 USB 设备写入 lin 信号
        int horizontal_ret = USB2LINEX.INSTANCE.LIN_EX_MasterWrite(devices[0], (byte) 0, write_pid, horizontal_data, (byte) horizontal_data.length, USB2LINEX.LIN_EX_CHECK_EXT);
        int vertical_ret = USB2LINEX.INSTANCE.LIN_EX_MasterWrite(devices[0], (byte) 0, write_pid, vertical_data, (byte) vertical_data.length, USB2LINEX.LIN_EX_CHECK_EXT);

        if (horizontal_ret == USB2LINEX.LIN_EX_SUCCESS && vertical_ret == USB2LINEX.LIN_EX_SUCCESS) {
            Log.i(TAG, "【" + id + "】信号发送成功");
            // 成功回调
            callback.success("【" + id + "】信号发送成功");
        } else {
            Log.i(TAG, "【" + id + "】信号发送失败");
            // 失败回调
            callback.error("【" + id + "】信号发送失败");
        }
    }

    private void close() {
        Log.i(TAG, "尝试关闭所有设备");
        for (int i = 0; i < deviceCount; i++) {
            int device = devices[i];
            boolean succeed = USBDevice.INSTANCE.USB_CloseDevice(device);
            if (succeed) {
                Log.i(TAG, "关闭设备【" + device + "】成功");
            } else {
                Log.i(TAG, "关闭设备【" + device + "】失败");
            }
        }
    }

    @Override
    public void initialize(CordovaInterface cordova, CordovaWebView webView) {
        // viewport adaptation patch
        WebView view = (WebView) webView.getView();
        WebSettings settings = view.getSettings();
        settings.setLoadWithOverviewMode(true);
        settings.setUseWideViewPort(true);
    }

    @Override
    public void onResume(boolean multitasking) {
        // init usb device
        init();
    }

    @Override
    public void onDestroy() {
        // close all devices
        close();
    }

    @Override
    public boolean execute(String action, JSONArray args, CallbackContext callback) throws JSONException {
        if (!action.equals("update")) return false;
        int id = args.getInt(0);
        int vertical = args.getInt(1);
        int horizontal = args.getInt(2);
        update(id, vertical, horizontal, callback);
        return true;
    }
}
