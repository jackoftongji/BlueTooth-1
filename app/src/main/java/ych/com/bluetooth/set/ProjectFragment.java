package ych.com.bluetooth.set;

import android.Manifest;
import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.support.v4.app.ActivityCompat;
import android.support.v4.content.ContextCompat;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.AdapterView;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.Spinner;
import android.widget.TextView;
import android.widget.Toast;

import com.dommy.qrcode.util.Constant;
import com.google.zxing.activity.CaptureActivity;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ych.com.bluetooth.R;
import ych.com.bluetooth.bluetooth.BlueToothTool;
import ych.com.bluetooth.utils.BaseUtils;
import ych.com.bluetooth.utils.bluetooth.BlueToothTra;
import ych.com.bluetooth.base.BaseFragment;

import static android.app.Activity.RESULT_OK;
import static android.content.Context.MODE_APPEND;
import static android.content.Context.MODE_PRIVATE;
import static ych.com.bluetooth.utils.Contract.device;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/07
 *      desc   :
 *      version:
 * </pre>
 */

@SuppressLint("ValidFragment")
public class ProjectFragment extends BaseFragment {
    private View view;
    private TextView tv_confirm;
    private EditText et_number;
    private EditText et_sensor_number1;
    private EditText et_sensor_number2;
    private EditText et_measuring_point1;
    private EditText et_measuring_point2;

    private ImageView iv_scan1;
    private ImageView iv_scan2;
    private ImageView iv_auto;
    private ImageView iv_shoudong;
    private Spinner snr_time;
    private String time;
    private String rate = "";

    private String isAuto = "0";
    private String data;
    private Context mContext;

