package ych.com.bluetooth.set.realTime;

import android.annotation.SuppressLint;
import android.content.Context;
import android.content.Intent;
import android.content.SharedPreferences;
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
import android.widget.ImageButton;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import com.melnykov.fab.FloatingActionButton;

import org.greenrobot.eventbus.EventBus;
import org.greenrobot.eventbus.Subscribe;
import org.greenrobot.eventbus.ThreadMode;

import java.util.ArrayList;
import java.util.Arrays;

import ych.com.bluetooth.R;
import ych.com.bluetooth.base.BaseFragment;
import ych.com.bluetooth.bluetooth.BlueToothTool;
import ych.com.bluetooth.db.CrudUtils;
import ych.com.bluetooth.db.Record;
import ych.com.bluetooth.set.ProjectFragment;
import ych.com.bluetooth.set.sensorAdapter;
import ych.com.bluetooth.utils.BaseUtils;
import ych.com.bluetooth.utils.bluetooth.BlueToothTra;

import static android.content.Context.MODE_PRIVATE;
import static ych.com.bluetooth.utils.Contract.recordList;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/08
 *      desc   : 实时数据界面
 *      version:
 * </pre>
 */

@SuppressLint("ValidFragment")
public class RealTimeFragment extends BaseFragment {
    private View view;
    private TextView tv_check_record;
    private FloatingActionButton btn_save;

    private ListView lv_sensor;

    private ych.com.bluetooth.set.sensorAdapter sensorAdapter;
    private static RealTimeFragment INSTANCE = null;
    private Context mContext;

    @SuppressLint("HandlerLeak")
    Handler handler = new Handler(){
        @Override
        public void handleMessage(Message msg) {
            super.handleMessage(msg);

            switch (msg.what){
                case 1:
                    String[] dataArr = (String[]) msg.obj;
                    Log.e("data", Arrays.toString(dataArr));
                    recordList = new ArrayList<>();
                    String time=BaseUtils.getINSTANCE().getCurrentTime();
                    if (!"".equals(dataArr[4])&&dataArr[4]!=null&&!"NO".equals(dataArr[4])){
                        Record record = new Record();
                        record.setCreateTime(time);
                        record.setProjectNum(dataArr[3]);
                        record.setCollectorNum(dataArr[2]);

                        record.setSensorNum(dataArr[4]);
                        record.setMeasuringPoint(dataArr[5]);
                        record.setCurrentValue(dataArr[6]);
                        recordList.add(record);
                    }
                    if (!"".equals(dataArr[7])&&dataArr[7]!=null&&!"NO".equals(dataArr[7])){
                        Record record = new Record();
                        record.setCreateTime(time);
                        record.setProjectNum(dataArr[3]);
                        record.setCollectorNum(dataArr[2]);

                        record.setSensorNum(dataArr[7]);
                        record.setMeasuringPoint(dataArr[8]);
                        record.setCurrentValue(dataArr[9]);
                        recordList.add(record);
                    }
                    if (!"".equals(dataArr[10])&&dataArr[10]!=null&&!"NO".equals(dataArr[10])){
                        Record record = new Record();
                        record.setCreateTime(time);
                        record.setProjectNum(dataArr[3]);
                        record.setCollectorNum(dataArr[2]);

                        record.setSensorNum(dataArr[10]);
                        record.setMeasuringPoint(dataArr[11]);
                        record.setCurrentValue(dataArr[12]);
                        recordList.add(record);
                    }
                    if (!"".equals(dataArr[13])&&dataArr[13]!=null&&!"NO".equals(dataArr[13])){
                        Record record = new Record();
                        record.setCreateTime(time);
                        record.setProjectNum(dataArr[3]);
                        record.setCollectorNum(dataArr[2]);

                        record.setSensorNum(dataArr[13]);
                        record.setMeasuringPoint(dataArr[14]);
                        record.setCurrentValue(dataArr[15]);
                        recordList.add(record);
                    }
                    if (!"".equals(dataArr[16])&&dataArr[16]!=null&&!"NO".equals(dataArr[16])){
                        Record record = new Record();
                        record.setCreateTime(time);
                        record.setProjectNum(dataArr[3]);
                        record.setCollectorNum(dataArr[2]);

                        record.setSensorNum(dataArr[16]);
                        record.setMeasuringPoint(dataArr[17]);
                        record.setCurrentValue(dataArr[18]);
                        recordList.add(record);
                    }
                    if (!"".equals(dataArr[19])&&dataArr[19]!=null&&!"NO".equals(dataArr[19])){
                        Record record = new Record();
                        record.setCreateTime(time);
                        record.setProjectNum(dataArr[3]);
                        record.setCollectorNum(dataArr[2]);

                        record.setSensorNum(dataArr[19]);
                        record.setMeasuringPoint(dataArr[20]);
                        record.setCurrentValue(dataArr[21]);
                        recordList.add(record);
                    }
                    if (!"".equals(dataArr[22])&&dataArr[22]!=null&&!"NO".equals(dataArr[22])){
                        Record record = new Record();
                        record.setCreateTime(time);
                        record.setProjectNum(dataArr[3]);
                        record.setCollectorNum(dataArr[2]);

                        record.setSensorNum(dataArr[22]);
                        record.setMeasuringPoint(dataArr[23]);
                        record.setCurrentValue(dataArr[24]);
                        recordList.add(record);
                    }
                    if (!"".equals(dataArr[25])&&dataArr[25]!=null&&!"NO".equals(dataArr[25])){
                        Record record = new Record();
                        record.setCreateTime(time);
                        record.setProjectNum(dataArr[3]);
                        record.setCollectorNum(dataArr[2]);

                        record.setSensorNum(dataArr[25]);
                        record.setMeasuringPoint(dataArr[26]);
                        record.setCurrentValue(dataArr[27]);
                        recordList.add(record);
                    }

                    sensorAdapter = new sensorAdapter(getActivity(), recordList);
                    lv_sensor.setAdapter(sensorAdapter);
                   //sensorAdapter.notifyDataSetChanged();
                    break;
                    default:
                        break;
            }

//            recordList = new ArrayList<>();
//            String crc=((String)(msg.obj)).substring(((String)(msg.obj)).length()-4);
//            String data=((String)(msg.obj)).substring(0,((String)(msg.obj)).length()-4);
//            //验证接受的数据
//           // Log.e("realData",data);
//           // Log.e("tag", BaseUtils.getINSTANCE().crc16((data).getBytes()).toUpperCase());
//            if (crc.equals(BaseUtils.getINSTANCE().crc16(data.getBytes()).toUpperCase())){
//                String[] arrayStr=data.split(",");
//                String time=BaseUtils.getINSTANCE().getCurrentTime();
//                for (int i=4;i<arrayStr.length;i+=3){
//                    Record record = new Record();
//                    record.setCreateTime(time);
//                    record.setProjectNum(arrayStr[3]);
//                    record.setCollectorNum(arrayStr[2]);
//                    record.setSensorNum(arrayStr[i]);
//                    record.setMeasuringPoint(arrayStr[i+1]);
//                    record.setCurrentValue(arrayStr[i+2]);
//                    recordList.add(record);
//                }
//            }
//            sensorAdapter = new sensorAdapter(getActivity(), recordList);
//            lv_sensor.setAdapter(sensorAdapter);
        }
    };

