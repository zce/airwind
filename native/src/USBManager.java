package me.zce.airwind;

import android.app.PendingIntent;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.hardware.usb.UsbConstants;
import android.hardware.usb.UsbDevice;
import android.hardware.usb.UsbDeviceConnection;
import android.hardware.usb.UsbEndpoint;
import android.hardware.usb.UsbInterface;
import android.hardware.usb.UsbManager;
import android.widget.Toast;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;

import com.sun.jna.Pointer;

import me.zce.airwind.Lock;
import me.zce.airwind.USBDevice;
import me.zce.airwind.USBList;

public class USBManager {
    private static final String ACTION_USB_PERMISSION = "me.zce.airwind.USB_PERMISSION";

    // 宏定义
    public static byte USB_REQ_TYPE_STANDARD = 0x00;
    public static byte USB_REQ_TYPE_CLASS = 0x20;
    public static byte USB_REQ_TYPE_VENDOR = 0x40;
    public static byte USB_REQ_TYPE_MASK = 0x60;

    public static byte USB_REQ_RECIPIENT_DEVICE = 0x00;
    public static byte USB_REQ_RECIPIENT_INTERFACE = 0x01;
    public static byte USB_REQ_RECIPIENT_ENDPOINT = 0x02;
    public static byte USB_REQ_RECIPIENT_MASK = 0x03;

    public static byte SEND_ENCAPSULATED_COMMAND = 0x00;
    public static byte GET_ENCAPSULATED_RESPONSE = 0x01;
    public static byte SET_COMM_FEATURE = 0x02;
    public static byte GET_COMM_FEATURE = 0x03;
    public static byte CLEAR_COMM_FEATURE = 0x04;
    public static byte SET_LINE_CODING = 0x20;
    public static byte GET_LINE_CODING = 0x21;
    public static byte SET_CONTROL_LINE_STATE = 0x22;
    public static byte SEND_BREAK = 0x23;
    public static byte NO_CMD = (byte) 0xFF;
    // 以下为自定义命令
    public static byte CMD_SNIFFER_STOP_READ = 0x30;
    public static byte CMD_ADC_STOP_READ = 0x40;
    public static byte CMD_GET_SERIAL_NUM = 0x50;
    public static byte CMD_RESET_DEVICE = 0x60;
    public static byte CMD_GET_CANFD_SENDED_NUM = 0x70;
    public static byte CMD_GET_CANFD_SENDED_MSG = 0x71;
    public static byte CMD_GET_CANFD_MSG = 0x72;
    public static byte CMD_GET_CANFD_DIAGNOSTIC = 0x73;
    public static byte CMD_GET_CANFD_BUS_ERROR = 0x74;
    public static byte CMD_WRITE_GPIO = (byte) 0x80;
    public static byte CMD_READ_GPIO = (byte) 0x81;

    private UsbManager usbManager;
    // private UsbDevice[] usbDevice=new UsbDevice[20];
    public HashMap<Integer, UsbDevice> usbDevMap;
    int DevNum = 0;
    private PendingIntent pendingIntent;
    Context context;

    public class USB2XXXDev {
        public int DevHandle;
        public UsbDevice usbDevice;
        public UsbInterface usbInterface = null;
        public UsbEndpoint[] BulkInEndpoint = new UsbEndpoint[8];
        public UsbEndpoint[] BulkOutEndpoint = new UsbEndpoint[8];
        public UsbDeviceConnection connection = null;

        USB2XXXDev(UsbDevice device) {
            usbDevice = device;
            usbInterface = usbDevice.getInterface(0);
            if (usbInterface == null) {
                return;
            }
            for (int i = 0; i < usbInterface.getEndpointCount(); i++) {
                UsbEndpoint ep = usbInterface.getEndpoint(i);
                //  look for bulk endpoint
                if (ep.getType() == UsbConstants.USB_ENDPOINT_XFER_BULK) {
                    if (ep.getDirection() == UsbConstants.USB_DIR_OUT) {
                        BulkOutEndpoint[ep.getEndpointNumber()] = ep;
                    } else {
                        BulkInEndpoint[ep.getEndpointNumber()] = ep;
                    }
                }
                System.out.println(String.format("ep[%d] %d %d %d", i, ep.getType(), ep.getDirection(), ep.getEndpointNumber()));
            }
            System.out.println("usbInterface.getEndpointCount() = " + usbInterface.getEndpointCount());
            connection = usbManager.openDevice(device);
            if (connection == null) {
                System.out.println("openDevice error");
                return;
            }
            DevHandle = connection.getFileDescriptor();
            connection.claimInterface(usbInterface, true);
            byte[] buffer = new byte[4];
            int ret = connection.controlTransfer(0x80 | USB_REQ_TYPE_VENDOR | USB_REQ_RECIPIENT_INTERFACE, CMD_GET_SERIAL_NUM, 0, 0, buffer, 4, 100);
            if (ret == 4) {
                System.out.println("ret = " + ret);
                int SerialNumber = buffer[0] | ((int) buffer[1] << 8) | ((int) buffer[2] << 16) | ((int) buffer[3] << 24);
                System.out.println("SerialNumber = " + String.format("0x%08X", SerialNumber));
                DevHandle = SerialNumber;
                USBList.INSTANCE.usb_list_add(DevHandle);
                System.out.println("list len = " + USBList.INSTANCE.usb_list_length());
            }
        }
    }

