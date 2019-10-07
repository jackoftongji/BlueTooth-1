package ych.com.bluetooth.utils.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.util.Log;

import ych.com.bluetooth.bluetooth.BlueToothListener;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/06
 *      desc   :
 *      version:
 * </pre>
 */

public class  BlueToothUtil {
    private static BlueToothUtil INSTANCE=null;
    private BluetoothAdapter mBluetoothAdapter=  BluetoothAdapter.getDefaultAdapter();;
    private Context mContext=null;
    private BluetoothBroadcastReceiver mReceiver=null;
    private BlueToothListener listener=null;
    public static BlueToothUtil getINSTANCE(){
        if (INSTANCE==null){
            synchronized (BlueToothUtil.class){
                if (INSTANCE==null){
                    INSTANCE = new BlueToothUtil();
                }
            }
        }
        return INSTANCE;
    }

    /**
     * 初始化蓝牙适配器
     */
    public void initBlueToothAdapter(Context mContext){
        this.mContext=mContext;
        if (mReceiver==null){
            mReceiver = new BluetoothBroadcastReceiver();
        }
        //注册设备被发现时的广播
        IntentFilter filter=new IntentFilter(BluetoothDevice.ACTION_FOUND);
        //搜索
        IntentFilter filter2=new IntentFilter(BluetoothAdapter.ACTION_DISCOVERY_FINISHED);
        IntentFilter filter3=new IntentFilter("android.bluetooth.device.action.PAIRING_REQUEST");
        mContext.registerReceiver(mReceiver,filter);
        mContext.registerReceiver(mReceiver,filter2);
        mContext.registerReceiver(mReceiver,filter3);
    }
    /**
     * 设置蓝牙监听器
     */
    public void setBluetoothListener(BlueToothListener listener){
        this.listener=listener;
    }

    /**
     * 蓝牙是否打开
     */
    public boolean isOpen(){
        if (mBluetoothAdapter!=null){
            return mBluetoothAdapter.isEnabled();
        }
        return false;
    }

    /**
     * 打开蓝牙
     */
    public void openBluetooth(){
        if (mBluetoothAdapter!=null){
            mBluetoothAdapter.enable();
        }
    }
    /**
     * 关闭蓝牙
     */
    public void closeBluetooth(){
        if (mBluetoothAdapter!=null){
            if (mBluetoothAdapter.isEnabled()){
                mBluetoothAdapter.disable();
            }
        }
    }

    /**
     * 扫描
     */
    public void scanBluetooth(){
        if (mBluetoothAdapter!=null){
            mBluetoothAdapter.startDiscovery();
        }

    }

    /**
     * 停止扫描
     */
    public void cancelScan(){
        if (mBluetoothAdapter!=null){
            mBluetoothAdapter.cancelDiscovery();
        }
    }

    class BluetoothBroadcastReceiver extends BroadcastReceiver{

        @Override
        public void onReceive(Context context, Intent intent) {
            String action=intent.getAction();
            if (action.equals(BluetoothDevice.ACTION_FOUND)){
               BluetoothDevice device= intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);

                if (device.getBondState()==BluetoothDevice.BOND_BONDED){
                    if (listener!=null){
                        //显示已配对设备
                        listener.paired(device);
                    }
                }else if (device.getBondState()==BluetoothDevice.BOND_NONE){
                    if (listener!=null){
                        //显示未配对设备
                        listener.unpaired(device);
                    }
                }else {

                }
            }else if (action.equals(BluetoothAdapter.ACTION_DISCOVERY_FINISHED)){
                if (listener!=null){
                    //搜索完成
                    listener.Scans();
                }
            }else if (action.equals(BluetoothAdapter.ACTION_STATE_CHANGED)){
                int blueState = intent.getIntExtra(BluetoothAdapter.EXTRA_STATE, 0);
                if (BluetoothAdapter.STATE_OFF==10){
                    //蓝牙关闭
                }else if (BluetoothAdapter.STATE_ON==12){
                    //蓝牙打开
                }
            }else if (action.equals("android.bluetooth.device.action.PAIRING_REQUEST")){
                BluetoothDevice btDevice = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                try{
                    //ClsUtils.setPin(btDevice.getClass(), btDevice, "1234"); // 手机和蓝牙采集器配对
                    //ClsUtils.createBond(btDevice.getClass(), btDevice);
                    //ClsUtils.cancelPairingUserInput(btDevice.getClass(), btDevice);
                } catch (Exception e) {
                    e.printStackTrace();
                }

            }
        }
    }

    public void destroy(){
        if (mReceiver!=null){
            mContext.unregisterReceiver(mReceiver);
        }
        if (INSTANCE!=null){
            INSTANCE=null;
        }
    }

}
