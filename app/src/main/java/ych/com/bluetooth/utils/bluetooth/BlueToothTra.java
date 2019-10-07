package ych.com.bluetooth.utils.bluetooth;

import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothServerSocket;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.content.SharedPreferences;
import android.util.Log;

import java.io.IOException;
import java.io.InputStream;
import java.io.OutputStream;
import java.util.LinkedList;
import java.util.UUID;

import ych.com.bluetooth.set.PjSettingLisener;
import ych.com.bluetooth.set.WorkSettingListener;
import ych.com.bluetooth.set.realTime.RealTimeListener;

import static ych.com.bluetooth.utils.Contract.device;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/10
 *      desc   : 蓝牙数据传输
 *      version:
 * </pre>
 */

public class BlueToothTra {
    private static BlueToothTra INSTANCE = null;
    private Context mContext;
    private String data;
    BluetoothSocket clientSocket;
    LinkedList<String> linkedList = new LinkedList<>();
    private PjSettingLisener pjSettingLisener;
    private RealTimeListener realTimeListener;
    private WorkSettingListener workSettingListener;
    private final UUID MY_UUID = UUID.fromString("abcd1234-ab12-ab12-ab12-abcdef123456");//随便定义一个

    public static BlueToothTra getINSTANCE(Context mContext) {
        if (INSTANCE == null) {
            synchronized (BlueToothTra.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BlueToothTra(mContext);
                }
            }
        }
        return INSTANCE;
    }

    public BlueToothTra(Context mContext) {
        this.mContext = mContext;
    }

    public void setPjSettingLisener(PjSettingLisener pjSettingLisener){
        this.pjSettingLisener=pjSettingLisener;
    }
    public void setRealTimeListener(RealTimeListener realTimeListener){
        this.realTimeListener=realTimeListener;
    }

    public void setWorkSettingListener(WorkSettingListener workSettingListener){
        this.workSettingListener=workSettingListener;
    }
    /**
     * 数据源
     */
    public void setData(String data){
        linkedList.add(data);
    }
    public String getData(){
        return linkedList.poll();
    }

    public void runSend(){
        new Thread(new SendThread()).start();
    }
    public void runReceiver(){
        new Thread(new ReceiverThread()).start();
    }
    class SendThread implements Runnable{

        @Override
        public void run() {
            while (true){
                String data=getData();
                if (data==null){
                    try {
                        Thread.sleep(1000);
                        continue;
                    } catch (InterruptedException e) {
                        e.printStackTrace();
                    }
                }
                send(data);
            }


        }
    }
    class ReceiverThread implements Runnable{

        @Override
        public void run() {
            while (true){
                receiver();
            }
        }
    }

    /**
     * 发送数据
     */
    public void send(String data) {
        //创建客户端蓝牙Socket
        try {
            if (clientSocket==null){
                clientSocket = device.createRfcommSocketToServiceRecord(MY_UUID);
                //开始连接蓝牙，如果没有配对则弹出对话框提示我们进行配对
                clientSocket.connect();
            }

            //获得输出流（客户端指向服务端输出文本）
            OutputStream os = clientSocket.getOutputStream();
            if (os != null) {
                //往服务端写信息
                os.write(data.getBytes("utf-8"));
                os.flush();
            }
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    /**
     * 接受数据
     */
    public void receiver() {
        BluetoothServerSocket bluetoothServerSocket = null;
        BluetoothSocket bluetoothSocket = null;
        InputStream is;
        OutputStream os;
        try {
            final String NAME = "Bluetooth_Socket";
            BluetoothAdapter mBluetoothAdapter = BluetoothAdapter.getDefaultAdapter();
            bluetoothServerSocket = mBluetoothAdapter.listenUsingInsecureRfcommWithServiceRecord(NAME, MY_UUID);
            Log.e("tag","开始接受信息");
            bluetoothSocket = bluetoothServerSocket.accept();
            is = bluetoothSocket.getInputStream();
            os=bluetoothSocket.getOutputStream();
            while(true) {
                byte[] buffer =new byte[1024];
                int count = is.read(buffer);
                if (count==-1){
                    break;
                }
                String msg = new String(buffer,0,count,"utf-8");
                Log.e("接收苏剧",msg);
                SharedPreferences sp = mContext.getSharedPreferences("config",Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if ("Data".equals(msg.substring(0,msg.indexOf(",")))){
                    //实时数据
                    msg="Data,C001,17001,JLS001,H01,17.134,JLS002,H02,18.145,JLS003,H03,18.145,JLS004,H04,18.145,LS005,H05,18.145,LS006,H06,18.145,LS007,H07,18.145,LS008,H08,18.145,CD41";
                    realTimeListener.setData(msg);
                    //editor.putString("data",msg);
                }else if ("Settings".equals(msg.substring(0,msg.indexOf(",")))){

                    //工程设置数据
                    msg="Settings,1,5m,C001,17001,JLS001,H01,JLS002,H02,1,60.166.40.34,2001,C814";
                    String[] str = msg.split(",");
                    pjSettingLisener.setAuto(str[1],str[2]);
                    pjSettingLisener.projectNum(str[4]);
                    pjSettingLisener.sensorNum1(str[5]);
                    pjSettingLisener.measuringPoint1(str[6]);
                    pjSettingLisener.sensorNum2(str[7]);
                    pjSettingLisener.measuringPoint2(str[8]);

                    workSettingListener.setWorkStyle(str[9]);
                    workSettingListener.address(str[10]);
                    workSettingListener.port(str[11]);
                }else {

                }
                editor.apply();
                editor.commit();
//                Log.e("已接收到客户端的消息",msg);
//                //os.write(msg.getBytes());
//                BlueToothTra.getINSTANCE(mContext).setData(msg);
//                os.flush();


            }
        } catch (IOException e) {
            e.printStackTrace();
        }

    }


}
