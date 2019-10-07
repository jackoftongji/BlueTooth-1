package ych.com.bluetooth.bluetooth;

import android.Manifest;
import android.bluetooth.BluetoothAdapter;
import android.bluetooth.BluetoothDevice;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.content.pm.PackageManager;
import android.graphics.Color;
import android.os.Build;
import android.os.Bundle;
import android.os.CountDownTimer;
import android.os.Environment;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.support.v7.app.AppCompatActivity;
import android.util.Log;
import android.view.View;
import android.view.ViewGroup;
import android.view.Window;
import android.view.WindowManager;
import android.widget.AdapterView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;

import java.io.IOException;
import java.io.InputStream;
import java.net.URL;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashSet;
import java.util.List;

import pl.droidsonroids.gif.GifDrawable;
import pl.droidsonroids.gif.GifImageView;
import ych.com.bluetooth.test.FirstActivity;
import ych.com.bluetooth.utils.BaseUtils;
import ych.com.bluetooth.utils.bluetooth.BlueToothTra;
import ych.com.bluetooth.utils.bluetooth.BlueToothUtil;
import ych.com.bluetooth.utils.bluetooth.ClsUtils;
import ych.com.bluetooth.R;
import ych.com.bluetooth.SelectBlueToothListener;
import ych.com.bluetooth.base.BaseActivity;
import ych.com.bluetooth.set.DataSettingActivity;

public class BlueToothActivity extends AppCompatActivity implements BlueToothContract.View {
    private GifImageView iv_search;
    private TextView tv_time;
    private ListView lv_device;
    private TextView tv_scan;
    private LinearLayout ll_search_error;
    private LinearLayout ll_search;
    private List<BluetoothDevice> deviceList;
    private SelectBlueToothListener listener;
    private BlueToothContract.Presenter presenter = null;

    private GifDrawable gifDrawable;

    private boolean isScan = false;

    private DeviceAdapter deviceAdapter = null;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
       // initWindows();
        requestAllPower();
        initLayout();
        initView();
        initData();
        initListener();
        
