package me.zce.airwind;

import com.sun.jna.Library;
import com.sun.jna.Native;

public interface Lock extends Library {
    Lock INSTANCE = (Lock) Native.loadLibrary("USB2XXX", Lock.class);

    void lock_init(int DevHandle);

    void lock(int DevHandle);

    void unlock(int DevHandle);

    void lock_all_init();

    void lock_all();

    void unlock_all();

    void can_lock(int DevHandle, int CANIndex);

    void can_unlock(int DevHandle, int CANIndex);

    void lin_lock(int DevHandle, int LINIndex);

    void lin_unlock(int DevHandle, int LINIndex);

    void can_data_lock(int DevHandle, int CANIndex);

    void can_data_unlock(int DevHandle, int CANIndex);
}