    public static RealTimeFragment getINSTANCE() {
        if (INSTANCE == null) {
            synchronized (ProjectFragment.class) {
                if (INSTANCE == null) {
                   // INSTANCE = new RealTimeFragment();
                }
            }
        }
        return INSTANCE;
    }

    @SuppressLint("ValidFragment")
    public RealTimeFragment() {
        EventBus.getDefault().register(this);
      //  this.mContext=mContext;


    }
    @Subscribe(threadMode = ThreadMode.MAIN)
    public void data(String[] arr){
        if ("Data".equals(arr[0])){
            Message message = new Message();
            message.what=1;
            message.obj=arr;
            handler.sendMessage(message);
        }
//        if ("Data".equals(msg.substring(0, msg.indexOf(",")))){
//            Message message = new Message();
//            message.obj=msg;
//            handler.sendMessage(message);
//        }

    }
    @Override
    public void onCreate(@Nullable Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Nullable
    @Override
    public View onCreateView(@NonNull LayoutInflater inflater, @Nullable ViewGroup container, @Nullable Bundle savedInstanceState) {
        if (view==null){
            view = LayoutInflater.from(getActivity()).inflate(R.layout.fragment_realtime, container, false);
        }
        return view;
    }

    @Override
    public void initView() {
        tv_check_record = view.findViewById(R.id.tv_check_record);
        lv_sensor = view.findViewById(R.id.lv_sensor);
        btn_save= view.findViewById(R.id.btn_save);
        btn_save.setShadow(true);
    }

    @Override
    public void initData() {
        SharedPreferences sp = getActivity().getSharedPreferences("config",MODE_PRIVATE);
        String isAuto=sp.getString("isAuto","");
        if ("1".equals(isAuto)){

        }else if ("0".equals(isAuto)){
            BlueToothTool.getINSTANCE(getActivity()).setData("GetData\n");
        }else{

        }

    }

    @Override
    public void initListener() {

        BlueToothTool.getINSTANCE(getActivity()).setRealTimeListener(new RealTimeListener() {
            @Override
            public void setData(String msg) {
                Message message = new Message();
                message.obj=msg;
                handler.sendMessage(message);
            }
        });
        tv_check_record.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(getActivity(),RecordActivity.class);
                startActivity(intent);
            }
        });
        btn_save.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                String data="";
                Snackbar.make(btn_save,"保存实时数据至历史记录中",Snackbar.LENGTH_SHORT).show();
                if (recordList!=null){
                    for (int i=0;i<recordList.size();i++){
                        CrudUtils.getINSTANCE().insert(recordList.get(i));
                        data+=recordList.toString()+"\n";
                    }
                }
                //将数据保存在本地文件中
                boolean b=BaseUtils.getINSTANCE().string2File( data);
                if (b){
                    Log.e("tag","数据写入文件成功");
                }else {
                    Log.e("tag","数据写入文件失败");
                }
            }
        });

    }
}