        IntentFilter intent = new IntentFilter();
        intent.addAction(BluetoothDevice.ACTION_FOUND);//搜索发现设备
        intent.addAction(BluetoothDevice.ACTION_BOND_STATE_CHANGED);//状态改变
        intent.addAction(BluetoothAdapter.ACTION_SCAN_MODE_CHANGED);//行动扫描模式改变了
        intent.addAction(BluetoothAdapter.ACTION_STATE_CHANGED);//动作状态发生了变化
        registerReceiver(searchDevices, intent);
    }


    public void initLayout() {
        setContentView(R.layout.activity_bluetooth);
    }


    public void initView() {
        iv_search = findViewById(R.id.iv_search);
        tv_time = findViewById(R.id.tv_time);
        lv_device = findViewById(R.id.lv_device);
        tv_scan = findViewById(R.id.tv_scan);
        ll_search_error = findViewById(R.id.ll_search_error);
        ll_search = findViewById(R.id.ll_search);
    }

    public void setSelectBlueToothListener(SelectBlueToothListener listener) {
        this.listener = listener;
    }


    public void initData() {
        try {
            gifDrawable = new GifDrawable(getResources(), R.drawable.loading);
            iv_search.setImageDrawable(gifDrawable);
        } catch (IOException e) {
            e.printStackTrace();
        }
        gifDrawable.stop();
        deviceList = new ArrayList<>();
        presenter = new BlueToothPresenter(BlueToothActivity.this, this);
        presenter.init();
        presenter.isOpen();

        BlueToothUtil.getINSTANCE().setBluetoothListener(new BlueToothListener() {
            @Override
            public void unpaired(BluetoothDevice device) {
                //已配对
               // view.showUnpaired(device);
                if (deviceList.contains(device)){
                    return;
                }
                deviceList.add(device);
                //deviceList = removeDuplicate(deviceList);
                //lv_device.setAdapter(deviceAdapter);
                deviceAdapter.notifyDataSetChanged();
            }

            @Override
            public void paired(BluetoothDevice device) {
                //为配对
                //view.showPaired(device);

            }

            @Override
            public void Scans() {
                //扫描结束
                //view.showScans();

            }
        });
    }


    public void initListener() {
        tv_scan.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (!isScan) {
                    ll_search.setVisibility(View.VISIBLE);
                    ll_search_error.setVisibility(View.GONE);
                    deviceList = new ArrayList<>();
                    deviceAdapter = new DeviceAdapter(BlueToothActivity.this, deviceList);
                    lv_device.setAdapter(deviceAdapter);
                    //扫描
                    tv_scan.setText("停止扫描");
                    gifDrawable.start();
                    presenter.open();
                    counDown();
                    presenter.scan();
                    isScan = true;
                } else {
                    //停止扫描
                    gifDrawable.stop();
                    tv_scan.setText("开始扫描");
                    presenter.cancelScan();
                    isScan = false;
                }

            }
        });
        lv_device.setOnItemClickListener(new AdapterView.OnItemClickListener() {
            @Override
            public void onItemClick(AdapterView<?> parent, View view, int position, long id) {
                presenter.cancelScan();
                BluetoothDevice device = deviceAdapter.getItem(position);
                //判断是否配对过
                if (10==device.getBondState()){
                    try{
                        ClsUtils.setPin(device.getClass(), device, "1234");
                        ClsUtils.createBond(device.getClass(), device);
                        ClsUtils.cancelPairingUserInput(device.getClass(), device);
                    } catch (Exception e) {
                        e.printStackTrace();
                    }

                    //未配对
                }else if (12==device.getBondState()){
                    //已配对
                    Intent intent1 = new Intent(BlueToothActivity.this, FirstActivity.class);
                    Bundle bundle = new Bundle();
                    bundle.putParcelable("device", device);
                    intent1.putExtras(bundle);
                    startActivity(intent1);
                    finish();
                }
//                //通过工具类ClsUtils,调用createBond方法
//                try {
//                    ClsUtils.createBond(device.getClass(), device);
//                    //1.确认配对
//                    // ClsUtils.setPairingConfirmation(device.getClass(), device, true);
//                    //2.终止有序广播
//                    //Log.i("order...", "isOrderedBroadcast:"+isOrderedBroadcast()+",isInitialStickyBroadcast:"+isInitialStickyBroadcast());
//                    // this.abortBroadcast();//如果没有将广播终止，则会出现一个一闪而过的配对框。
//                    //3.调用setPin方法进行配对...
//                    boolean ret= ClsUtils.setPin(device.getClass(), device, "1234");
//                    //ClsUtils.setPairingConfirmation(device.getClass(),device,true);
//
////                    if (ret) {
////                        Intent intent = new Intent(BlueToothActivity.this, DataSettingActivity.class);
////                        Bundle bundle = new Bundle();
////                        bundle.putParcelable("device", device);
////                        intent.putExtras(bundle);
////                        startActivity(intent);
////                    }
//
//                } catch (Exception e) {
//                    e.printStackTrace();
//                }

            }
        });


    }

    public void counDown() {
        final long[] currentTime = {0};
        /** 倒计时3秒，一次1秒 */
        new CountDownTimer(30 * 1000, 1000) {
            @Override
            public void onTick(long millisUntilFinished) {
                if (!isFinishing()) {
                    if (isScan) {
                        //倒计时的过程中回调该函数
                        tv_time.setText((millisUntilFinished) / 1000 + "S");
                    } else {
                        tv_time.setText("30S");
                        cancel();
                        gifDrawable.stop();
                    }
                } else {
                    cancel();
                    gifDrawable.stop();
                }
            }

            @Override
            public void onFinish() {
                //倒计时结束时回调该函数
                //停止扫描
                gifDrawable.stop();
                tv_scan.setText("开始扫描");
                presenter.cancelScan();
                isScan = false;
            }
        }.start();
    }


    @Override
    public void showPaired(BluetoothDevice device) {
        if (deviceList.contains(device)){
            return;
        }
        deviceList.add(device);
        deviceList = removeDuplicate(deviceList);
        lv_device.setAdapter(deviceAdapter);
        deviceAdapter.notifyDataSetChanged();
        Log.e("已配对", device.getBondState()+"/"+device.getName() + "/" + device.getAddress());
    }

    @Override
    public void showUnpaired(BluetoothDevice device) {
        if (deviceList.contains(device)){
            return;
        }
        deviceList.add(device);
        deviceList = removeDuplicate(deviceList);
        lv_device.setAdapter(deviceAdapter);
        deviceAdapter.notifyDataSetChanged();
        Log.e("未配对", device.getBondState()+"/"+device.getName() + "/" + device.getAddress());
    }

    @Override
    public void showScans() {
        if (deviceList == null || deviceList.size() == 0) {
            ll_search.setVisibility(View.GONE);
            ll_search_error.setVisibility(View.VISIBLE);
            tv_scan.setText("继续扫描");
            gifDrawable.stop();

        } else {
            Log.e("Scans", "扫描结束");
            //停止扫描
            tv_scan.setText("开始扫描");
            gifDrawable.stop();
        }
        isScan = false;


    }

    public List removeDuplicate(List<BluetoothDevice> list) {
        HashSet<BluetoothDevice> h = new HashSet(list);
        list.clear();
        list.addAll(h);
        return list;
    }

    @Override
    public void showOpen(boolean isOpen) {
        if (isOpen) {
            //蓝牙打开
            presenter.open();
            // presenter.scan();
        } else {
            //蓝牙未打开
        }
    }

    @Override
    public void setPresenter(BlueToothContract.Presenter presenter) {
        this.presenter = presenter;
    }


    public void requestAllPower() {
        if (ContextCompat.checkSelfPermission(this, Manifest.permission.CAMERA) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.READ_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED
                && ContextCompat.checkSelfPermission(this, Manifest.permission.WRITE_EXTERNAL_STORAGE) != PackageManager.PERMISSION_GRANTED) {
            if (ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.CAMERA)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.READ_EXTERNAL_STORAGE)
                    && ActivityCompat.shouldShowRequestPermissionRationale(this, Manifest.permission.WRITE_EXTERNAL_STORAGE)) {
            } else {
                ActivityCompat.requestPermissions(this,
                        new String[]{Manifest.permission.CAMERA,
                                Manifest.permission.READ_EXTERNAL_STORAGE,
                                Manifest.permission.WRITE_EXTERNAL_STORAGE}, 1);
            }
        }
    }

    @Override
    protected void onDestroy() {
        if (null!=searchDevices){
            unregisterReceiver(searchDevices);
        }
        if (null!=presenter){
            presenter.onDestroy();
        }
        super.onDestroy();
    }

    private BroadcastReceiver searchDevices = new BroadcastReceiver() {
        //接收
        public void onReceive(Context context, Intent intent) {
            String action = intent.getAction();
            Bundle b = intent.getExtras();
            Object[] lstName = b.keySet().toArray();

            // 显示所有收到的消息及其细节
            for (int i = 0; i < lstName.length; i++) {
                String keyName = lstName[i].toString();
                Log.e("bluetooth", keyName + ">>>" + String.valueOf(b.get(keyName)));
            }
            BluetoothDevice device;
            // 搜索发现设备时，取得设备的信息；注意，这里有可能重复搜索同一设备
            if (BluetoothDevice.ACTION_FOUND.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                //onRegisterBltReceiver.onBluetoothDevice(device);
            }
            //状态改变时
            else if (BluetoothDevice.ACTION_BOND_STATE_CHANGED.equals(action)) {
                device = intent.getParcelableExtra(BluetoothDevice.EXTRA_DEVICE);
                switch (device.getBondState()) {
                    case BluetoothDevice.BOND_BONDING://正在配对
                        Log.d("BlueToothTestActivity", "正在配对......");
                       // onRegisterBltReceiver.onBltIng(device);
                        break;
                    case BluetoothDevice.BOND_BONDED://配对结束
                        Log.d("BlueToothTestActivity", "完成配对");
                       // onRegisterBltReceiver.onBltEnd(device);
                        Intent intent1 = new Intent(BlueToothActivity.this, FirstActivity.class);
                        Bundle bundle = new Bundle();
                        bundle.putParcelable("device", device);
                        intent1.putExtras(bundle);
                        startActivity(intent1);
                        finish();
                        break;
                    case BluetoothDevice.BOND_NONE://取消配对/未配对
                        Log.d("BlueToothTestActivity", "取消配对");
                       // onRegisterBltReceiver.onBltNone(device);
                    default:
                        break;
                }
            }
        }
    };

}