    public List<USB2XXXDev> USB2XXXDevList = new ArrayList<USB2XXXDev>();

    public int USBScanDev() {
        USBList.INSTANCE.usb_list_clear();
        USB2XXXDevList.clear();
        // 扫描已经连接的USB设备
        if (usbManager == null) {
            System.out.println("usbManager is null");
            return 0;
        }
        HashMap<String, UsbDevice> map = usbManager.getDeviceList();
        System.out.println("map.size = " + map.size());
        for (UsbDevice device : map.values()) {
            if (USBDevice.DEV_VID == device.getVendorId() && USBDevice.DEV_PID == device.getProductId()) {
                if (!usbManager.hasPermission(device)) {
                    pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(device, pendingIntent);
                }
                USB2XXXDev mUSB2XXXDev = new USB2XXXDev(device);
                if (mUSB2XXXDev.connection != null) {
                    USB2XXXDevList.add(mUSB2XXXDev);
                }
            }
        }
        return USB2XXXDevList.size();
    }

    public boolean USBInit(Context context) {
        this.context = context;
        usbManager = (UsbManager) context.getSystemService(context.USB_SERVICE);
        // 注册USB插拔检测服务
        IntentFilter filter = new IntentFilter();
        filter.addAction(UsbManager.ACTION_USB_DEVICE_DETACHED);
        filter.addAction(UsbManager.ACTION_USB_DEVICE_ATTACHED);
        filter.addAction(ACTION_USB_PERMISSION);
        filter.setPriority(Integer.MAX_VALUE); // 设置级别
        UsbDeviceDetachedBroadcastReceiver usbStateReceiver = new UsbDeviceDetachedBroadcastReceiver();
        context.registerReceiver(usbStateReceiver, filter);
        Lock.INSTANCE.lock_all_init();
        USBList.INSTANCE.usb_list_init();
        // 设置回调函数，必须调用
        USBDevice.INSTANCE.set_scan_dev(scan_dev);
        USBDevice.INSTANCE.set_open_dev(open_dev);
        USBDevice.INSTANCE.set_close_dev(close_dev);
        USBDevice.INSTANCE.set_reset_dev(reset_dev);
        USBDevice.INSTANCE.set_del_dead_device(del_dead_device);
        USBDevice.INSTANCE.set_USBBulkWriteData(USBBulkWriteData);
        USBDevice.INSTANCE.set_USBBulkReadData(USBBulkReadData);
        USBDevice.INSTANCE.set_USBCtrlData(USBCtrlData);
        // 扫描已经连接的设备
        USBScanDev();
        System.out.println("USBInit Success!" + USB2XXXDevList.size());
        return true;
    }

