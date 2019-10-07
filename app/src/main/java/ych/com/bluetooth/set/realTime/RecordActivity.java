package ych.com.bluetooth.set.realTime;

import android.app.AlertDialog;
import android.app.Dialog;
import android.content.Intent;
import android.content.SharedPreferences;
import android.os.Bundle;
import android.os.Environment;
import android.util.Log;
import android.view.Gravity;
import android.view.LayoutInflater;
import android.view.View;
import android.view.Window;
import android.view.WindowManager;
import android.widget.ImageView;
import android.widget.LinearLayout;
import android.widget.ListView;
import android.widget.TextView;
import android.widget.Toast;

import java.io.File;
import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.PrintStream;
import java.util.ArrayList;
import java.util.Collections;
import java.util.List;

import ych.com.bluetooth.MailActivity;
import ych.com.bluetooth.R;
import ych.com.bluetooth.base.BaseActivity;
import ych.com.bluetooth.base.BaseGestureActivity;
import ych.com.bluetooth.base.OnGestureListener;
import ych.com.bluetooth.db.CrudUtils;
import ych.com.bluetooth.db.Record;
import ych.com.bluetooth.db.Title;
import ych.com.bluetooth.db.Type;
import ych.com.bluetooth.set.realTime.adapter.RecordAdapter;
import ych.com.bluetooth.utils.BaseUtils;

/**
 * <pre>
 *      author : ych
 *      email  : 1147471053@qq.com
 *      time   : 2018/08/15
 *      desc   :已记录数据界面
 *      version:
 * </pre>
 */

public class RecordActivity extends BaseGestureActivity {
    private ImageView iv_back;
    private ListView lv_record;
    private TextView tv_import;
    private List<String> timeList = new ArrayList<>();

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
    }

    @Override
    public void initLayout() {
        setContentView(R.layout.activity_record);
    }

    @Override
    public void initView() {
        iv_back = findViewById(R.id.iv_back);
        lv_record = findViewById(R.id.lv_record);
        tv_import = findViewById(R.id.tv_import);
    }

    @Override
    public void initData() {

        List<Record> list1 = CrudUtils.getINSTANCE().selectAll();
        for (int i = 0; i < list1.size(); i++) {
            Log.e("data---all",
                    list1.get(i).getProjectNum()
                            + ":" + list1.get(i).getCollectorNum()
                            + ":" + list1.get(i).getCreateTime()
                            + ":" + list1.get(i).getCurrentValue()
                            + ":" + list1.get(i).getSaveTime()
                            + ":" + list1.get(i).getMeasuringPoint()
                            + ":" + list1.get(i).getSensorNum());
        }

       List<Record> records=CrudUtils.getINSTANCE().selectTimeAndPj();
        for (int i=0;i<records.size();i++){
            Log.e("createTime:",records.get(i).getCreateTime());
            timeList.add(records.get(i).getCreateTime());
        }
        //倒叙
        Collections.reverse(timeList);
        //显示保存在数据中的数据
        List<Type> list = new ArrayList<>();
        for (int i = 0; i < timeList.size(); i++) {
            List<Record> records2=CrudUtils.getINSTANCE().selectTime(timeList.get(i));
            Title title = new Title();
            title.setProjectNum(records2.get(1).getProjectNum());
            title.setTime(records2.get(1).getCreateTime());
            Type type = new Type(title);
            for (int j = 0; j < records2.size(); j++) {
                type.addItem(records2.get(j));
            }
            list.add(type);
        }



        RecordAdapter adapter = new RecordAdapter(this, list);
         lv_record.setAdapter(adapter);
    }

    @Override
    public void initListener() {
        this.setOnBackListener(new OnGestureListener() {
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
        });
        iv_back.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                finish();
            }
        });
        tv_import.setOnClickListener(new View.OnClickListener() {

            @Override
            public void onClick(View v) {
                alertDialog();
            }
        });
    }

    public void alertDialog() {
        final Dialog dialog = new Dialog(this, R.style.MyDialog);
        LinearLayout root = (LinearLayout) LayoutInflater.from(this).inflate(R.layout.alert, null);
        LinearLayout ll_mail = root.findViewById(R.id.ll_mail);
        LinearLayout ll_server = root.findViewById(R.id.ll_server);
        ll_mail.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Intent intent = new Intent(RecordActivity.this, MailActivity.class);
                startActivity(intent);
                dialog.dismiss();
            }
        });
        ll_server.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View v) {
                Toast.makeText(RecordActivity.this, "上传服务器", Toast.LENGTH_SHORT).show();
                dialog.dismiss();
            }
        });
        dialog.setContentView(root);
        Window dialogWindow = dialog.getWindow();
        dialogWindow.setGravity(Gravity.BOTTOM);
        WindowManager.LayoutParams lp = dialogWindow.getAttributes(); // 获取对话框当前的参数值
        lp.width = (int) getResources().getDisplayMetrics().widthPixels; // 宽度
        root.measure(0, 0);
        lp.height = root.getMeasuredHeight();
        lp.alpha = 9f; // 透明度
        dialogWindow.setAttributes(lp);
        dialog.show();
    }
}
