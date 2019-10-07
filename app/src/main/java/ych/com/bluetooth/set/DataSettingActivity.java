package ych.com.bluetooth.set;

import android.annotation.SuppressLint;
import android.bluetooth.BluetoothDevice;
import android.content.Context;
import android.content.Intent;
import android.graphics.Color;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.design.widget.Snackbar;
import android.support.v4.app.FragmentManager;
import android.support.v4.app.FragmentTransaction;
import android.util.Log;
import android.view.KeyEvent;
import android.view.View;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;
import android.widget.Toast;
import ych.com.bluetooth.R;
import ych.com.bluetooth.base.BaseActivity;
import ych.com.bluetooth.bluetooth.BlueToothActivity;
import ych.com.bluetooth.bluetooth.BlueToothTool;
import ych.com.bluetooth.bluetooth.ConnectListener;
import ych.com.bluetooth.utils.bluetooth.BlueToothUtil;
import ych.com.bluetooth.SelectBlueToothListener;
import ych.com.bluetooth.set.realTime.RealTimeFragment;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/07
 *      desc   :
 *      version:
 * </pre>
 */

public class DataSettingActivity extends BaseActivity {
    private LinearLayout ll_project;
    private LinearLayout ll_work;
    private LinearLayout ll_real_time;
    private TextView tv_project;
    private TextView tv_work;
    private TextView tv_real_time;
    private TextView tv_name;
    private View view_project;
    private View view_work;
    private View view_real_time;
    private SelectBlueToothListener listener;
    private BluetoothDevice device;
    private Context mContext;
    private ImageView iv_close_bt;

    private ProjectFragment projectFragment;
    private WorkSetFragment workSetFragment;
    private RealTimeFragment realTimeFragment;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            switch (msg.what) {
                case 1:
                   // Toast.makeText(DataSettingActivity.this, "连接失败", Toast.LENGTH_SHORT).show();
                    Snackbar.make(tv_name,"连接失败",Snackbar.LENGTH_SHORT).show();
                    startActivity(new Intent(DataSettingActivity.this, BlueToothActivity.class));
                    finish();
                    break;
                case 5:
                   // Toast.makeText(DataSettingActivity.this, "连接成功", Toast.LENGTH_SHORT).show();
                    Snackbar.make(tv_name,"连接成功",Snackbar.LENGTH_SHORT).show();
                    break;
                default:
            }

        }
    };

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_setting);
    }

    @Override
    public void initView() {
        ll_project = findViewById(R.id.ll_project);
        tv_project = findViewById(R.id.tv_project);
        view_project = findViewById(R.id.view_project);
        ll_work = findViewById(R.id.ll_work);
        tv_work = findViewById(R.id.tv_work);
        view_work = findViewById(R.id.view_work);
        ll_real_time = findViewById(R.id.ll_real_time);
        tv_real_time = findViewById(R.id.tv_real_time);
        view_real_time = findViewById(R.id.view_real_time);
        tv_name = findViewById(R.id.tv_name);

        iv_close_bt = findViewById(R.id.iv_close_bt);

    }

    @Override
    public void initData() {
        if (null == mContext) {
            mContext = getApplicationContext();
        }

        projectFragment = new ProjectFragment();
        workSetFragment = new WorkSetFragment();
        realTimeFragment = new RealTimeFragment();

        BlueToothTool.getINSTANCE(mContext).setConnectListener(new ConnectListener() {
            @Override
            public void isConnect(int state) {
                Message message = new Message();
                message.what = state;
                handler.sendMessage(message);
            }
        });

        FragmentManager manager = getSupportFragmentManager();
        FragmentTransaction transaction = manager.beginTransaction();
        transaction.add(R.id.fragment, projectFragment);
        transaction.add(R.id.fragment, realTimeFragment);
        transaction.add(R.id.fragment, workSetFragment);
        transaction.show(projectFragment);
        transaction.hide(workSetFragment);
        transaction.hide(realTimeFragment);
        transaction.commit();
        tv_project.setTextColor(Color.parseColor("#555eee"));
        view_project.setBackgroundColor(Color.parseColor("#555eee"));

        Intent intent = this.getIntent();
        device = (BluetoothDevice) intent.getParcelableExtra("device");
        if (device != null) {
            if (device.getName() == null) {
                tv_name.setText(device.getAddress());
            } else {
                tv_name.setText(device.getName());
            }
            BlueToothTool.getINSTANCE(this).connect(device);
            BlueToothTool.getINSTANCE(this).setData("GetSettings\n");
        } else {
            tv_name.setText("未连接蓝牙");
        }
    }

    @Override
    public void initListener() {
       /* this.setOnBackListener(new OnGestureListener() {
            @Override
            public void onBack(boolean onBack) {
                finish();
            }

            @Override
            public void onLeft(boolean onLeft) {

            }

            @Override
            public void onClickTwice(boolean onClickTwice) {

            }
        });*/
        iv_close_bt.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                BlueToothUtil.getINSTANCE().closeBluetooth();
                BlueToothTool.getINSTANCE(mContext).disConnect();
                Toast.makeText(DataSettingActivity.this, "关闭蓝牙", Toast.LENGTH_SHORT).show();
                startActivity(new Intent(DataSettingActivity.this, BlueToothActivity.class));
                finish();
            }
        });
        ll_project.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.show(projectFragment);
                transaction.hide(workSetFragment);
                transaction.hide(realTimeFragment);
                transaction.commit();
                tv_project.setTextColor(Color.parseColor("#555eee"));
                view_project.setBackgroundColor(Color.parseColor("#555eee"));

                tv_work.setTextColor(Color.parseColor("#000000"));
                view_work.setBackgroundColor(Color.parseColor("#FFFFFF"));

                tv_real_time.setTextColor(Color.parseColor("#000000"));
                view_real_time.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        ll_work.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.hide(projectFragment);
                transaction.show(workSetFragment);
                transaction.hide(realTimeFragment);
                transaction.commit();
                tv_work.setTextColor(Color.parseColor("#555eee"));
                view_work.setBackgroundColor(Color.parseColor("#555eee"));

                tv_project.setTextColor(Color.parseColor("#000000"));
                view_project.setBackgroundColor(Color.parseColor("#FFFFFF"));

                tv_real_time.setTextColor(Color.parseColor("#000000"));
                view_real_time.setBackgroundColor(Color.parseColor("#FFFFFF"));
            }
        });
        ll_real_time.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                FragmentManager manager = getSupportFragmentManager();
                FragmentTransaction transaction = manager.beginTransaction();
                transaction.hide(projectFragment);
                transaction.hide(workSetFragment);
                transaction.show(realTimeFragment);
                transaction.commit();
                tv_project.setTextColor(Color.parseColor("#000000"));
                view_project.setBackgroundColor(Color.parseColor("#FFFFFF"));

                tv_work.setTextColor(Color.parseColor("#000000"));
                view_work.setBackgroundColor(Color.parseColor("#FFFFFF"));

                tv_real_time.setTextColor(Color.parseColor("#555eee"));
                view_real_time.setBackgroundColor(Color.parseColor("#555eee"));
            }
        });

    }

    @Override
    protected void onDestroy() {
        super.onDestroy();
       // BlueToothTool.getINSTANCE(mContext).disConnect();
    }
}
