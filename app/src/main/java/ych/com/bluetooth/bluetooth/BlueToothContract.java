package ych.com.bluetooth.bluetooth;

import android.bluetooth.BluetoothDevice;

import ych.com.bluetooth.base.BasePresenter;
import ych.com.bluetooth.base.BaseView;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/06
 *      desc   :
 *      version:
 * </pre>
 */

public interface BlueToothContract {
    interface View extends BaseView<Presenter>{
        void showPaired(BluetoothDevice device);
        void showUnpaired(BluetoothDevice device);
        void showScans();
        void showOpen(boolean isOpen);

    }
    interface Presenter extends BasePresenter{
        void init();
        void isOpen();
        void open();
        void close();
        void scan();
        void cancelScan();
        void onDestroy();

    }

}