    // 定义回调函数接口
    public USBDevice.p_scan_dev scan_dev = new USBDevice.p_scan_dev() {
        @Override
        public int scan_dev(int usb_pid, int usb_vid, final Pointer pDevHandle) {
            for (int i = 0; i < USB2XXXDevList.size(); i++) {
                pDevHandle.setInt(i, USB2XXXDevList.get(i).DevHandle);
            }
            return USB2XXXDevList.size();
        }
    };
    public USBDevice.p_open_dev open_dev = new USBDevice.p_open_dev() {
        @Override
        public boolean open_dev(int DevHandle) {
            for (int i = 0; i < USB2XXXDevList.size(); i++) {
                if (DevHandle == USB2XXXDevList.get(i).DevHandle) {
                    return true;
                }
            }
            return false;
        }
    };
    public USBDevice.p_close_dev close_dev = new USBDevice.p_close_dev() {
        @Override
        public boolean close_dev(int DevHandle) {
            return true;
        }
    };
    public USBDevice.p_reset_dev reset_dev = new USBDevice.p_reset_dev() {
        @Override
        public boolean reset_dev(int DevHandle) {
            return true;
        }
    };
    public USBDevice.p_del_dead_device del_dead_device = new USBDevice.p_del_dead_device() {
        @Override
        public void del_dead_device() {

        }
    };
    public USBDevice.p_USBBulkWriteData USBBulkWriteData = new USBDevice.p_USBBulkWriteData() {
        @Override
        public boolean USBBulkWriteData(int DevHandle, int pipenum, final Pointer buffer, int len, int waittime) {
            for (int i = 0; i < USB2XXXDevList.size(); i++) {
                USB2XXXDev Dev = USB2XXXDevList.get(i);
                if (Dev.DevHandle == DevHandle) {
                    // byte[] buf = buffer.getByteArray(0,1);
                    int ret = Dev.connection.bulkTransfer(Dev.BulkOutEndpoint[pipenum & 0x03], buffer.getByteArray(0, len), len, waittime);
                    // System.out.println("USBBulkWriteData ret = "+ret);
                    if (ret == len) {
                        if ((len % Dev.BulkOutEndpoint[pipenum & 0x03].getMaxPacketSize()) == 0) {
                            Dev.connection.bulkTransfer(Dev.BulkOutEndpoint[pipenum & 0x03], buffer.getByteArray(0, len), 0, waittime);
                        }
                        return true;
                    } else {
                        return false;
                    }
                }
            }
            return false;
        }
    };
    public USBDevice.p_USBBulkReadData USBBulkReadData = new USBDevice.p_USBBulkReadData() {
        @Override
        public int USBBulkReadData(int DevHandle, int pipenum, final Pointer buffer, int len, int waittime) {
            for (int i = 0; i < USB2XXXDevList.size(); i++) {
                USB2XXXDev Dev = USB2XXXDevList.get(i);
                if (Dev.DevHandle == DevHandle) {
                    byte[] buf = new byte[40960];
                    int ret = Dev.connection.bulkTransfer(Dev.BulkInEndpoint[pipenum & 0x03], buf, len, waittime);
                    // System.out.println("USBBulkReadData ret = "+ret);
                    for (int j = 0; j < ret; j++) {
                        buffer.setByte(j, buf[j]);
                    }
                    return ret;
                }
            }
            return 0;
        }
    };
    public USBDevice.p_USBCtrlData USBCtrlData = new USBDevice.p_USBCtrlData() {
        @Override
        public boolean USBCtrlData(int DevHandle, byte request_type, byte bRequest, short wValue, short wIndex, final Pointer data, short wLength, int timeout) {
            for (int i = 0; i < USB2XXXDevList.size(); i++) {
                USB2XXXDev Dev = USB2XXXDevList.get(i);
                if (Dev.DevHandle == DevHandle) {
                    int ret = Dev.connection.controlTransfer(request_type, bRequest, wValue, wIndex, data.getByteArray(0, wLength), wLength, timeout);
                    return (ret == wLength);
                }
            }
            return false;
        }
    };

    // 定义USB插拔广播接收器
    class UsbDeviceDetachedBroadcastReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_DETACHED)) {
                Toast.makeText(context, "Device Detached", Toast.LENGTH_SHORT).show();
                USBScanDev();
            } else if (intent.getAction().equals(UsbManager.ACTION_USB_DEVICE_ATTACHED)) {
                Toast.makeText(context, "Device Attached", Toast.LENGTH_SHORT).show();
                UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (!usbManager.hasPermission(usbDevice)) {
                    pendingIntent = PendingIntent.getBroadcast(context, 0, new Intent(ACTION_USB_PERMISSION), 0);
                    usbManager.requestPermission(usbDevice, pendingIntent);
                }
            } else if (intent.getAction().equals(ACTION_USB_PERMISSION)) {
                // UsbDevice usbDevice = intent.getParcelableExtra(UsbManager.EXTRA_DEVICE);
                if (intent.getBooleanExtra(UsbManager.EXTRA_PERMISSION_GRANTED, false)) {
                    System.out.println("Have get permission");
                    USBScanDev();
                } else {
                    System.out.println("Have not get permission");
                }
            }
        }
    }
}
