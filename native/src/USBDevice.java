package me.zce.airwind;

import java.util.Arrays;
import java.util.List;

import com.sun.jna.Callback;
import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;

public interface USBDevice extends Library {
    USBDevice INSTANCE = (USBDevice) Native.loadLibrary("USB2XXX", USBDevice.class);

    //定义设备信息
    public class DEVICE_INFO extends Structure {
        @Override
        protected List<String> getFieldOrder() {
             // TODO Auto-generated method stub
             return Arrays.asList(new String[]{"FirmwareName", "BuildDate", "HardwareVersion", "FirmwareVersion", "SerialNumber", "Functions"});
        }

        public static class ByReference extends DEVICE_INFO implements Structure.ByReference {
        }

        public static class ByValue extends DEVICE_INFO implements Structure.ByValue {
        }

        public byte[] FirmwareName = new byte[32];    //固件名称字符串
        public byte[] BuildDate = new byte[32];        //固件编译时间字符串
        public int HardwareVersion;                //硬件版本号
        public int FirmwareVersion;                //固件版本号
        public int[] SerialNumber = new int[3];        //适配器序列号
        public int Functions;                    //适配器当前具备的功能
    }

    //定义USB数据传输回调函数
    interface p_scan_dev extends Callback {
        int scan_dev(int usb_pid, int usb_vid, final Pointer pDevHandle);
    }

    interface p_open_dev extends Callback {
        boolean open_dev(int DevHandle);
    }

    interface p_close_dev extends Callback {
        boolean close_dev(int DevHandle);
    }

    interface p_reset_dev extends Callback {
        boolean reset_dev(int DevHandle);
    }

    interface p_del_dead_device extends Callback {
        void del_dead_device();
    }

    interface p_USBBulkWriteData extends Callback {
        boolean USBBulkWriteData(int DevHandle, int pipenum, final Pointer buffer, int len, int waittime);
    }

    interface p_USBBulkReadData extends Callback {
        int USBBulkReadData(int DevHandle, int pipenum, final Pointer buffer, int len, int waittime);
    }

    interface p_USBCtrlData extends Callback {
        boolean USBCtrlData(int DevHandle, byte request_type, byte bRequest, short wValue, short wIndex, final Pointer data, short wLength, int timeout);
    }

    //回调函数赋值函数，必须全部调用
    void set_scan_dev(p_scan_dev value);

    void set_open_dev(p_open_dev value);

    void set_close_dev(p_close_dev value);

    void set_reset_dev(p_reset_dev value);

    void set_del_dead_device(p_del_dead_device value);

    void set_USBBulkWriteData(p_USBBulkWriteData value);

    void set_USBBulkReadData(p_USBBulkReadData value);

    void set_USBCtrlData(p_USBCtrlData value);

    //定义设备的PID和VID
    public static int DEV_PID = 0x7918;
    public static int DEV_VID = 0x0483;
    //定义电压输出值
    public static byte POWER_LEVEL_NONE = 0;    //不输出
    public static byte POWER_LEVEL_1V8 = 1;    //输出1.8V
    public static byte POWER_LEVEL_2V5 = 2;    //输出2.5V
    public static byte POWER_LEVEL_3V3 = 3;    //输出3.3V
    public static byte POWER_LEVEL_5V0 = 4;    //输出5.0V

    /**
     * @param pDevHandle 返回每个设备的设备句柄，后面对设备的所有操作都需要该句柄
     * @param fd         安卓USB的设备的文件描述符
     * @brief 初始化USB设备，并扫描设备连接数，必须调用
     * @retval 扫描到的设备数量
     */
    int USB_ScanDevice(int[] pDevHandle);//非安卓设备用该函数

    /**
     * @param DevHandle 设备句柄
     * @brief 打开设备，必须调用
     * @retval 打开设备的状态
     */
    boolean USB_OpenDevice(int DevHandle);

    /**
     * @param DevHandle 设备句柄
     * @brief 关闭设备
     * @retval 关闭设备的状态
     */
    boolean USB_CloseDevice(int DevHandle);

    /**
     * @param DevHandle 设备索引号
     * @brief 复位设备程序，复位后需要重新调用USB_ScanDevice，USB_OpenDevice函数
     * @retval 复位设备的状态
     */
    boolean USB_ResetDevice(int DevHandle);

    /**
     * @param DevHandle    设备句柄
     * @param pDevInfo     设备信息存储结构体指针
     * @param pFunctionStr 设备功能说明字符串
     * @brief 获取设备信息，比如设备名称，固件版本号，设备序号，设备功能说明字符串等
     * @retval 获取设备信息的状态
     */
    boolean DEV_GetDeviceInfo(int DevHandle, DEVICE_INFO pDevInfo, byte[] pFunctionStr);

    /**
     * @param DevHandle 设备句柄
     * @brief 擦出用户区数据
     * @retval 用户区数据擦出状态
     */
    boolean DEV_EraseUserData(int DevHandle);

    /**
     * @param DevHandle  设备句柄
     * @param OffsetAddr 数据写入偏移地址，起始地址为0x00，用户区总容量为0x10000字节，也就是64KBye
     * @param pWriteData 用户数据缓冲区首地址
     * @param DataLen    待写入的数据字节数
     * @brief 向用户区域写入用户自定义数据，写入数据之前需要调用擦出函数将数据擦出
     * @retval 写入用户自定义数据状态
     */
    boolean DEV_WriteUserData(int DevHandle, int OffsetAddr, byte[] pWriteData, int DataLen);

    /**
     * @param DevHandle  设备句柄
     * @param OffsetAddr 数据写入偏移地址，起始地址为0x00，用户区总容量为0x10000字节，也就是64KBye
     * @param pReadData  用户数据缓冲区首地址
     * @param DataLen    待读出的数据字节数
     * @brief 从用户自定义数据区读出数据
     * @retval 读出用户自定义数据的状态
     */
    boolean DEV_ReadUserData(int DevHandle, int OffsetAddr, byte[] pReadData, int DataLen);

    /**
     * @param DevHandle  设备索引号
     * @param PowerLevel 输出电压值
     * @brief 设置可变电压输出引脚输出电压值
     * @retval 设置输出电压状态
     */
    boolean DEV_SetPowerLevel(int DevHandle, byte PowerLevel);
}
