package ych.com.bluetooth.set;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.os.Handler;
import android.os.Message;
import android.support.annotation.NonNull;
import android.support.annotation.Nullable;
import android.support.design.widget.Snackbar;
import android.util.Log;
import android.view.LayoutInflater;
import android.view.View;
import android.view.ViewGroup;
import android.widget.EditText;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.TextView;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import ych.com.bluetooth.R;
import ych.com.bluetooth.base.BaseActivity;
import ych.com.bluetooth.base.BaseFragment;
import ych.com.bluetooth.bluetooth.BlueToothTool;
import ych.com.bluetooth.utils.BaseUtils;
import ych.com.bluetooth.utils.bluetooth.BlueToothTra;

import static ych.com.bluetooth.utils.Contract.device;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/13
 *      desc   : 工作设置
 *      version:
 * </pre>
 */

@SuppressLint("ValidFragment")
public class WorkSetFragment extends BaseFragment{
    private View view;
    private LinearLayout ll_zigbee;
    private LinearLayout ll_offline;
    private LinearLayout ll_online;

    private ImageView iv_zigbee;
    private ImageView iv_offline;
    private ImageView iv_online;

    private EditText et_address;
    private EditText et_port;

    private TextView tv_confirm;
    private int workStyle=0;
    private String data;
    private Context mContext;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);
            switch (msg.what){
                case 0:
                    if ("0".equals(((String)msg.obj))){
                        iv_zigbee.setImageResource(R.drawable.weigouxuan);
                        iv_offline.setImageResource(R.drawable.weigouxuan);
                        iv_online.setImageResource(R.drawable.yigouxuan);
                        workStyle=0;
                    }else if ("1".equals(((String)msg.obj))){
                        iv_zigbee.setImageResource(R.drawable.weigouxuan);
                        iv_offline.setImageResource(R.drawable.yigouxuan);
                        iv_online.setImageResource(R.drawable.weigouxuan);
                        workStyle=1;
                    }else if ("2".equals(((String)msg.obj))){
                        iv_zigbee.setImageResource(R.drawable.yigouxuan);
                        iv_offline.setImageResource(R.drawable.weigouxuan);
                        iv_online.setImageResource(R.drawable.weigouxuan);
                        workStyle=2;
                    }else {

                    }
                    break;
                case 1:et_address.setText(((String)msg.obj));
                    break;
                case 2:et_port.setText(((String)msg.obj));
                    break;
                case 3:
                    String[] settingArr =(String[]) msg.obj;
                    String workType=settingArr[21];
                    String netAddr=settingArr[22];
                    String port=settingArr[23];

                    if ("0".equals(workType)){
                        iv_zigbee.setImageResource(R.drawable.weigouxuan);
                        iv_offline.setImageResource(R.drawable.weigouxuan);
                        iv_online.setImageResource(R.drawable.yigouxuan);
                        workStyle=0;
                    }else if ("1".equals(workType)){
                        iv_zigbee.setImageResource(R.drawable.weigouxuan);
                        iv_offline.setImageResource(R.drawable.yigouxuan);
                        iv_online.setImageResource(R.drawable.weigouxuan);
                        workStyle=1;
                    }else if ("2".equals(workType)){
                        iv_zigbee.setImageResource(R.drawable.yigouxuan);
                        iv_offline.setImageResource(R.drawable.weigouxuan);
                        iv_online.setImageResource(R.drawable.weigouxuan);
                        workStyle=2;
                    }else {

                    }

                    et_address.setText(netAddr);
                    et_port.setText(port);

                    Log.e("工作设置：",workType+":"+netAddr+":"+port);
                    break;
                    default:

            }
        }
    };

    public WorkSetFragment(){
        //this.mContext=mContext;

    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void setSettings(String[] arr){
        if ("Settings".equals(arr[0])){
            Message message = new Message();
            message.what=3;
            message.obj=arr;
            handler.sendMessage(message);
        }
    }

    @Override
    public void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        EventBus.getDefault().register(this);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
       view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_workset,container,false);
       return view;
    }

    @Override
    public void initView() {
        ll_zigbee=view.findViewById(R.id.ll_zigbee);
        ll_offline=view.findViewById(R.id.ll_offline);
        ll_online=view.findViewById(R.id.ll_online);
        iv_offline = view.findViewById(R.id.iv_offline);
        iv_online=view.findViewById(R.id.iv_online);
        iv_zigbee=view.findViewById(R.id.iv_zigbee);
        et_address=view.findViewById(R.id.et_address);
        et_port=view.findViewById(R.id.et_port);

        tv_confirm=view.findViewById(R.id.tv_confirm);
    }

    @Override
    public void initData() {

    }

    @Override
    public void initListener() {
        BlueToothTool.getINSTANCE(getActivity()).setWorkSettingListener(new WorkSettingListener() {
            @Override
            public void setWorkStyle(String style) {
                Message message =new Message();
                message.what=0;
                message.obj=style;
                handler.sendMessage(message);
            }

            @Override
            public void address(String address) {
                Message message =new Message();
                message.what=1;
                message.obj=address;
                handler.sendMessage(message);
            }

            @Override
            public void port(String port) {
                Message message =new Message();
                message.what=2;
                message.obj=port;
                handler.sendMessage(message);
            }
        });


        ll_zigbee.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_zigbee.setImageResource(R.drawable.yigouxuan);
                iv_offline.setImageResource(R.drawable.weigouxuan);
                iv_online.setImageResource(R.drawable.weigouxuan);
                workStyle=2;
            }
        });
        ll_offline.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_zigbee.setImageResource(R.drawable.weigouxuan);
                iv_offline.setImageResource(R.drawable.yigouxuan);
                iv_online.setImageResource(R.drawable.weigouxuan);
                workStyle=1;
            }
        });
        ll_online.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                iv_zigbee.setImageResource(R.drawable.weigouxuan);
                iv_offline.setImageResource(R.drawable.weigouxuan);
                iv_online.setImageResource(R.drawable.yigouxuan);
                workStyle=0;
            }
        });

        tv_confirm.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                if (et_address.getText().toString()!=null&&!"".equals(et_address.getText().toString().trim())){
                    if (et_port.getText().toString()!=null&&!"".equals(et_port.getText().toString().trim())){
                        data="SetWork"
                                +","+workStyle
                                +","+et_address.getText().toString().trim()
                                +","+et_port.getText().toString()
                                +",";
                        BlueToothTool.getINSTANCE(getActivity()).setData(data+ BaseUtils.getINSTANCE().crc16(data.getBytes()).toUpperCase() + "\n");
                    }else {
                        Snackbar.make(tv_confirm,"端口号不能为空",Snackbar.LENGTH_SHORT).show();
                    }
                }else {
                    Snackbar.make(tv_confirm,"联网网址不能为空",Snackbar.LENGTH_SHORT).show();
                }
            }
        });
    }
}
