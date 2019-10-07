package ych.com.bluetooth.bluetooth;

import android.bluetooth.BluetoothDevice;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/06
 *      desc   : 蓝牙监听器
 *      version:
 * </pre>
 */

public interface BlueToothListener {
    void unpaired(BluetoothDevice device);
    void paired(BluetoothDevice device);
    void Scans();
}