    private static ProjectFragment INSTANCE = null;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler() {
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what) {
                case 0:
                    et_number.setText((String) msg.obj);
                    break;
                case 1:
                    et_sensor_number1.setText((String) msg.obj);
                    break;
                case 2:
                    et_measuring_point1.setText((String) msg.obj);
                    break;
                case 3:
                    et_sensor_number2.setText((String) msg.obj);
                    break;
                case 4:
                    et_measuring_point2.setText((String) msg.obj);
                    break;
                case 5: //手动
                    iv_shoudong.setImageResource(R.drawable.yigouxuan);
                    iv_auto.setImageResource(R.drawable.weigouxuan);
                    break;
                case 6:  //自动
                    iv_auto.setImageResource(R.drawable.yigouxuan);
                    iv_shoudong.setImageResource(R.drawable.weigouxuan);
                    if (((String) msg.obj).equals("5s")) {
                        snr_time.setSelection(0, true);
                    } else if (((String) msg.obj).equals("5m")) {
                        snr_time.setSelection(1, true);
                    } else if (((String) msg.obj).equals("5h")) {
                        snr_time.setSelection(2, true);
                    } else {

                    }
                case 7:
                    String[] settingsArr=(String[]) msg.obj;
                    String projectNum=settingsArr[4];
                    String chuanhanqiNum1=settingsArr[5];
                    String celiangdian1=settingsArr[6];
                    String chuanhanqiNum2=settingsArr[7];
                    String celiangdian2=settingsArr[8];
                    String cejiqiType=settingsArr[1];
                    String cejiqiTime=settingsArr[2];

                    et_number.setText(projectNum);
                    et_sensor_number1.setText(chuanhanqiNum1);
                    et_measuring_point1.setText(celiangdian1);
                    et_sensor_number2.setText(chuanhanqiNum2);
                    et_measuring_point2.setText(celiangdian2);

                    if ("0".equals(cejiqiType)){
                        iv_auto.setImageResource(R.drawable.yigouxuan);
                        iv_shoudong.setImageResource(R.drawable.weigouxuan);

                        if (cejiqiTime.equals("5s")) {
                            snr_time.setSelection(0, true);
                        } else if (cejiqiTime.equals("5m")) {
                            snr_time.setSelection(1, true);
                        } else if (cejiqiTime.equals("5h")) {
                            snr_time.setSelection(2, true);
                        } else {

                        }
                    }else if ("1".equals(cejiqiType)){
                        iv_shoudong.setImageResource(R.drawable.yigouxuan);
                        iv_auto.setImageResource(R.drawable.weigouxuan);
                    }

                    Log.e("工程设置数据：",projectNum+":"+chuanhanqiNum1+":"
                            +celiangdian1+":"+chuanhanqiNum2+":"+celiangdian2+":"+
                            cejiqiType+":"+cejiqiTime);
                    break;
                default:
            }
        }
    };

    public static ProjectFragment getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (ProjectFragment.class) {
                if (INSTANCE == null) {
                    //  INSTANCE = new ProjectFragment();
                }
            }
        }
        return INSTANCE;
    }

    @SuppressLint("ValidFragment")
    public ProjectFragment() {
        // this.mContext = mContext;

    }

    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setSettings(String[] arr){
        if ("Settings".equals(arr[0])){
            Message message = new Message();
            message.what=7;
            message.obj=arr;
            handler.sendMessage(message);
        }
    }

    @Nullable
    @Override
    public View onCreateView(LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_project, container, false);
        return view;
    }

    @Override
    public void initView() {
        tv_confirm = view.findViewById(R.id.tv_confirm);
        et_number = view.findViewById(R.id.et_number);
        et_sensor_number1 = view.findViewById(R.id.et_sensor_number1);
        et_sensor_number2 = view.findViewById(R.id.et_sensor_number2);
        et_measuring_point1 = view.findViewById(R.id.et_measuring_point1);
        et_measuring_point2 = view.findViewById(R.id.et_measuring_point2);
        iv_scan1 = view.findViewById(R.id.iv_scan1);
        iv_scan2 = view.findViewById(R.id.iv_scan2);
        iv_auto = view.findViewById(R.id.iv_auto);
        iv_shoudong = view.findViewById(R.id.tv_shoudong);
        snr_time = view.findViewById(R.id.snr_time);
    }

    @Override
    public void initData() {
        BlueToothTool.getINSTANCE(getActivity()).setPjSettingLisener(new PjSettingLisener() {
            @Override
            public void projectNum(String projectNum) {
                Message msg = new Message();
                msg.what = 0;
                msg.obj = projectNum;
                handler.sendMessage(msg);
            }

            @Override
            public void sensorNum1(String sensorNum1) {
                Message msg = new Message();
                msg.what = 1;
                msg.obj = sensorNum1;
                handler.sendMessage(msg);
            }

            @Override
            public void measuringPoint1(String measuringPoint1) {
                Message msg = new Message();
                msg.what = 2;
                msg.obj = measuringPoint1;
                handler.sendMessage(msg);
            }

            @Override
            public void sensorNum2(String sensorNum2) {
                Message msg = new Message();
                msg.what = 3;
                msg.obj = sensorNum2;
                handler.sendMessage(msg);
            }

            @Override
            public void measuringPoint2(String measuringPoint2) {
                Message msg = new Message();
                msg.what = 4;
                msg.obj = measuringPoint2;
                handler.sendMessage(msg);
            }

            @Override
            public void setAuto(String auto, String rate) {
                SharedPreferences sp = getActivity().getSharedPreferences("config", MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                if (rate == null || "".equals(rate)) {
                    Message msg = new Message();
                    msg.what = 5;
                    handler.sendMessage(msg);
                    isAuto = auto;
                    time = "";
                } else {
                    Message msg = new Message();
                    msg.what = 6;
                    msg.obj = rate;
                    handler.sendMessage(msg);
                    isAuto = auto;
                }
                editor.putString("isAuto", isAuto);
                editor.apply();
                editor.commit();
            }
        });
    }

    @Override
    public void initListener() {
        snr_time.setOnItemSelectedListener(new AdapterView.OnItemSelectedListener() {
            @Override
            public void onItemSelected(AdapterView<?> parent, View view, int position, long id) {
                if ("0".equals(isAuto)) {
                    time = (String) snr_time.getSelectedItem();
                } else {
                    time = "";
                }
            }

            @Override
            public void onNothingSelected(AdapterView<?> parent) {
            }
        });
        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_number.getText().toString() != null) {
                    if (!et_number.getText().toString().trim().equals("")) {

                        if (et_sensor_number1.getText().toString() != null) {
                            if (!et_sensor_number1.getText().toString().trim().equals("")) {

                                if (et_measuring_point1.getText().toString() != null) {
                                    if (!et_measuring_point1.getText().toString().trim().equals("")) {

                                        if (et_sensor_number2.getText().toString() != null) {
                                            if (!et_sensor_number2.getText().toString().trim().equals("")) {

                                                if (et_measuring_point2.getText().toString() != null) {
                                                    if (!et_measuring_point2.getText().toString().trim().equals("")) {
                                                        data = "SetPj"
                                                                + "," + isAuto
                                                                + "," + time.trim()
                                                                + "," + "C001"
                                                                + "," + et_number.getText().toString().trim()
                                                                + "," + et_sensor_number1.getText().toString().trim()
                                                                + "," + et_measuring_point1.getText().toString().trim()
                                                                + "," + et_sensor_number2.getText().toString().trim()
                                                                + "," + et_measuring_point2.getText().toString().trim()
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," + "NO"
                                                                + "," ;
                                                        Log.e("sendData", data  );
                                                        BlueToothTool.getINSTANCE(getActivity()).setData(data + BaseUtils.getINSTANCE().crc16(data.getBytes()).toUpperCase() + "\n");
                                                    } else {
                                                        Snackbar.make(tv_confirm, "请输入测点号2", Snackbar.LENGTH_SHORT).show();
                                                        //Toast.makeText(getActivity(), "请输入测点号2", Toast.LENGTH_SHORT).show();
                                                    }
                                                } else {
                                                    Snackbar.make(tv_confirm, "请输入测点号2", Snackbar.LENGTH_SHORT).show();
                                                    // Toast.makeText(getActivity(), "请输入测点号2", Toast.LENGTH_SHORT).show();
                                                }
                                            } else {
                                                Snackbar.make(tv_confirm, "请输入测点号2", Snackbar.LENGTH_SHORT).show();
                                                //Toast.makeText(getActivity(), "请输入传感器编号2", Toast.LENGTH_SHORT).show();
                                            }
                                        } else {
                                            Snackbar.make(tv_confirm, "请输入传感器编号2", Snackbar.LENGTH_SHORT).show();
                                            //Toast.makeText(getActivity(), "请输入传感器编号2", Toast.LENGTH_SHORT).show();
                                        }
                                    } else {
                                        Snackbar.make(tv_confirm, "请输入测点号1", Snackbar.LENGTH_SHORT).show();
                                        //Toast.makeText(getActivity(), "请输入测点号1", Toast.LENGTH_SHORT).show();
                                    }
                                } else {
                                    Snackbar.make(tv_confirm, "请输入测点号1", Snackbar.LENGTH_SHORT).show();
                                    // Toast.makeText(getActivity(), "请输入测点号1", Toast.LENGTH_SHORT).show();
                                }
                            } else {
                                Snackbar.make(tv_confirm, "请输入传感器编号1", Snackbar.LENGTH_SHORT).show();
                                //Toast.makeText(getActivity(), "请输入传感器编号1", Toast.LENGTH_SHORT).show();
                            }
                        } else {
                            Snackbar.make(tv_confirm, "请输入传感器编号1", Snackbar.LENGTH_SHORT).show();
                            //Toast.makeText(getActivity(), "请输入传感器编号1", Toast.LENGTH_SHORT).show();
                        }
                    } else {
                        Snackbar.make(tv_confirm, "请输入工程编号", Snackbar.LENGTH_SHORT).show();
                        //Toast.makeText(getActivity(), "请输入工程编号", Toast.LENGTH_SHORT).show();
                    }
                } else {
                    Snackbar.make(tv_confirm, "请输入工程编号", Snackbar.LENGTH_SHORT).show();
                    //Toast.makeText(getActivity(), "请输入工程编号", Toast.LENGTH_SHORT).show();
                }


            }
        });
        iv_scan1.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 二维码扫码
                //Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.setComponent(new ComponentName("google.com.scna","com.google.zxing.activity.CaptureActivity"));
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                //intent.setClassName("google.com.scna","com.google.zxing.activity.CaptureActivity");
                startActivityForResult(intent, 1);
            }
        });
        iv_scan2.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                // 二维码扫码
                //Intent intent = new Intent(Intent.ACTION_VIEW);
                //intent.setComponent(new ComponentName("google.com.scna","com.google.zxing.activity.CaptureActivity"));
                Intent intent = new Intent(getActivity(), CaptureActivity.class);
                //intent.setClassName("google.com.scna","com.google.zxing.activity.CaptureActivity");
                startActivityForResult(intent, 2);
            }
        });
        iv_auto.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_auto.setImageResource(R.drawable.yigouxuan);
                iv_shoudong.setImageResource(R.drawable.weigouxuan);
                isAuto = "0";
                SharedPreferences sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("isAuto", "0");
                editor.apply();
                editor.commit();
            }
        });
        iv_shoudong.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_auto.setImageResource(R.drawable.weigouxuan);
                iv_shoudong.setImageResource(R.drawable.yigouxuan);
                isAuto = "1";
                time = "";
                SharedPreferences sp = getActivity().getSharedPreferences("config", Context.MODE_PRIVATE);
                SharedPreferences.Editor editor = sp.edit();
                editor.putString("isAuto", "1");
                editor.apply();
                editor.commit();
            }
        });
    }

    // 开始扫码
    private void startQrCode() {
        // 二维码扫码
        //Intent intent = new Intent(Intent.ACTION_VIEW);
        //intent.setComponent(new ComponentName("google.com.scna","com.google.zxing.activity.CaptureActivity"));
        Intent intent = new Intent(getActivity(), CaptureActivity.class);
        //intent.setClassName("google.com.scna","com.google.zxing.activity.CaptureActivity");
        startActivityForResult(intent, Constant.REQ_QR_CODE);
    }

    @Override
    public void onActivityResult(int requestCode, int resultCode, Intent data) {
        super.onActivityResult(requestCode, resultCode, data);
        //扫描结果回调
        if (requestCode == 1 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            et_measuring_point1.setText(scanResult);
        } else if (requestCode == 2 && resultCode == RESULT_OK) {
            Bundle bundle = data.getExtras();
            String scanResult = bundle.getString(Constant.INTENT_EXTRA_KEY_QR_SCAN);
            //将扫描出的信息显示出来
            et_measuring_point2.setText(scanResult);
        }
    }

}
