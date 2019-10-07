package ych.com.bluetooth.bluetooth;

import android.bluetooth.BluetoothDevice;
import android.bluetooth.BluetoothSocket;
import android.content.Context;
import android.os.Handler;
import android.util.Log;
import android.widget.Toast;

import com.sun.mail.util.ASCIIUtility;

import org.greenrobot.eventbus.EventBus;

import java.io.BufferedReader;
import java.io.BufferedWriter;
import java.io.IOException;
import java.io.InputStream;
import java.io.InputStreamReader;
import java.io.OutputStream;
import java.io.OutputStreamWriter;
import java.lang.reflect.InvocationTargetException;
import java.lang.reflect.Method;
import java.util.Arrays;
import java.util.LinkedList;

import ych.com.bluetooth.set.PjSettingLisener;
import ych.com.bluetooth.set.WorkSettingListener;
import ych.com.bluetooth.set.realTime.RealTimeListener;
import ych.com.bluetooth.test.FirstListener;
import ych.com.bluetooth.utils.bluetooth.BlueToothTra;
/**
 * Created by Administrator on 2018/9/26 0026.
 */

public class BlueToothTool {
    private static final String TAG = "BlueToothTool";
    BluetoothSocket socket;
    static final int CONNECT_FAILED = 1;
    static final int CONNECT_SUCCESS = 5;
    static final int READ_FAILED = 2;
    static final int WRITE_FAILED = 3;
    static final int DATA = 4;
    private boolean isConnect = false;
    private PjSettingLisener pjSettingLisener;
    private RealTimeListener realTimeListener;
    private WorkSettingListener workSettingListener;
    private FirstListener firstListener;
    LinkedList<String> linkedList = new LinkedList<>();
    private static BlueToothTool INSTANCE;
    private Context mContext;
    private String currentData = "";

    private ConnectListener connectListener;

    public BlueToothTool(Context mContext) {
        this.mContext = mContext;
    }

    public static BlueToothTool getINSTANCE(Context mContext) {
        if (INSTANCE == null) {
            synchronized (BlueToothTra.class) {
                if (INSTANCE == null) {
                    INSTANCE = new BlueToothTool(mContext);
                }
            }
        }
        return INSTANCE;
    }

    public void setConnectListener(ConnectListener connectListener){
        this.connectListener=connectListener;
    }

    public void setPjSettingLisener(PjSettingLisener pjSettingLisener) {
        this.pjSettingLisener = pjSettingLisener;
    }

    public void setRealTimeListener(RealTimeListener realTimeListener) {
        this.realTimeListener = realTimeListener;
    }

    public void setWorkSettingListener(WorkSettingListener workSettingListener) {
        this.workSettingListener = workSettingListener;
    }

    public void setFirstListener(FirstListener firstListener){
        this.firstListener = firstListener;
    }

    /**
     * 数据源
     */
    public void setData(String data) {
        linkedList.add(data);
    }

    public String getData() {
        return linkedList.poll();
    }


    public void disConnect(){
        if (socket!=null){
            try {
                socket.close();
                socket=null;
            } catch (IOException e) {
                e.printStackTrace();
            }
        }
    }

    /**
     * 开辟连接线程的任务
     */
    public void connect(final BluetoothDevice device) {
        Thread thread = new Thread(new Runnable() {
            @Override
            public void run() {
                BluetoothSocket tmp = null;
                Method method;
                try {
                    method = device.getClass().getMethod("createRfcommSocket", new Class[]{int.class});
                    tmp = (BluetoothSocket) method.invoke(device, 1);
                } catch (NoSuchMethodException | InvocationTargetException | IllegalAccessException e) {
                    setState(CONNECT_FAILED);
                    e.printStackTrace();
                }
                socket = tmp;
                try {
                    if (!socket.isConnected()) {
                        socket.connect();
                    }
                    //Toast.makeText(mContext,"与"+device.getName()+"连接成功",Toast.LENGTH_SHORT).show();
                    isConnect = true;
                    setState(CONNECT_SUCCESS);
                    WriteTask writeTask = new WriteTask();
                    Readtask readtask = new Readtask();//连接成功后开启读取数据的线程
                    Log.e("tag", "单片机--建立连接成功");
                    writeTask.start();
                    readtask.start();
                } catch (IOException e) {
                    setState(CONNECT_FAILED);
                    e.printStackTrace();
                }
            }
        });
        new Thread(thread).start();
    }

    /**
     * 开辟线程读取任务
     */
    public class Readtask extends Thread {
        @Override
        public void run() {

                try {
                    Log.e("正在接收单片机数据","开始");
                    InputStream is = socket.getInputStream();
                    int len;
                    byte[] buf = new byte[1024];
                    StringBuilder b = new StringBuilder();
                    while ((len=is.read(buf))!=-1){
                        Log.e("开始读","1");
                        String str = new String(buf,0,len);
                        Log.e("单片机"+len,str);
                        Thread.sleep(5);
                        Log.e("开始读","-1");
                        if (str.contains("FF")){
                            break;
                        }
                        b.append(str);
                    }
                    firstListener.sendData(b.toString());
                    Log.e("单片机--结束",b+"---");
                } catch (IOException e) {
                    Log.e("错误",e.toString());
                    setState(READ_FAILED);
                    e.printStackTrace();
                } catch (InterruptedException e) {
                    e.printStackTrace();
                }

            if (socket != null) {
                try {
                    socket.close();
                } catch (IOException e) {
                    e.printStackTrace();
                }
            }
        }
    }

    /**
     * 开辟线程写任务
     */
    public class WriteTask extends Thread {

        @Override
        public void run() {
            try {
                while (true) {
                    String str = getData();
                    if (null == str) {
                        Thread.sleep(1000);
                        continue;
                    }
                    OutputStream os = null;
                    byte[] st = (str).getBytes();
                    os = socket.getOutputStream();
                    BufferedWriter writer = new BufferedWriter(new OutputStreamWriter(os));
                   // writer.write(str);
                    os.write(st);
                    os.flush();
                    Log.e("单片机---发送数据：", str);
                }

            } catch (IOException e) {
                setState(WRITE_FAILED);
                e.printStackTrace();
            } catch (InterruptedException e) {
                e.printStackTrace();
            }

        }
    }

    public void setState(int state) {
        switch (state) {
            case READ_FAILED:
                Log.e(TAG, "单片机--接受数据失败");
                break;
            case WRITE_FAILED:
                Log.e(TAG, "单片机--发送数据失败");
                break;
            case 1:
                Log.e(TAG, "单片机--是否连接成功"+state);
                break;
            default:
        }
        Log.e("单片机--state", String.valueOf(state));
    }
}
