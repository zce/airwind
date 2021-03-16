package me.zce.airwind;
import java.util.Arrays;
import java.util.List;

import com.sun.jna.Library;
import com.sun.jna.Native;
import com.sun.jna.Pointer;
import com.sun.jna.Structure;


public interface USBList extends Library {
    USBList INSTANCE = (USBList) Native.loadLibrary("USB2XXX", USBList.class);

    //定义设备信息
    public class usb_list extends Structure {
        @Override
        protected List<String> getFieldOrder() {
            // TODO Auto-generated method stub
            return Arrays.asList(new String[]{"next", "prev", "dev", "handle", "DevHandle", "android_fd"});
        }

        public static class ByReference extends usb_list implements Structure.ByReference {
        }

        public static class ByValue extends usb_list implements Structure.ByValue {
        }

        public int next;
        public int prev;
        public int dev;
        public int handle;
        public int DevHandle;
        public int android_fd;
    }

    void usb_list_init();

    void usb_list_add(int DevHandle);

    Pointer usb_list_post(int DevHandle);

    boolean usb_list_is_empty();

    void usb_list_clear();

    void usb_list_del(int DevHandle);

    int usb_list_length();

    int usb_list_get_devnums(int[] pDevHandle);

    int usb_list_get_index(int DevHandle);
}
