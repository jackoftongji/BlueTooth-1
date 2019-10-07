package ych.com.bluetooth.utils;

import android.bluetooth.BluetoothDevice;

import java.util.List;

import ych.com.bluetooth.db.Record;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/12
 *      desc   :
 *      version:
 * </pre>
 */

public class Contract {
    public static BluetoothDevice device;
    public static List<Record> recordList;

    // request参数
    public static final int REQ_QR_CODE = 11002; // // 打开扫描界面请求码
    public static final int REQ_PERM_CAMERA = 11003; // 打开摄像头

    public static final String INTENT_EXTRA_KEY_QR_SCAN = "qr_scan_result";
}
