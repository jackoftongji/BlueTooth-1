package ych.com.bluetooth.base;

import android.app.Activity;
import android.content.BroadcastReceiver;
import android.content.Context;
import android.content.Intent;
import android.content.IntentFilter;
import android.os.Bundle;

/**
 * 带有销毁标志的Activity基类
 * Created by wk on 2018/4/23.
 */

public abstract class CloseByFlagActivity extends BaseActivity implements SetFlag {
    private String mFlag;
    private CloseReceiver receiver;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        //动态方式注册广播接收者
        receiver = new CloseReceiver();
        IntentFilter filter = new IntentFilter();
        filter.addAction("broadcast.close"+mFlag+"Activity");
        registerReceiver(receiver, filter);
    }

    @Override
    public void setFlag(String Flag) {
        mFlag = Flag;
    }

    public class CloseReceiver extends BroadcastReceiver {
        @Override
        public void onReceive(Context context, Intent intent) {
            ((Activity) context).finish();
        }
    }

    @Override
    protected void onDestroy() {
        if(receiver!=null){
            unregisterReceiver(receiver);
        }
        super.onDestroy();
    }
}
