package ych.com.bluetooth.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.content.Context;

import ych.com.bluetooth.utils.bluetooth.BlueToothUtil;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/06
 *      desc   :
 *      version:
 * </pre>
 */

public class BlueToothPresenter implements BlueToothContract.Presenter {
    private BlueToothContract.View view;
    private Context mContext;

    public BlueToothPresenter(Context mContext,BlueToothContract.View view){
        this.mContext=mContext;
        this.view=view;
    }
    @Override
    public void start() {

    }

    @Override
    public void init() {
        BlueToothUtil.getINSTANCE().initBlueToothAdapter(mContext);
    }

    @Override
    public void isOpen() {
        boolean isOpen=BlueToothUtil.getINSTANCE().isOpen();
        view.showOpen(isOpen);
    }

    @Override
    public void open() {
        BlueToothUtil.getINSTANCE().openBluetooth();
    }

    @Override
    public void close() {
        BlueToothUtil.getINSTANCE().closeBluetooth();
    }

    @Override
    public void scan() {
        BlueToothUtil.getINSTANCE().scanBluetooth();
        BlueToothUtil.getINSTANCE().setBluetoothListener(new BlueToothListener() {
            @Override
            public void unpaired(BluetoothDevice device) {
                //已配对
                view.showUnpaired(device);
            }

            @Override
            public void paired(BluetoothDevice device) {
                //为配对
               view.showPaired(device);

            }

            @Override
            public void Scans() {
                //扫描结束
                view.showScans();

            }
        });
    }

    @Override
    public void cancelScan() {
        BlueToothUtil.getINSTANCE().cancelScan();
    }

    @Override
    public void onDestroy() {
        BlueToothUtil.getINSTANCE().destroy();
    }

}
